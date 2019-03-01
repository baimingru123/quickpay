package cn.pay.quickpay.service.impl;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.bean.MerchantChannelBean;
import cn.pay.quickpay.bean.TransactionBean;
import cn.pay.quickpay.channel.base.ChannelAbstract;
import cn.pay.quickpay.convert.Request2BeanConvert;
import cn.pay.quickpay.dao.IMerchantChannelDao;
import cn.pay.quickpay.dao.ITransactionDao;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.service.PayForAnotherService;
import cn.pay.quickpay.utils.platform.ChannelUseCheckUtil;
import cn.pay.quickpay.utils.platform.DateUtils;
import cn.pay.quickpay.utils.platform.GetChannelXmlBeanUtil;
import cn.pay.quickpay.utils.platform.ResultVOUtil;
import cn.pay.quickpay.valid.QuickpayRequsetValid;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import cn.pay.quickpay.xmlbean.VirtualChannelXmlBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 代付和预下单所需参数基本相同，校验部分可用预下单的校验方法
 * @author bmr
 * @time 2019-02-22 10:56
 */
@Service
@Slf4j
public class PayForAnotherServiceImpl implements PayForAnotherService {

    @Autowired
    private ITransactionDao transactionDao;

    @Autowired
    private IMerchantChannelDao merchantChannelDao;

    @Autowired
    private GetChannelXmlBeanUtil getChannelXmlBeanUtil;

    @Autowired
    private ChannelUseCheckUtil channelUseCheckUtil;

    @Override
    public ResultVO payForAnotherOne(HttpServletRequest request, HttpServletResponse response, MerchantBean mb, AgentXmlBean agentXmlBean, Properties agentProperties, Properties platformProperties) {
        String bizType=request.getParameter("bizType");
        String agentNo=request.getParameter("agentNo");

        //必填参数校验
        ResultVO addMerParamVaildResult=QuickpayRequsetValid.addOrderParamVaild(request);
        if(addMerParamVaildResult != null){
            log.info("【必填参数为空】bizType:{}，agentNo：{}，错误信息：{}", bizType, agentNo,addMerParamVaildResult.getRetMsg());
            return ResultVOUtil.error(addMerParamVaildResult);
        }

        //代付
        TransactionBean transactionBean   = Request2BeanConvert.convertTransactionBean(request);


        MerchantChannelBean merchantChannelBean= null;

        transactionBean.setAgentId(agentXmlBean.getId());
        transactionBean.setAgentNo(agentXmlBean.getAgent_no());
        transactionBean.setMerchantId(mb.getId());
        transactionBean.setMerchantUsername(mb.getMerName());
        transactionBean.setIsIntegral(1);//代付
        transactionBean.setStatus("INIT");
        transactionBean.setCreateTime(DateUtils.getCurrentDateTime());//创建日期


        TransactionBean existTransactionBean= transactionDao.getByOrderNumber(agentProperties, transactionBean.getOrderNumber());
        if(existTransactionBean!=null){
            return ResultVOUtil.error(ResultEnum.ORDER_NO_IS_EXIST);
        }

        //结算卡号
        transactionBean.setSettlementBankCardName(mb.getSettleBank());
        transactionBean.setSettlementBankCardNo(mb.getSettleBankCardNo());
        if(mb.getSettleType()==1){//T1
            transactionBean.setSettlementType("1");
        }else if(mb.getSettleType()==2){//D0
            transactionBean.setSettlementType("0");
        }


        List<MerchantChannelBean> merchantChannelList = merchantChannelDao.getByType(agentProperties, mb.getId(), "3");
        if(merchantChannelList.size()==0){
            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【结算】");
            return ResultVOUtil.error(ResultEnum.NOT_SUPPORT_TRANS_TYPE.getCode(),ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【结算】");

        }
        //计算最优通道
        for (MerchantChannelBean mcb : merchantChannelList) {
            merchantChannelBean = mcb;
        }

        //当前机构可用结算通道
        List<VirtualChannelXmlBean> balanceChannelList = agentXmlBean.getBalaceList();
        if(balanceChannelList.size()==0){
            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【结算】");
            return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【结算】");
        }
        //渠道商每种类型只配一个虚拟通道，因为渠道商下边的商户在各通道进件的时候费率固定
        //运营商每种类型可配多个虚拟通道，因为运营商下边的商户可以选择不同费率的通道进件
        VirtualChannelXmlBean virtualChannelXmlBean = balanceChannelList.get(0);

        //-1为小于、0为等于、1为大于
        //商户交易金额不能超过渠道商虚拟通道交易金额范围
        if(transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMin_money_score())==-1||transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMax_money_score())==1){//交易限额
            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"金额异常，单笔限额:" + virtualChannelXmlBean.getMin_money_score()+ "元-" + virtualChannelXmlBean.getMax_money_score() + "元");
            return ResultVOUtil.error(ResultEnum.TRANS_MONEY_SCORE_ERROR.getCode(),"单笔限额:" + virtualChannelXmlBean.getMin_money_score()+ "元-" + virtualChannelXmlBean.getMax_money_score() + "元");

        }
        String channelCode = virtualChannelXmlBean.getCode();//通道号
        int channelId 	= virtualChannelXmlBean.getDefault_channel();//通道id
        transactionBean.setChannelId(channelId);
        transactionBean.setChannelCode(channelCode);
//			//暂时放置
//			transactionBean.setChannelId(6);
//			transactionBean.setChannelCode("CX0008023");
//			//暂时放置

        //校验通道是否可用
        ResultVO useCheckResult=channelUseCheckUtil.useCheck(transactionBean.getChannelCode());
        if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
            return useCheckResult;
        }
        ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();


        //机构
        transactionBean.setAgentCollection(new BigDecimal(virtualChannelXmlBean.getBalance_fee()).divide(new BigDecimal(100)));//渠道代付费（分转元）
        transactionBean.setAgentService(new BigDecimal("0"));//渠道交易手续费=渠道费率（百分比）*交易金额
        transactionBean.setAgentTransactionRate(new BigDecimal("0"));//渠道交易费率（百分比）

        //通道
        transactionBean.setChannelCollectionCharges(new BigDecimal(channelXmlBean.getBalance_fee()).divide(new BigDecimal(100)));//通道代付费（分转元）
        transactionBean.setChannelServiceCharge(new BigDecimal("0"));//通道交易手续费=通道费率（百分比）*交易金额
        transactionBean.setChannelTransactionRate(new BigDecimal("0"));//通道交易费率（百分比）

        //商户
        transactionBean.setMerchantCollection(new BigDecimal(merchantChannelBean.getBalanceFee()).divide(new BigDecimal(100)));//代付费（分转元）
        transactionBean.setMerchantService(new BigDecimal("0"));//商户手续费=商户费率(百分比)*交易金额
        transactionBean.setMerchantTransactionRate(new BigDecimal("0"));//商户手续费率（百分比）

        transactionBean.setArrivalAmount(transactionBean.getOrderAmount().subtract(transactionBean.getMerchantService()).subtract(transactionBean.getMerchantCollection()));//到账金额=交易金额-商户手续费-代付费
        transactionBean.setArrivalAmount(transactionBean.getArrivalAmount().setScale(2, BigDecimal.ROUND_DOWN));//只保留两位小数，多余舍弃


        ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);

//			String platformOrderNumber = "102018022616431200001";

        //生成不重复订单号
        Map<String, String> map1 = new HashMap<>();
        map1.put("agent_id", agentXmlBean.getId()+"");
        map1.put("agent_no", agentXmlBean.getAgent_no());
        map1 = transactionDao.getOrderNumber(map1);
        String platformOrderNumber = map1.get("platformOrderNumber");
        log.info("【代付】渠道订单号{}，平台订单号:{}",transactionBean.getOrderNumber(),platformOrderNumber);
        transactionBean.setPlatformOrderNumber(platformOrderNumber);

        ResultVO payForAnotherOneResult= channel.payForAnotherOne(transactionBean, response, channelXmlBean, agentXmlBean, mb, agentProperties,merchantChannelBean,virtualChannelXmlBean);
        return payForAnotherOneResult;
    }
}

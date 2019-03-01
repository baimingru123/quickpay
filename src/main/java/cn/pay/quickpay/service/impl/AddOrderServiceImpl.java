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
import cn.pay.quickpay.service.AddOrderService;
import cn.pay.quickpay.utils.platform.ChannelUseCheckUtil;
import cn.pay.quickpay.utils.platform.DateUtils;
import cn.pay.quickpay.utils.platform.GetChannelXmlBeanUtil;
import cn.pay.quickpay.utils.platform.ResultVOUtil;
import cn.pay.quickpay.utils.platform.valid.VirtualChannelValid;
import cn.pay.quickpay.valid.QuickpayRequsetValid;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.ChannelDeductionSectionXmlBean;
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
 * @author bmr
 * @time 2019-02-21 17:04
 * 预下单service层
 */
@Slf4j
@Service
public class AddOrderServiceImpl implements AddOrderService {

    @Autowired
    private ITransactionDao transactionDao;

    @Autowired
    private IMerchantChannelDao merchantChannelDao;

    @Autowired
    private GetChannelXmlBeanUtil getChannelXmlBeanUtil;

    @Autowired
    private ChannelUseCheckUtil channelUseCheckUtil;

    @Override
    public ResultVO addOrder(HttpServletRequest request, HttpServletResponse response, MerchantBean mb, AgentXmlBean agentXmlBean, Properties agentProperties, Properties platformProperties) {
        String bizType=request.getParameter("bizType");
        String agentNo=request.getParameter("agentNo");

        //必填参数校验
        ResultVO addMerParamVaildResult=QuickpayRequsetValid.addOrderParamVaild(request);
        if(!ResultEnum.SUCCESS.getCode().equals(addMerParamVaildResult.getRetCode()) ){
            log.info("【必填参数为空】bizType:{}，agentNo：{}，错误信息：{}", bizType, agentNo,addMerParamVaildResult.getRetMsg());
            return ResultVOUtil.error(addMerParamVaildResult);
        }

        //商户交易（预下单的话需要调用确认支付的接口、下单）
        TransactionBean transactionBean   = Request2BeanConvert.convertTransactionBean(request);

        TransactionBean existTransactionBean= transactionDao.getByOrderNumber(agentProperties, transactionBean.getOrderNumber());
        if(existTransactionBean!=null){
            return ResultVOUtil.error(ResultEnum.ORDER_NO_IS_EXIST);
        }

        MerchantChannelBean merchantChannelBean = null;

        if (transactionBean.getType()==0){//快捷

            if (transactionBean.getIsIntegral()==0) {//不带积分
                List<MerchantChannelBean> merchantChannelList = merchantChannelDao.getByType(agentProperties, mb.getId(), "1");
                if(merchantChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【快捷无积分】");
                    return ResultVOUtil.error(ResultEnum.NOT_SUPPORT_TRANS_TYPE.getCode(),ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【快捷无积分】");
                }

                //计算最优通道，目前通道少还体现不出来，按照客户接入的类型，比如快捷返html、纯接口等区分选择最优通道
                for (MerchantChannelBean mcb : merchantChannelList) {
                    merchantChannelBean = mcb;
                }
            }else if(transactionBean.getIsIntegral()==1){//带积分
                List<MerchantChannelBean> merchantChannelList = merchantChannelDao.getByType(agentProperties, mb.getId(), "0");
                if(merchantChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【快捷有积分】");
                    return ResultVOUtil.error(ResultEnum.NOT_SUPPORT_TRANS_TYPE.getCode(),ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【快捷有积分】");
                }

                //计算最优通道，目前通道少还体现不出来，按照客户接入的类型，比如快捷返html、纯接口等区分选择最优通道
                for (MerchantChannelBean mcb : merchantChannelList) {
                    merchantChannelBean = mcb;
                }
            }
        }else if(transactionBean.getType()==1||transactionBean.getType()==2){//智能代还消费、还款（都是有积分）

            List<MerchantChannelBean> merchantChannelList = merchantChannelDao.getByType(agentProperties, mb.getId(), "2");
            if(merchantChannelList.size()==0){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【智能代还】");
                return ResultVOUtil.error(ResultEnum.NOT_SUPPORT_TRANS_TYPE.getCode(),ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【智能代还】");
            }

            //计算最优通道
            //智能代还牵扯到计划定制需要同一通道的问题，只给渠道商放一条通道，不做切换
            for (MerchantChannelBean mcb : merchantChannelList) {
                merchantChannelBean = mcb;
            }
        }else if(transactionBean.getType()==3){//代付
            List<MerchantChannelBean> merchantChannelList = merchantChannelDao.getByType(agentProperties, mb.getId(), "3");
            if(merchantChannelList.size()==0){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【代付】");
                return ResultVOUtil.error(ResultEnum.NOT_SUPPORT_TRANS_TYPE.getCode(),ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【代付】");
            }

            //计算最优通道
            for (MerchantChannelBean mcb : merchantChannelList) {
                merchantChannelBean = mcb;
            }
        }else if(transactionBean.getType()==4){//升级	升级通道商户不用进件
            List<VirtualChannelXmlBean> upgradeChannelList = agentXmlBean.getUpgradeList();
            if(upgradeChannelList.size()==0){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【升级】");
                return ResultVOUtil.error(ResultEnum.NOT_SUPPORT_TRANS_TYPE.getCode(),ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【升级】");
            }
            VirtualChannelXmlBean virtualChannelXmlBean = upgradeChannelList.get(0);
            merchantChannelBean = new MerchantChannelBean();
            merchantChannelBean.setChannelCode(virtualChannelXmlBean.getCode());
        }else if (transactionBean.getType()==5){//划扣


            List<MerchantChannelBean> merchantChannelList = merchantChannelDao.getByType(agentProperties, mb.getId(), "5");
            if(merchantChannelList.size()==0){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【划扣】");
                return ResultVOUtil.error(ResultEnum.NOT_SUPPORT_TRANS_TYPE.getCode(),ResultEnum.NOT_SUPPORT_TRANS_TYPE.getMessage()+"【划扣】");
            }

            //计算最优通道，目前通道少还体现不出来，按照客户接入的类型，比如快捷返html、纯接口等区分选择最优通道
            for (MerchantChannelBean mcb : merchantChannelList) {
                merchantChannelBean = mcb;
            }
        }


        String channelCode=merchantChannelBean.getChannelCode();
        //校验通道是否可用
        ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
        if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
            return useCheckResult;
        }
        ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();


        transactionBean.setAgentId(agentXmlBean.getId());
        transactionBean.setAgentNo(agentXmlBean.getAgent_no());
        transactionBean.setChannelId(channelXmlBean.getId());
        transactionBean.setChannelCode(channelXmlBean.getCode());
        VirtualChannelXmlBean virtualChannelXmlBean = null;
        if (transactionBean.getType()==0){//快捷

            if (transactionBean.getIsIntegral()==0) {//不带积分
                //当前机构可用无积分通道
                List<VirtualChannelXmlBean> noIntegralChannelList = agentXmlBean.getChannelList();
                if(noIntegralChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【无积分】");
                    return ResultVOUtil.error(ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getCode(),ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【无积分】");
                }
                virtualChannelXmlBean = noIntegralChannelList.get(0);

                //进行前置金额、费率校验
                ResultVO validResult=VirtualChannelValid.noIntegralChannelTransCheck(transactionBean,mb,virtualChannelXmlBean);
                if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                    return validResult;
                }



                //机构
                transactionBean.setAgentCollection(new BigDecimal(virtualChannelXmlBean.getDfee()).divide(new BigDecimal(100)));//渠道代付费（分转元）
                //结算方式（1 T1、2 D0）
                if(mb.getSettleType()==1){//T1
                    transactionBean.setAgentService(transactionBean.getOrderAmount().multiply(virtualChannelXmlBean.getFee1().divide(new BigDecimal(100))));//渠道交易手续费=渠道费率（百分比）*交易金额
                    transactionBean.setAgentTransactionRate(virtualChannelXmlBean.getFee1());//渠道交易费率（百分比）
                }else if(mb.getSettleType()==2){//D0
                    transactionBean.setAgentService(transactionBean.getOrderAmount().multiply(virtualChannelXmlBean.getFee0().divide(new BigDecimal(100))));//渠道交易手续费=渠道费率（百分比）*交易金额
                    transactionBean.setAgentTransactionRate(virtualChannelXmlBean.getFee0());//渠道交易费率（百分比）
                }

                //通道
                transactionBean.setChannelCollectionCharges(new BigDecimal(channelXmlBean.getDfee()).divide(new BigDecimal(100)));//通道代付费（分转元）
                transactionBean.setChannelServiceCharge(transactionBean.getOrderAmount().multiply(channelXmlBean.getFee0().divide(new BigDecimal(100))));//通道交易手续费=通道费率（百分比）*交易金额
                transactionBean.setChannelTransactionRate(channelXmlBean.getFee0());//通道交易费率（百分比）


            }else if(transactionBean.getIsIntegral()==1){//带积分
                //当前机构可用有积分通道
                List<VirtualChannelXmlBean> integralChannelList = agentXmlBean.getIntegralChannelList();
                if(integralChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【有积分】");
                    return ResultVOUtil.error(ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getCode(),ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【有积分】");
                }
                virtualChannelXmlBean = integralChannelList.get(0);

                //进行前置金额、费率校验
                ResultVO validResult=VirtualChannelValid.integralChannelTransCheck(transactionBean,mb,virtualChannelXmlBean);
                if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                    return validResult;
                }

                //机构费率
                transactionBean.setAgentCollection(new BigDecimal(virtualChannelXmlBean.getDfee()).divide(new BigDecimal(100)));//渠道代付费（分转元）
                //结算方式（1 T1、2 D0）
                if(mb.getSettleType()==1){//T1
                    transactionBean.setAgentService(transactionBean.getOrderAmount().multiply(virtualChannelXmlBean.getFee1().divide(new BigDecimal(100))));//渠道交易手续费=渠道费率（百分比）*交易金额
                    transactionBean.setAgentTransactionRate(virtualChannelXmlBean.getFee1());//渠道交易费率（百分比）
                }else if(mb.getSettleType()==2){//D0
                    transactionBean.setAgentService(transactionBean.getOrderAmount().multiply(virtualChannelXmlBean.getFee0().divide(new BigDecimal(100))));//渠道交易手续费=渠道费率（百分比）*交易金额
                    transactionBean.setAgentTransactionRate(virtualChannelXmlBean.getFee0());//渠道交易费率（百分比）
                }

                //通道费率
                transactionBean.setChannelCollectionCharges(new BigDecimal(channelXmlBean.getIntegral_dfee()).divide(new BigDecimal(100)));//通道代付费（分转元）
                transactionBean.setChannelServiceCharge(transactionBean.getOrderAmount().multiply(channelXmlBean.getIntegral_fee0().divide(new BigDecimal(100))));//通道交易手续费=通道费率（百分比）*交易金额
                transactionBean.setChannelTransactionRate(channelXmlBean.getIntegral_fee0());//通道交易费率（百分比）
            }

            //商户
            transactionBean.setMerchantCollection(new BigDecimal(merchantChannelBean.getFee()).divide(new BigDecimal(100)));//代付费（分转元）
            transactionBean.setMerchantService(transactionBean.getOrderAmount().multiply(merchantChannelBean.getFee0().divide(new BigDecimal(100))));//商户手续费=商户费率(百分比)*交易金额
            transactionBean.setMerchantTransactionRate(merchantChannelBean.getFee0());//商户手续费率（百分比）

        }else if(transactionBean.getType()==1){//智能代还消费(都是有积分？)
            //当前机构可用智能代还消费通道
            List<VirtualChannelXmlBean> repaymentChannelList = agentXmlBean.getRepaymentList();
            if(repaymentChannelList.size()==0){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【智能代还消费】");
                return ResultVOUtil.error(ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getCode(),ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【智能代还消费】");
            }
            virtualChannelXmlBean = repaymentChannelList.get(0);

            //进行前置金额、费率校验
            ResultVO validResult=VirtualChannelValid.repayCostChannelTransCheck(transactionBean,mb,virtualChannelXmlBean);
            if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                return validResult;
            }

            //机构
            transactionBean.setAgentCollection(new BigDecimal(virtualChannelXmlBean.getDfee()).divide(new BigDecimal(100)));//渠道代付费（分转元）
            //结算方式（1 T1、2 D0）
            if(mb.getSettleType()==1){//T1
                transactionBean.setAgentService(transactionBean.getOrderAmount().multiply(virtualChannelXmlBean.getFee1().divide(new BigDecimal(100))));//渠道交易手续费=渠道费率（百分比）*交易金额
                transactionBean.setAgentTransactionRate(virtualChannelXmlBean.getFee1());//渠道交易费率（百分比）
            }else if(mb.getSettleType()==2){//D0
                transactionBean.setAgentService(transactionBean.getOrderAmount().multiply(virtualChannelXmlBean.getFee0().divide(new BigDecimal(100))));//渠道交易手续费=渠道费率（百分比）*交易金额
                transactionBean.setAgentTransactionRate(virtualChannelXmlBean.getFee0());//渠道交易费率（百分比）
            }

            //通道
            transactionBean.setChannelCollectionCharges(new BigDecimal(channelXmlBean.getIntegral_dfee()).divide(new BigDecimal(100)));//通道代付费（分转元）
            transactionBean.setChannelServiceCharge(transactionBean.getOrderAmount().multiply(channelXmlBean.getIntegral_fee0().divide(new BigDecimal(100))));//通道交易手续费=通道费率（百分比）*交易金额
            transactionBean.setChannelTransactionRate(channelXmlBean.getIntegral_fee0());//通道交易费率（百分比）

            //商户
            transactionBean.setMerchantCollection(new BigDecimal(merchantChannelBean.getFee()).divide(new BigDecimal(100)));//代付费（分转元）
            transactionBean.setMerchantService(transactionBean.getOrderAmount().multiply(merchantChannelBean.getFee0().divide(new BigDecimal(100))));//商户手续费=商户费率(百分比)*交易金额
            transactionBean.setMerchantTransactionRate(merchantChannelBean.getFee0());//商户手续费率（百分比）

        }else if(transactionBean.getType()==2){//智能代还还款【只有单笔费用】

            //需要把费率算清楚
            //当前机构可用智能代还还款通道
            List<VirtualChannelXmlBean> repaymentChannelList = agentXmlBean.getRepaymentList();
            if(repaymentChannelList.size()==0){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【智能代还还款】");
                return ResultVOUtil.error(ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getCode(),ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【智能代还还款】");

            }
            virtualChannelXmlBean = repaymentChannelList.get(0);

            //进行前置金额、费率校验
            ResultVO validResult=VirtualChannelValid.repaymentChannelTransCheck(transactionBean,mb,virtualChannelXmlBean);
            if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                return validResult;
            }
            //机构
            transactionBean.setAgentCollection(new BigDecimal(virtualChannelXmlBean.getRepayment_fee()).divide(new BigDecimal(100)));//渠道代还费（分转元）
            transactionBean.setAgentService(new BigDecimal("0"));
            transactionBean.setAgentTransactionRate(new BigDecimal("0"));

            //通道
            transactionBean.setChannelCollectionCharges(new BigDecimal(channelXmlBean.getRepayment_fee()).divide(new BigDecimal(100)));//通道代还费（分转元）
            transactionBean.setChannelServiceCharge(new BigDecimal("0"));//
            transactionBean.setChannelTransactionRate(new BigDecimal("0"));//

            //商户
            transactionBean.setMerchantCollection(new BigDecimal(merchantChannelBean.getRepayFee()).divide(new BigDecimal(100)));//商户代还费（分转元）
            transactionBean.setMerchantService(new BigDecimal("0"));//
            transactionBean.setMerchantTransactionRate(new BigDecimal("0"));//


        }else if(transactionBean.getType()==3){//代付【只有单笔费用】
            //需要把费率算清楚
            //当前机构可用代付通道
            List<VirtualChannelXmlBean> balaceChannelList = agentXmlBean.getBalaceList();
            if(balaceChannelList.size()==0){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【代付】");
                return ResultVOUtil.error(ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getCode(),ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【代付】");
            }
            virtualChannelXmlBean = balaceChannelList.get(0);

            //进行前置金额、费率校验
            ResultVO validResult=VirtualChannelValid.repaymentChannelTransCheck(transactionBean,mb,virtualChannelXmlBean);
            if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                return validResult;
            }

            //机构
            transactionBean.setAgentCollection(new BigDecimal(virtualChannelXmlBean.getBalance_fee()).divide(new BigDecimal(100)));//渠道结算费（分转元）
            transactionBean.setAgentService(new BigDecimal("0"));
            transactionBean.setAgentTransactionRate(new BigDecimal("0"));

            //通道
            transactionBean.setChannelCollectionCharges(new BigDecimal(channelXmlBean.getBalance_fee()).divide(new BigDecimal(100)));//通道结算费（分转元）
            transactionBean.setChannelServiceCharge(new BigDecimal("0"));//
            transactionBean.setChannelTransactionRate(new BigDecimal("0"));//

            //商户
            transactionBean.setMerchantCollection(new BigDecimal(merchantChannelBean.getBalanceFee()).divide(new BigDecimal(100)));//商户结算费（分转元）
            transactionBean.setMerchantService(new BigDecimal("0"));//
            transactionBean.setMerchantTransactionRate(new BigDecimal("0"));//


        }else if(transactionBean.getType()==4){//升级【暂时内部用】升级通道不用给商户进件，因为钱到渠道商
            //需要把费率算清楚

            //当前机构可用升级通道
            List<VirtualChannelXmlBean> upgradeChannelList = agentXmlBean.getUpgradeList();
            if(upgradeChannelList.size()==0){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【升级】");
                return ResultVOUtil.error(ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getCode(),ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【升级】");

            }
            virtualChannelXmlBean = upgradeChannelList.get(0);

            //进行前置金额、费率校验
            ResultVO validResult=VirtualChannelValid.repaymentChannelTransCheck(transactionBean,mb,virtualChannelXmlBean);
            if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                return validResult;
            }

            //只用无积分
            //机构
            transactionBean.setAgentCollection(new BigDecimal(virtualChannelXmlBean.getDfee()).divide(new BigDecimal(100)));//渠道代付费（分转元）
            //结算方式（1 T1、2 D0）
            if(mb.getSettleType()==1){//T1
                transactionBean.setAgentService(transactionBean.getOrderAmount().multiply(virtualChannelXmlBean.getFee1().divide(new BigDecimal(100))));//渠道交易手续费=渠道费率（百分比）*交易金额
                transactionBean.setAgentTransactionRate(virtualChannelXmlBean.getFee1());//渠道交易费率（百分比）
            }else if(mb.getSettleType()==2){//D0
                transactionBean.setAgentService(transactionBean.getOrderAmount().multiply(virtualChannelXmlBean.getFee0().divide(new BigDecimal(100))));//渠道交易手续费=渠道费率（百分比）*交易金额
                transactionBean.setAgentTransactionRate(virtualChannelXmlBean.getFee0());//渠道交易费率（百分比）
            }
            //通道
            transactionBean.setChannelCollectionCharges(new BigDecimal(channelXmlBean.getDfee()).divide(new BigDecimal(100)));//通道代付费（分转元）
            transactionBean.setChannelServiceCharge(transactionBean.getOrderAmount().multiply(channelXmlBean.getFee0().divide(new BigDecimal(100))));//通道交易手续费=通道费率（百分比）*交易金额
            transactionBean.setChannelTransactionRate(channelXmlBean.getFee0());//通道交易费率（百分比）

            //商户
            transactionBean.setMerchantCollection(transactionBean.getAgentCollection());//代付费（分转元）
            transactionBean.setMerchantService(transactionBean.getAgentService());//商户手续费=商户费率(百分比)*交易金额
            transactionBean.setMerchantTransactionRate(transactionBean.getAgentTransactionRate());//商户手续费率（百分比）
        }else if(transactionBean.getType()==5){//划扣

            //当前机构可用划扣通道
            List<VirtualChannelXmlBean> delimitChannelList = agentXmlBean.getDelimitList();
            if(delimitChannelList.size()==0){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【划扣】");
                return ResultVOUtil.error(ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getCode(),ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage()+"【划扣】");

            }
            virtualChannelXmlBean = delimitChannelList.get(0);
            if(virtualChannelXmlBean.getIs_contract_sign()==1){//需要静默签署合同，异步的时候签署
                transactionBean.setIsContractSign(1);
            }

            //进行前置金额、费率校验
            ResultVO validResult=VirtualChannelValid.delimitChannelTransCheck(transactionBean,mb,virtualChannelXmlBean);
            if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                return validResult;
            }

            if(transactionBean.getPayType()==5){//协议支付  费率、代付费

                //机构
                transactionBean.setAgentCollection(new BigDecimal(virtualChannelXmlBean.getDfee()).divide(new BigDecimal(100)));//渠道代付费（分转元）
                //结算方式（1 T1、2 D0）
                if(mb.getSettleType()==1){//T1
                    transactionBean.setAgentService(transactionBean.getOrderAmount().multiply(virtualChannelXmlBean.getFee1().divide(new BigDecimal(100))));//渠道交易手续费=渠道费率（百分比）*交易金额
                    transactionBean.setAgentTransactionRate(virtualChannelXmlBean.getFee1());//渠道交易费率（百分比）
                }else if(mb.getSettleType()==2){//D0
                    transactionBean.setAgentService(transactionBean.getOrderAmount().multiply(virtualChannelXmlBean.getFee0().divide(new BigDecimal(100))));//渠道交易手续费=渠道费率（百分比）*交易金额
                    transactionBean.setAgentTransactionRate(virtualChannelXmlBean.getFee0());//渠道交易费率（百分比）
                }

                //通道
                BigDecimal channelCollectionCharges = new BigDecimal(channelXmlBean.getDfee()).divide(new BigDecimal(100));
                BigDecimal channelServiceCharge = transactionBean.getOrderAmount().multiply(channelXmlBean.getFee0().divide(new BigDecimal(100)));
                BigDecimal channelTransactionRate = channelXmlBean.getFee0();

                //协议支付如果开通了扣费区间，那么满足区间的交易不会按照费率和代付费计算通道成本，按照此处设定的值计算。
                if(channelXmlBean.getIs_open_deduction_section()==1){//开启扣费区间
                    List<ChannelDeductionSectionXmlBean> deductionSectionXmlBeanList = channelXmlBean.getDeductionSectionList();
                    if(deductionSectionXmlBeanList!=null&&deductionSectionXmlBeanList.size()>0){
                        for (ChannelDeductionSectionXmlBean channelDeductionSectionXmlBean : deductionSectionXmlBeanList) {
                            if(channelDeductionSectionXmlBean.getPay_type()==2){//2协议支付
                                //-1为小于、0为等于、1为大于
                                //商户交易金额不能超过渠道商虚拟通道交易金额范围
                                if(transactionBean.getOrderAmount().compareTo(channelDeductionSectionXmlBean.getPrice_start())>=0&&transactionBean.getOrderAmount().compareTo(channelDeductionSectionXmlBean.getPrice_end())==-1){//大于等于起始值，小于结束值
                                    channelCollectionCharges = channelDeductionSectionXmlBean.getCost_price();
                                    channelServiceCharge = new BigDecimal(0);
                                    channelTransactionRate = new BigDecimal(0);
                                    break;
                                }
                            }
                        }
                    }
                }

                transactionBean.setChannelCollectionCharges(channelCollectionCharges);//通道代付费（分转元）
                transactionBean.setChannelServiceCharge(channelServiceCharge);//通道交易手续费=通道费率（百分比）*交易金额
                transactionBean.setChannelTransactionRate(channelTransactionRate);//通道交易费率（百分比）

                //商户
                transactionBean.setMerchantCollection(new BigDecimal(merchantChannelBean.getFee()).divide(new BigDecimal(100)));//代付费（分转元）
                transactionBean.setMerchantService(transactionBean.getOrderAmount().multiply(merchantChannelBean.getFee0().divide(new BigDecimal(100))));//商户手续费=商户费率(百分比)*交易金额
                transactionBean.setMerchantTransactionRate(merchantChannelBean.getFee0());//商户手续费率（百分比）

            }else if(transactionBean.getPayType()==6){//代扣  单笔代扣费
                if(merchantChannelBean.getWithholdFee()<=0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"单笔代扣费率小于等于0");
                    return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_HK_FEE_SCORE_ERROR.getCode(),"请填写正确的单笔代扣费");
                }
                //机构
                transactionBean.setAgentCollection(new BigDecimal(virtualChannelXmlBean.getWithhold_fee()).divide(new BigDecimal(100)));//渠道代付费（分转元）
                transactionBean.setAgentService(new BigDecimal(0));//渠道交易手续费=渠道费率（百分比）*交易金额
                transactionBean.setAgentTransactionRate(new BigDecimal(0));//渠道交易费率（百分比）

                //通道
                BigDecimal channelCollectionCharges = new BigDecimal(0);

                //代扣如果开通了扣费区间，那么不满足区间的交易无法进行，满足区间的交易按照此处设定的值计算通道成本。
                if(channelXmlBean.getIs_open_deduction_section()==1){//开启扣费区间
                    List<ChannelDeductionSectionXmlBean> deductionSectionXmlBeanList = channelXmlBean.getDeductionSectionList();
                    if(deductionSectionXmlBeanList!=null&&deductionSectionXmlBeanList.size()>0){
                        for (ChannelDeductionSectionXmlBean channelDeductionSectionXmlBean : deductionSectionXmlBeanList) {
                            if(channelDeductionSectionXmlBean.getPay_type()==1){//1代扣
                                //-1为小于、0为等于、1为大于
                                //商户交易金额不能超过渠道商虚拟通道交易金额范围

                                if(transactionBean.getOrderAmount().compareTo(channelDeductionSectionXmlBean.getPrice_start())>=0&&transactionBean.getOrderAmount().compareTo(channelDeductionSectionXmlBean.getPrice_end())==-1){//大于等于起始值，小于结束值
                                    channelCollectionCharges = channelDeductionSectionXmlBean.getCost_price();
                                    break;
                                }
                            }
                        }
                    }
                }else{
                    channelCollectionCharges = new BigDecimal(channelXmlBean.getWithhold_fee()).divide(new BigDecimal(100));//通道代付费（分转元）
                }

                if(channelCollectionCharges.compareTo(new BigDecimal(0))==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"单笔代扣金额异常");
                    return ResultVOUtil.error(ResultEnum.TRANS_MONEY_SCORE_ERROR.getCode(),"单笔代扣金额异常");

                }
                transactionBean.setChannelCollectionCharges(channelCollectionCharges);//通道代付费（元）
                transactionBean.setChannelServiceCharge(new BigDecimal(0));//通道交易手续费=通道费率（百分比）*交易金额
                transactionBean.setChannelTransactionRate(new BigDecimal(0));//通道交易费率（百分比）

                //商户
                transactionBean.setMerchantCollection(new BigDecimal(merchantChannelBean.getWithholdFee()).divide(new BigDecimal(100)));//代付费（分转元）
                transactionBean.setMerchantService(new BigDecimal(0));//商户手续费=商户费率(百分比)*交易金额
                transactionBean.setMerchantTransactionRate(new BigDecimal(0));//商户手续费率（百分比）

                //-1为小于、0为等于、1为大于
                //商户代付费必须大于等于渠道代付费
                if(transactionBean.getChannelCollectionCharges().compareTo(transactionBean.getMerchantCollection())==1){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"商户单笔划扣手续费费必须小于渠道手续费");
                    return ResultVOUtil.error(ResultEnum.MERCHANT_OPEN_HK_FEE_SCORE_ERROR.getCode(),"商户单笔划扣手续费费必须小于渠道手续费");

                }

            }


        }

//			//动态加载通道配置文件
        ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);

        //商户通道
        transactionBean.setMerchantUsername(mb.getMerName());
        transactionBean.setMerchantId(mb.getId());

        //结算金额
        transactionBean.setArrivalAmount(transactionBean.getOrderAmount().subtract(transactionBean.getMerchantService()).subtract(transactionBean.getMerchantCollection()));//到账金额=交易金额-商户手续费-代付费
        transactionBean.setArrivalAmount(transactionBean.getArrivalAmount().setScale(2, BigDecimal.ROUND_DOWN));//只保留两位小数，多余舍弃
        //
        transactionBean.setCreateTime(DateUtils.getCurrentDateTime());//创建日期

        //结算卡号
        transactionBean.setSettlementBankCardName(mb.getSettleBank());
        transactionBean.setSettlementBankCardNo(mb.getSettleBankCardNo());
        if(mb.getSettleType()==1){//T1
            transactionBean.setSettlementType("1");
        }else if(mb.getSettleType()==2){//D0
            transactionBean.setSettlementType("0");
        }

        transactionBean.setStatus("INIT");

        //生成不重复订单号
        Map<String, String> map1 = new HashMap<>();
        map1.put("agent_id", agentXmlBean.getId()+"");
        map1.put("agent_no", agentXmlBean.getAgent_no());
        map1 = transactionDao.getOrderNumber(map1);
        String platformOrderNumber = map1.get("platformOrderNumber");
        log.info("【通过存储过程生成平台订单号】渠道订单号{}，平台订单号:{}",transactionBean.getOrderNumber(),platformOrderNumber);
        transactionBean.setPlatformOrderNumber(platformOrderNumber);
        ResultVO addOrderResult= channel.addOrder(transactionBean,merchantChannelBean, response,channelXmlBean,agentXmlBean,mb,agentProperties,virtualChannelXmlBean);

        return addOrderResult;

    }
}

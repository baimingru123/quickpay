package cn.pay.quickpay.service.impl;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.bean.TransactionBean;
import cn.pay.quickpay.channel.base.ChannelAbstract;
import cn.pay.quickpay.convert.Request2BeanConvert;
import cn.pay.quickpay.dao.ITransactionDao;
import cn.pay.quickpay.dto.PayConfirmDto;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.service.PayConfirmService;
import cn.pay.quickpay.utils.platform.ChannelUseCheckUtil;
import cn.pay.quickpay.utils.platform.GetChannelXmlBeanUtil;
import cn.pay.quickpay.utils.platform.ResultVOUtil;
import cn.pay.quickpay.valid.QuickpayRequsetValid;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * @author bmr
 * @time 2019-02-22 10:10
 */
@Service
@Slf4j
public class PayConfirmServiceImpl implements PayConfirmService {

    @Autowired
    private ITransactionDao transactionDao;

    @Autowired
    private ChannelUseCheckUtil channelUseCheckUtil;

    @Autowired
    private GetChannelXmlBeanUtil getChannelXmlBeanUtil;

    @Override
    public ResultVO quickPayConfirm(HttpServletRequest request, HttpServletResponse response, MerchantBean mb, AgentXmlBean agentXmlBean, Properties agentProperties, Properties platformProperties) {

        String bizType=request.getParameter("bizType");
        String agentNo=request.getParameter("agentNo");

        //必填参数校验
        ResultVO payConfirmParamVaildResult=QuickpayRequsetValid.payConfirmParamVaild(request);
        if(!ResultEnum.SUCCESS.getCode().equals(payConfirmParamVaildResult.getRetCode())){
            log.info("【必填参数为空】bizType:{}，agentNo：{}，错误信息：{}", bizType, agentNo,payConfirmParamVaildResult.getRetMsg());
            return ResultVOUtil.error(payConfirmParamVaildResult);
        }

        //确认支付
        PayConfirmDto payConfirmDto   = Request2BeanConvert.convertConfirmPay(request);


        TransactionBean  transactionBean = new TransactionBean();
        transactionBean.setAgentId(agentXmlBean.getId());
        transactionBean.setMerchantId(mb.getId());
        transactionBean.setOrderNumber(payConfirmDto.getOrderNumber());
        transactionBean.setPlatformOrderNumber(payConfirmDto.getPlatformOrderNumber());
        log.info(transactionBean.toString());
        transactionBean = transactionDao.getByOrder(agentProperties, transactionBean);
        if(transactionBean==null){
            return ResultVOUtil.error(ResultEnum.ORDER_NOT_EXIST);
        }

        //校验通道是否可用
        ResultVO useCheckResult=channelUseCheckUtil.useCheck(transactionBean.getChannelCode());
        if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
            return useCheckResult;
        }
        ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
        ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
        ResultVO  payConfirmResult= channel.payConfirm(agentProperties, transactionBean,payConfirmDto, response);

        return payConfirmResult;

    }
}

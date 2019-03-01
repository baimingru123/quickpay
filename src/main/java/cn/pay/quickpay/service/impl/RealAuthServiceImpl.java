package cn.pay.quickpay.service.impl;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.AgentBean;
import cn.pay.quickpay.bean.BankCertificationBean;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.channel.base.AuthChannelAbstract;
import cn.pay.quickpay.convert.Request2BeanConvert;
import cn.pay.quickpay.dao.IAgentDao;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.service.RealAuthService;
import cn.pay.quickpay.utils.platform.GetChannelXmlBeanUtil;
import cn.pay.quickpay.utils.platform.ResultVOUtil;
import cn.pay.quickpay.valid.QuickpayRequsetValid;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.AuthChannelXmlBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bmr
 * @time 2019-02-21 16:52
 */
@Service
@Slf4j
public class RealAuthServiceImpl implements RealAuthService {

    @Autowired
    private IAgentDao agentDao;

    @Autowired
    private GetChannelXmlBeanUtil getChannelXmlBeanUtil;

    @Override
    public ResultVO realAuth(HttpServletRequest request,AgentXmlBean agentXmlBean) {
        String bizType=request.getParameter("bizType");
        String agentNo=request.getParameter("agentNo");

        AgentBean agentBean = agentDao.getById(agentXmlBean.getId());
        //判断机构余额大于单笔鉴权金额   -1为小于、0为等于、1为大于
        if(agentBean.getAuthBalance().compareTo(agentBean.getAuthFee())==-1){//
            log.info("【鉴权费用不足】bizType:{}，agentNo：{}",bizType,agentNo);
            return ResultVOUtil.error(ResultEnum.AGENT_AUTH_FEE_INSUFFICIENT);
        }

        //校验必填参数是否填写
        ResultVO realAuthParamVaildResult=QuickpayRequsetValid.realAuthParamVaild(request);
        if(!realAuthParamVaildResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
            return ResultVOUtil.error(realAuthParamVaildResult);
        }


        MerchantBean merchantBean1 = Request2BeanConvert.convertToMerchantBeanRealAuth(request);
        //运营商要做四要素鉴权
        BankCertificationBean bankCertificationBean = new BankCertificationBean();
        bankCertificationBean.setBankCardName(merchantBean1.getSettleName());
        bankCertificationBean.setBankCardNo(merchantBean1.getSettleBankCardNo());
        bankCertificationBean.setPhone(merchantBean1.getSettlePhone());
        bankCertificationBean.setIdCard(merchantBean1.getIdCard());

        String authChannelCode = "yiyuan";
        AuthChannelXmlBean authChannelXmlBean=getChannelXmlBeanUtil.getAuthChannelXml(authChannelCode);
        if (authChannelXmlBean.getIs_open()==0) {
            log.info("bizType:{}，agentNo：{}，错误信息：{}", bizType,agentNo,"通道已关闭");
            return ResultVOUtil.error(ResultEnum.CHANNEL_CANNOT_USE);
        }

        AuthChannelAbstract authChannel = getChannelXmlBeanUtil.getAuthChannelAbstract(authChannelXmlBean.getChannel_sign());
        return authChannel.realAuth(bankCertificationBean,agentBean);
    }
}

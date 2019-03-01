package cn.pay.quickpay.service;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.xmlbean.AgentXmlBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * @author bmr
 * @time 2019-02-22 11:27
 */
public interface OpenCardService {
    ResultVO openCard(HttpServletRequest request, HttpServletResponse response, MerchantBean mb, AgentXmlBean agentXmlBean, Properties agentProperties, Properties platformProperties);

}

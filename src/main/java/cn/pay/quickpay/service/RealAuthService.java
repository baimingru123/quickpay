package cn.pay.quickpay.service;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.xmlbean.AgentXmlBean;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bmr
 * @time 2019-02-21 16:51
 */
public interface RealAuthService {

    ResultVO realAuth(HttpServletRequest request, AgentXmlBean agentXmlBean);
}

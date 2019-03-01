package cn.pay.quickpay.service;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.xmlbean.AgentXmlBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bmr
 * @time 2019-02-22 11:56
 */
public interface AgentService {

    ResultVO getAgentBalance(HttpServletRequest request, HttpServletResponse response, AgentXmlBean agentXmlBean);
}

package cn.pay.quickpay.service;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.xmlbean.AgentXmlBean;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/**
 * @author bmr
 * @time 2019-02-21 10:58
 */
public interface UpdateMerService {

    ResultVO updateMer(HttpServletRequest request,MerchantBean mb,AgentXmlBean agentXmlBean,Properties agentProperties, Properties platformProperties);
}

package cn.pay.quickpay.service;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.xmlbean.AgentXmlBean;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/**
 * @author bmr
 * @time 2019-02-21 16:26
 * 商户入驻service层
 */
public interface AddMerService {
    ResultVO addMer(HttpServletRequest request, MerchantBean mb, AgentXmlBean agentXmlBean, Properties agentProperties, Properties platformProperties);
}

package cn.pay.quickpay.service.impl;

import cn.pay.quickpay.VO.AgentBalanceResultVo;
import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.AgentBean;
import cn.pay.quickpay.dao.IAgentDao;
import cn.pay.quickpay.service.AgentService;
import cn.pay.quickpay.utils.platform.ResultVOUtil;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bmr
 * @time 2019-02-22 11:58
 */
@Slf4j
@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private IAgentDao agentDao;

    @Override
    public ResultVO getAgentBalance(HttpServletRequest request, HttpServletResponse response, AgentXmlBean agentXmlBean) {
        AgentBean agentBean = agentDao.getById(agentXmlBean.getId());
        AgentBalanceResultVo resultVo=new AgentBalanceResultVo(agentBean.getAuthBalance().toString(),agentBean.getBalance().toString());
        return ResultVOUtil.success(resultVo);
    }
}

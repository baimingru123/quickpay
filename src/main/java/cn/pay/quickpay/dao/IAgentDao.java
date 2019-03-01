package cn.pay.quickpay.dao;

import java.util.Properties;

import cn.pay.quickpay.bean.AgentBean;
import org.springframework.stereotype.Component;


/**
 * 
 * 创建日期 2018-2-4 下午1:57:10   
 * @author 闪付时代 zll
 *
 */
public interface IAgentDao {

	/**
	 * 
	 * @param id
	 * @return
	 */
	AgentBean getById(int id);
	
	/**
	 * 鉴权扣费
	 * @param bean
	 * @return
	 */
	int updateAgent(AgentBean bean);
	
	/**
	 * 鉴权扣费
	 * @param bean
	 * @return
	 */
	int updateAuthBalance(AgentBean bean);
	
	/**
	 * 合同扣费
	 * @param bean
	 * @return
	 */
	int updateContractBalance(AgentBean bean);
	
	/**
	 * 代付扣费
	 * @param bean
	 * @return
	 */
	int updateAgentBalance(AgentBean bean);
	
	/**
	 * 代付失败给机构充值
	 * @param bean
	 * @return
	 */
	int updateAgentBalanceAdd(AgentBean bean);
	
}

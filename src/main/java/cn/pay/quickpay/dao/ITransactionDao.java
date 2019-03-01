package cn.pay.quickpay.dao;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.pay.quickpay.bean.TransactionBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


/**
 * 商户交易
 * 创建日期 2018-2-1 上午10:50:33   
 * @author 闪付时代 zll
 *
 */
public interface ITransactionDao {


	/**
	 * 新增交易
	 * @param bean
	 * @return
	 */
	int insertTransaction(Properties agentProperties,@Param("bean") TransactionBean bean);

	/**
	 * 交易记录保存第三方订单号
	 * @param bean
	 * @return
	 */
	int updateTransaction(Properties agentProperties,@Param("bean") TransactionBean bean);

	/**
	 * 第三方异步通知处理
	 * 平台订单号、第三方订单号、交易状态、结算状态、第三方交易状态、第三方结算状态、支付时间（可不填）
	 * @param bean
	 * @return
	 */
	int updateTransactionCallback(Properties agentProperties,@Param("bean") TransactionBean bean);

	/**
	 * 修改订单状态
	 * @param bean
	 * @return
	 */
	int updateTransactionStatus(Properties agentProperties,@Param("bean") TransactionBean bean);

	/**
	 * 修改订单合同签署状态
	 * @param bean
	 * @return
	 */
	int updateContractSignStatus(Properties agentProperties,@Param("bean") TransactionBean bean);

	/**
	 * 根据id查交易记录
	 * @param id
	 * @return
	 */
	TransactionBean getById(Properties properties,@Param("id") int id);

	/**
	 * 根据平台订单号查交易记录
	 * @param platformOrderNumber
	 * @return
	 */
	TransactionBean getByPlatformOrderNumber(Properties properties,@Param("platformOrderNumber") String platformOrderNumber);

	/**
	 * 根据商户订单号查交易记录
	 * @param platformOrderNumber
	 * @return
	 */
	TransactionBean getByOrderNumber(Properties properties,@Param("orderNumber") String orderNumber);

	/**
	 * 根据商户订单号、机构id、商户id、商户订单号、平台订单号（可填） 查询订单
	 * @param agentProperties
	 * @param bean
	 * @return
	 */
	TransactionBean getByOrder(Properties agentProperties,@Param("bean") TransactionBean bean);

	/**
	 * 查询智能代还消费对应的还款单号1对1
	 * @param agentProperties
	 * @param repayIds
	 * @return
	 */
	TransactionBean getByRepayIds(Properties agentProperties,@Param("repayIds") String repayIds);

	/**
	 * 自定义sql查询订单集合
	 * @param agentProperties
	 * @param sql
	 * @return
	 */
	List<TransactionBean> getByPlatformOrderNumberList(Properties agentProperties,@Param("paramSQL")String sql);

	/**
	 * 获取不重复唯一订单号
	 * @param map
	 * @return
	 */
	Map<String, String> getOrderNumber(Map<String, String> map);
}

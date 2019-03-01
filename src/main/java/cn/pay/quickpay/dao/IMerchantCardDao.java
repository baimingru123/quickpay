package cn.pay.quickpay.dao;

import java.util.Map;
import java.util.Properties;

import cn.pay.quickpay.bean.MerchantCardBean;
import org.apache.ibatis.annotations.Param;


/**
 * 商户绑卡
 * 创建日期 2018-1-29 下午5:42:42   
 * @author 闪付时代 zll
 *
 */
public interface IMerchantCardDao {

	/**
	 * 新增绑卡
	 * @param bean
	 * @return
	 */
	int insertMerchantCard(Properties agentProperties, @Param("bean") MerchantCardBean bean);
	
	/**
	 * 卡签约
	 * @param bean
	 * @return
	 */
	int updateMerchantCard(Properties agentProperties, @Param("bean") MerchantCardBean bean);
	
	/**
	 * 更新绑卡状态
	 * @param agentProperties
	 * @param bean
	 * @return
	 */
	int updateBindState(Properties agentProperties, @Param("bean") MerchantCardBean bean);
	
	/**
	 * 证件审核
	 * @param bean
	 * @return
	 */
	int updateAuth(Properties agentProperties, @Param("bean") MerchantCardBean bean);
	
	/**
	 * 修改信用卡基本信息
	 * @param agentProperties
	 * @param bean
	 * @return
	 */
	int updateCardInfo(Properties agentProperties, @Param("bean") MerchantCardBean bean);
	
	/**
	 * 修改信用卡交易费率
	 * @param agentProperties
	 * @param bean
	 * @return
	 */
	int updateRate(Properties agentProperties, @Param("bean") MerchantCardBean bean);
	
	
	/**
	 * 卡解约
	 * @param bean
	 * @return
	 */
	int updateMerchantCardDel(Properties agentProperties, @Param("bean") MerchantCardBean bean);
	
	/**
	 * 修改平台订单号
	 * @param bean
	 * @return
	 */
	int updatePlatformOutTradeNo(Properties agentProperties, @Param("bean") MerchantCardBean bean);
	
	/**
	 * 修改平台绑卡交易号和通道返回的绑卡申请流水号 
	 * @param bean
	 * @return
	 */
	int updatePlatformOutTradeNoAndBindCode(Properties agentProperties, @Param("bean") MerchantCardBean bean);
	
	MerchantCardBean getByInfo(Properties agentProperties, int merchantId, String bankCardNo, int channelId);
	
	/**
	 * 根据平台绑卡交易号查询绑卡信息
	 * @param agentProperties
	 * @param merchantId
	 * @param platformOutTradeNo
	 * @param channelId
	 * @return
	 */
	MerchantCardBean getByPlatformOutTradeNo(Properties agentProperties, int merchantId, String platformOutTradeNo, int channelId);
	
	/**
	 * 根据平台订单号查交易记录
	 * @param platformOrderNumber
	 * @return
	 */
	MerchantCardBean getByPlatformOrderNumber(Properties properties, String platformOrderNumber);
	
	
	/**
	 * 查询代理商分库   根据商户订单号或者平台订单号查询信息 
	 * @param MerchantCardBean
	 * @return
	 */
	MerchantCardBean getByOrderNo(Properties properties, @Param("bean") MerchantCardBean bean);
	
	/**
	 * 获取不重复唯一商户号
	 * @param map
	 * @return
	 */
	Map<String, String> getMerchantCardNumber(Map<String, String> map);
	
}

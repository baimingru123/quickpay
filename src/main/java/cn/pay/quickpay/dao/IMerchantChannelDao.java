package cn.pay.quickpay.dao;

import java.util.List;
import java.util.Properties;

import cn.pay.quickpay.bean.MerchantChannelBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


/**
 * 商户入驻各通道
 * 创建日期 2018-1-29 下午5:42:42   
 * @author 闪付时代 zll
 *
 */
public interface IMerchantChannelDao {

	/**
	 * 新增商户入驻
	 * @param bean
	 * @return
	 */
	int insertMerchantChannel(Properties properties,@Param("bean") MerchantChannelBean bean);

	int updateMerchantChannel(Properties agentProperties,@Param("bean") MerchantChannelBean bean);

	int updateOpenQuickStatus(Properties agentProperties,@Param("bean") MerchantChannelBean bean);

	int updateSettleCardAndBindId(Properties agentProperties,@Param("bean") MerchantChannelBean bean);

	/**
	 * 根据id查询merchantChannel记录
	 * @param id
	 * @return
	 */
	MerchantChannelBean getById(Properties agentProperties,@Param("id") int id);

	/**
	 * 查商户入驻同类型通道信息
	 * @param agentProperties
	 * @param type 类型（0有积分、1无积分、2智能代还、3结算、4升级、5划扣）
	 * @return
	 */
	List<MerchantChannelBean> getByType(Properties agentProperties,@Param("merchantId") int merchantId,@Param("type") String type);
	/**
	 * 查商户入驻各通道信息
	 * @param agentProperties
	 * @param merchantId
	 * @return
	 */
	List<MerchantChannelBean> getByMerchantId(Properties agentProperties,@Param("merchantId") int merchantId);
	/**
	 * 查看商户入驻某个虚拟通道的费率
	 * @param agentProperties
	 * @param merchantId
	 * @param virtualChannelId
	 * @return
	 */
	MerchantChannelBean getByVirtualChannelId(Properties agentProperties,@Param("merchantId") int merchantId,@Param("virtualChannelId") int virtualChannelId);

	/**
	 * 查商户入驻同类型通道信息
	 * @param agentProperties
	 * @param merchantId
	 * @param type 类型（0有积分、1无积分、2智能代还、3结算）
	 * @param channelId
	 * @return
	 */
	MerchantChannelBean getByChannelId(Properties agentProperties,@Param("merchantId") int merchantId,@Param("type") String type,@Param("channelId") int channelId);

	/**
	 * 根据三方商户号和通道id获取MerchantChannelBean
	 * @param agentProperties
	 * @param thirdMerNo 三方商户号
	 * @param channelId 通道id
	 * @return
	 */
	MerchantChannelBean getByChannelIdThirdMerNo(Properties agentProperties,@Param("thirdMerNo") String thirdMerNo,@Param("channelId") int channelId);

	/**
	 * 根据入网时的订单号获取MerchantChannelBean
	 * @param agentProperties
	 * @param ext 订单号
	 * @return
	 */
	MerchantChannelBean getByExt(Properties agentProperties,@Param("ext") String ext);



}

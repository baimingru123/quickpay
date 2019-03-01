package cn.pay.quickpay.dao;

import java.util.Map;
import java.util.Properties;

import cn.pay.quickpay.bean.MerchantBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


/**
 * 商户
 * 创建日期 2018-1-29 下午5:42:42   
 * @author 闪付时代 zll
 *
 */
public interface IMerchantDao {

	/**
	 * 直接查库
	 * @param merNo 机构商户号
	 * @return
	 */
	MerchantBean getMerchantByNo(Properties properties,@Param("merNo") String merNo);

	/**
	 * 直接查库
	 * @param platformMerNo 平台商户号
	 * @return
	 */
	MerchantBean getByPlatformMerNo(Properties properties,@Param("platformMerNo") String platformMerNo);

	/**
	 * 直接查库
	 * @param id
	 * @return
	 */
	MerchantBean getById(Properties properties,@Param("id") int id);

	/**
	 * 新增商户
	 * @param bean
	 * @return
	 */
	int insertMerchant(Properties agentProperties,@Param("bean") MerchantBean bean);

	/**
	 * 修改商户（更新第三方商户号、秘钥）
	 * @param bean
	 * @return
	 */
	int updateMerchant(Properties agentProperties,@Param("bean") MerchantBean bean);

	/**
	 * 修改商户（开通快捷）
	 * @param bean
	 * @return
	 */
	int updateOpenQuick(Properties agentProperties,@Param("bean") MerchantBean bean);

	/**
	 * 修改商户结算卡信息
	 * @param bean
	 * @return
	 */
	int updateSettle(Properties agentProperties,@Param("bean") MerchantBean bean);

	/**
	 * 修改商户费率
	 * @param bean
	 * @return
	 */
	int updateFee(Properties agentProperties,@Param("bean") MerchantBean bean);

	/**
	 * 删除
	 * @param bean
	 * @return
	 */
	int deleteMerchant(Properties agentProperties,@Param("bean") MerchantBean bean);

	/**
	 * 获取不重复唯一商户号
	 * @param map
	 * @return
	 */
	Map<String, String> getMerchantNumber(Map<String, String> map);

}

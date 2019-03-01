package cn.pay.quickpay.dao;

import cn.pay.quickpay.bean.BankCertificationBean;
import org.springframework.stereotype.Component;

/**
 * 
 * 创建日期 2018-2-4 下午1:57:10   
 * @author 闪付时代 zll
 *
 */
public interface IBankCertificationDao {

	/**
	 * 根据卡号查询实名审核信息
	 * @param bankCardNo
	 * @return
	 */
	BankCertificationBean getByBankCardNo(String bankCardNo);
	
	
	/**
	 * 根据四要素查是否鉴过权
	 * 卡号、身份证号、手机号、姓名
	 * @param bean
	 * @return
	 */
	BankCertificationBean getByFourElements(BankCertificationBean bean);
	
	/**
	 * 
	 * @param bean
	 * @return
	 */
	int insertBankCertification(BankCertificationBean bean);
	
}

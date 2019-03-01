package cn.pay.quickpay.dao;

import cn.pay.quickpay.bean.BankCardBinBean;

/**
 * 
 * 创建日期 2018-2-4 下午1:57:02   
 * @author 闪付时代 zll
 *
 */
public interface IBankCardBinDao {

	/**
	 * 查银行简码通过卡号前6位、卡号长度
	 * @param cardBin
	 * @return
	 */
	BankCardBinBean getByCardBin(String cardBin, String cardLen);
	
	
}

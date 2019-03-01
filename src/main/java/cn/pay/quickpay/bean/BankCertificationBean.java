package cn.pay.quickpay.bean;

import lombok.Data;

import java.io.Serializable;
/**
 * 银行卡实名认证
 * 创建日期 2018-3-1 上午11:21:20   
 * @author 闪付时代 zll
 *
 */
@Data
public class BankCertificationBean implements Serializable {

	private static final long serialVersionUID = -443884878445551581L;
	private int id;//
	private String idCard;//身份证号
	private String phone;//手机号
	private String bankCardNo;//银行卡号
	private String bankCardName;//持卡人姓名
	private String province;//省份
	private String city;//城市
	private String bankTel;//银行客服电话
	private String brand;//银行卡产品名称
	private String bankName;//银行名称
	private String cardType;//银行卡种(借记卡、准贷记卡、贷记卡、预付费卡)
	private String url;//银行官网
	private String createTime;//创建日期
	private int authStatus;//鉴权状态  0鉴权不通过 1鉴权通过
	private String authMsg;//鉴权状态描述

	
	
}

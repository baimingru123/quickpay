package cn.pay.quickpay.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Properties;

/**
 * 商户入驻
 * 创建日期 2019-2-15 上午10:28:51
 * @author 闪付时代 bmr
 *
 */
@Data
public class MerchantBean {
	private int agentId;//所属机构
	private int id;//
	private String agentNo;//所属机构号
	private String merNo;//商户号
	private String merName;//商户名称 12
	private String merAddress;//商户地址
	private String settleName;//开户名 16
	private String settlePhone;//手机号
	private String idCard;//身份证号 18
	private String settleBank;//开户银行
	private String settleBankNo;//开户行简码 10
	private String settleBankCardNo;//结算卡号 20
	private String settleBankSub;//开户支行
	private String settleBankBranch;//联行号 12
	private String settleSubProvince;//开户省份 10
	private String settleSubCity;//结算卡开户市 14
	private String platformMerNo;//平台商户号
	private String platformMerKey;//平台商户秘钥
	private String createTime;//创建日期
	private String updateTime;//修改日期
	private String status;//商户状态（0正常，1冻结，2删除）
	private String bankCardPhoto;//银行卡正面照
	private String idCardPhoto;//身份证正面照
	private String idCardBackPhoto;//身份证反面照
	private String personPhoto;//身份证+银行卡+本人合照

	//有积分
	private BigDecimal fee0 ;//有积分交易费率(百分比)
	private int d0fee;//有积分单笔费用（分）

	//无积分
	private BigDecimal noIntegralFee0 ;//无积分交易费率(百分比)
	private int noIntegralFee;//无积分单笔费用（分）

	//协议划扣
	private BigDecimal delimitFee0 ;//划扣交易费率(百分比)
	private int delimitFee;//划扣单笔费用（分）
	private int withholdFee;//代扣单笔费用（分）

	//智能代还
	private BigDecimal repayFee0 ;//智能代还交易费率(百分比)
	private int repayFee;//智能代还单笔费用（分）
	private int repayFee1;//智能代还代还单笔费用（分）

	//结算
	private int balanceFee;//结算单笔手续费(分)

	private String openQuick;//开通快捷（0未开通、1已开通）

	private int settleType;//结算方式（1 T1、2 D0）

	private String idCardExpiryDateStart;//身份证有效期开始日 yyyy-MM-dd
	private String idCardExpiryDateEnd;//身份证有效期结束日 yyyy-MM-dd
	private String industryCode;//行业编码
	private String provinceCode;//省代码
	private String cityCode;//市代码
	private String countyCode;//区县代码
	private String storeFacadePath;//店铺门面照
	private String storeBussinessPlacePath;//商户经营场所或仓库照
	private String electronicSignPath;//电子签名照
	private String bankCode;//银行编码
	private int merchantType;//商户类型（1企业、2个体工商户、3个人）
	private String businessLicenseNo;//营业执照号
	private String businessLicensePath;//营业执照照片路径
	private String businessLicenseEffDate;//营业执照生效日
	private String businessLicenseValDate;//营业执照到期日
	private String establishDate;//成立日期

	//不存库字段

	/** 修改类型：1修改结算卡信息、2修改费率.*/
	private String merFlag;

	/** 是否配置有积分快捷支付通道.*/
	private boolean usableIntegral;

	/** 是否配置无积分快捷支付通道.*/
	private boolean usableNoIntegral;

	/** 是否配置智能代还通道.*/
	private boolean usableRepay;

	/** 是否配置结算通道.*/
	private boolean usableBalance;

	/** 是否配置划扣通道.*/
	private boolean usableDelimit;
	
}

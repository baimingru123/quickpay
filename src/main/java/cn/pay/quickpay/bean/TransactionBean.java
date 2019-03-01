package cn.pay.quickpay.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商户交易
 * 创建日期 2018-1-29 上午10:41:13   
 * @author 闪付时代 zll
 *
 */
@Data
public class TransactionBean {
	private int id;//
	private int merchantId;//商户id
	private String merchantNumber;//商户号
	private String merchantUsername;//商户名
	private int agentId;//机构id
	private String agentNo;//机构号
	private String createTime;//创建日期
	private String payTime;//支付日期
    private String payerName;//支付人姓名
    private String idCardType;//证件类型（0身份证、1其他）
    private String idCardNo;//身份证号码
    private String bankCardNo;//银行卡号
    private String bankCardName;//取款银行
    private String year;//当银行卡是信用卡时必输信用卡有效期年
    private String month;//当银行卡是信用卡时必输信用卡有效期月
    private String cvv2;//当银行卡是信用卡时必输信用卡安全码
    private String phone;//手机号
    private String currency;//币种 暂只支持人民币：CNY
    private String settlementBankCardNo;//结算卡号
	private String settlementBankCardName;//结算银行
	private String settlementType;//结算类型（0当日结算D0、1次日结算T1）
	private int channelId;//通道id
	private String channelCode;//通道号
	private BigDecimal orderAmount;//订单金额，以元为单位，最小金额为0.01
	private BigDecimal arrivalAmount;//到账金额
	private BigDecimal channelCollectionCharges;//通道代付费
	private BigDecimal channelServiceCharge;//通道交易手续费（交易金额*费率）
	private BigDecimal channelTransactionRate;//通道交易费率
	private BigDecimal agencyCollection1;//代理商1代付费
	private BigDecimal agencyService1;//代理商1手续费
	private BigDecimal agencyTransactionRate1;//代理商1费率
	private BigDecimal agencyCollection2;//代理商2代付费
	private BigDecimal agencyService2;//代理商2手续费
	private BigDecimal agencyTransactionRate2;//代理商2手续费率
	private BigDecimal agencyCollection3;//代理商3代付费
	private BigDecimal agencyService3;//代理商3手续费
	private BigDecimal agencyTransactionRate3;//代理商3手续费率
	private BigDecimal agentCollection;//渠道代付费（元）
	private BigDecimal agentService;//渠道手续费（元）（交易金额*费率）
	private BigDecimal agentTransactionRate;//渠道手续费率（百分比）
	private BigDecimal merchantCollection;//商户代付费（元）
	private BigDecimal merchantService;//商户手续费（元）（交易金额*费率）
	private BigDecimal merchantTransactionRate;//商户手续费率（百分比）
	private String orderIp;//用户支付时使用的网络终端 IP
    private int period;//过了订单有效时间的订单会被设置为取消状态不能再重新进行支付
    private String periodUnit;//有效期单位 Day：天、Hour：时、Minute：分
	private String orderNumber;//商户订单号
	private String platformOrderNumber;//平台订单号
	private String platformTransactionNumber;//平台交易号
	private String thirdOrderNumber;//第三方订单号
	private String thirdTransactionNumber;//第三方交易号
	private String thirdStatus;//第三方订单状态
	private String thirdSettleStatus;//第三方结算状态
	private String thirdMsg;//第三方消息
	private String goodsName;//商品名称
    private String goodsDesc;//商品描述
    private String terminalType;//终端类型 IMEI、MAC、UUID（针对 IOS 系统）、OTHER
    private String terminalId;//终端唯一标识，如手机序列号
	private String status;//支付状态(INIT:未支付、SUCCESS：成功、CANCELLED：已取消、REFUNDED：已退款、FAILED：失败、DOING：处理中、CONFIRM：待确认)
	private String notifyUrl;//异步通知地址
	private String returnUrl;//同步通知地址
	private int settleState;//结算状态0失败1成功
	private int isIntegral;//是否有积分（0无积分、1有积分）
	private int type;//交易类型（0快捷消费、1智能代还消费、2智能代还还款、3代付、4升级、5划扣）
	private int settleType;//结算方式(0：直清由我方直接代付出款，直接结算到商户结算卡。,1刷卡金额记入商户余额账户，接入方可主动发起代付。每笔代付都收取提现费,2：刷卡金额记入机构余额账户)
	private String repayIds;//
	private String platformOrderNumberList;//不存库，同名付用
	private int virtualChannelId;//虚拟通道id 不存库
	private int payType;//支付类型 0普通、1支付宝、2微信、3银联全渠道、4翼支付、5协议支付(交易费率+单笔手续费)、6代扣(只含单笔手续费)
	private boolean issms;//是否发送短信、目前用于划扣
	private int isContractSign;//是否需要静默签署合同（0否、1是）
	private int contractSignStatus;//合同签署状态（0未签署或不需要、1已签署、2签署失败）
	
	
	/** 不存库字段*/
	
	/** 是否需要让程序睡眠*/
	private int isSleep;
	

	
	
	
}

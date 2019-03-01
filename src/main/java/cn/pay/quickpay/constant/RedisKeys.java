package cn.pay.quickpay.constant;

public class RedisKeys {
	public static byte[] notifyKey = "orderNotify".getBytes();
	
	/** 支付异步通知Key. */
	public static final String NOTIFYKEY="orderNotifyKey";
	
	/** 支付异步通知6次未收到正确响应的Key. */
	public static final String NONOTIFYKEY="noOrderNotifyKey";
	
	/** 同名付异步通知Key. */
	public static final String PAYFORSAMENAMEKEY="payForSameNameKey";
	
	/** 订单查询Key. */
	public static final String QUERYORDERKEY="queryOrderKey";
	
	/** 合同签署异步通知Key. */
	public static final String CONTRACTNOTIFYKEY="contractNotifyKey";
	
	/** 合同签署异步通知6次未收到正确响应的Key. */
	public static final String NOCONTRACTNOTIFYKEY="noContractNotifyKey";
	
	/** 授权查询异步通知Key. */
	public static final String AUTHNOTIFYKEY="authNotifyKey";
	
	/** 授权查询异步通知6次未收到正确响应的Key. */
	public static final String NOAUTHNOTIFYKEY="noAuthNotifyKey";
	
	
	
	
	
}


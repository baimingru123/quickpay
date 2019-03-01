package cn.pay.quickpay.dto;

import lombok.Data;

/**
 * 支付结果异步通知实体类
 * @author Administrator
 *
 */
@Data
public class NotifyOrderDto {

	private String platformMerNo;//平台商户号
	private String agentNo;//机构号
	private String orderNumber;//订单号
	private String platformOrderNumber;//平台订单号
	private String status;//订单状态 (INIT:未支付、SUCCESS：成功、CANCELLED：已取消、REFUNDED：已退款、FAILED：失败、DOING：处理中)
	private String settleState;//结算状态 （0失败、1成功）
	private String message;//文字说明
	private String sign;//签名
	private int frequency;//推送次数
	private String url;//

	
}

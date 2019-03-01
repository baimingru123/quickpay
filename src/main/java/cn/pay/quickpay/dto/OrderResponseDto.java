package cn.pay.quickpay.dto;

import lombok.Data;

/**
 *
 * 创建日期 2018-1-29 上午10:41:13   
 * @author 闪付时代 zll
 *
 */
@Data
public class OrderResponseDto {

	private String message;
	private String agentNo;//机构号
	private String platformMerNo;//平台商户号
	private String orderNumber;//商户订单号
	private String platformOrderNo;//平台订单号
	private String status;//支付状态(INIT:未支付、SUCCESS：成功、CANCELLED：已取消、REFUNDED：已退款、FAILED：失败、DOING：处理中)
	private int settleState;//结算状态0失败1成功

}

package cn.pay.quickpay.dto;

import lombok.Data;

/**
 * 确认支付dto
 * 创建日期 2018-1-19 上午11:27:51
 * @author 闪付时代 bmr
 *
 */
@Data
public class PayConfirmDto {
	private String channelCode;//通道代码唯一
    private String bizType;//交易类型  QuickPayBankCardPay订单创建、QuickPaySendValidateCode获取短信验证码
    private String orderNumber;//商户系统内部订单号
    private String platformOrderNumber;//平台订单号
    private String timestamp;//时间戳 yyyyMMddHHmmss 14 位数字，精确到秒
    private String validateCode;//短信验证码
    private String orderIp;//支付ip
    
    private String agentNo;//
    private String platformMerNo;//
    


}

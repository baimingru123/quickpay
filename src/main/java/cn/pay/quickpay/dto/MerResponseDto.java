package cn.pay.quickpay.dto;

import lombok.Data;

import java.util.ArrayList;

/**
 * 商户入驻
 * 创建日期 2018-1-29 上午10:41:13   
 * @author 闪付时代 zll
 *
 */
@Data
public class MerResponseDto {

	private Object imgList;//返回信息
	private String platformMerKey;//商户密钥
	private String platformMerNo;//商户号
	private String returnHtml;//返回页面
	private String returnUrl;//返回url
	private String platformOrderNo;//平台订单号
	private String platformCertNumber;//平台合同证书编号
	private String platformOutTradeNo;//平台绑卡交易号
	private String agreementNo;//签约协议号
	private String needSms;//【北京创新支付代还】是否需要验证码本笔订单确认支付时，是否需要短信验证码 0 需要 1 不需要
	private String wxUrl;//微信url
	
	/** 绑卡划扣协议状态*/
	private String status;

}

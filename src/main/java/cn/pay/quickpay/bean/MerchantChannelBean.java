package cn.pay.quickpay.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Properties;

/**
 * 商户入驻各通道信息表
 * 创建日期 2018-1-29 上午10:41:13   
 * @author 闪付时代 zll
 *
 */
@Data
public class MerchantChannelBean {
	private int id;//
	private int merchantId;//所属商户
	private int channelId;//通道id
	private int virtualChannelId;//虚拟通道id
	private String channelCode;//通道代码
	private String type;//类型（0有积分、1无积分、2智能代还、3结算、4升级、5划扣）
	private String thirdMerNo;//第三方商户号
	private String thirdMerKey;//第三方秘钥
	private String createTime;//创建日期
	private String updateTime;//修改日期
	//费率
	private BigDecimal fee0 ;//交易费率(百分比)
	private int fee;//单笔费用（分）
	
	private int repayFee;//代还费用（分）
	
	private int withholdFee;//单笔代扣费，（划扣用）(分)
	
	private int balanceFee;//结算费用（分）
	
	private String openQuick;//开通快捷（0未开通、1已开通）
	
	private String ext;//扩展字段
	
	private String settleBankCardNo;//结算卡号
	





	
}

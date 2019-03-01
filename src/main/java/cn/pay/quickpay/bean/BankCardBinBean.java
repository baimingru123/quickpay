package cn.pay.quickpay.bean;

import lombok.Data;

import java.io.Serializable;
/**
 * 银行代码表（总库）
 * 星洁接口
 * 创建日期 2018-2-4 下午1:34:38   
 * @author 闪付时代 zll
 *
 */
@Data
public class BankCardBinBean implements Serializable {
	

	private static final long serialVersionUID = -443884878445551581L;
	private String cardBin;//卡号初始6位
	private String cardLen;//卡号长度
	private String cardType;//卡类型(借记卡/贷记卡/准贷记卡)
	private String cardName;//卡名
	private String bankTitle;//银行头
	private String bankNo;//银行编号
	private String bankName;//银行名称
	private String pattern;//银行卡号格式
	private int channelId;

	
}

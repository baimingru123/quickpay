package cn.pay.quickpay.xmlbean;

import lombok.Data;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement( name = "item" )
@Data
public class VirtualChannelXmlBean {
	
	private int id;

	private String name;//代称
	
	private int type;//类型,1快捷有积分 2快捷无积分 3代还通道 4结算通道 5升级通道
	
	private int default_channel;//默认通道

	private String code;//通道号
	
	private int agent_id;//机构id
	
	//快捷有积分(代还快捷用有积分)、无积分
	private BigDecimal fee1;//T1费率
	private BigDecimal fee0;//d0费率
	private int dfee;//代付费 分
	private BigDecimal min_money_score;//最低金额限额
	private BigDecimal max_money_score;//最高金额限额
	private BigDecimal min_rate_score0;//D0最低利率范围
	private BigDecimal max_rate_score0;//D0最高费率范围
	private BigDecimal min_rate_score1;//T1最低利率范围
	private BigDecimal max_rate_score1;//T1最高利率范围
	private int min_dfee_score;//最低代付费范围
	private int max_dfee_score;//最高代付费用范围
	
	//代还
	private int repayment_fee;//代还手续费 分
	private int min_repayment_score;//最低代还费用范围
	private int max_repayment_score;//最高代还范围
	
	//风控
	private String min_time_score;//开始时间范围
	private String max_time_score;//结束时间范围
	
	//结算
	private int balance_fee;//结算单笔手续费(分)
	private int min_balance_score;//最低结算费用范围
	private int max_balance_score;//最高结算范围
	
	//划扣代扣
	private int withhold_fee;//单笔代扣费，（划扣用）(分)
	private int min_withhold_score;//最低代扣费用范围（划扣用）
	private int max_withhold_score;//最高代扣费用范围（划扣用）
	
	private String remark;//备注
	
	private String support_bank;//通道支持银行
	private int is_card_sign;//是否需单卡进件(0否1是)
	private int is_contract_sign;//是否静默签署合同（0否、1是）
	
	//不存库字段
	private boolean usableIntegral;//有积分是否可用
	private boolean usableNoIntegral;//无积分是否可用
	private boolean usableDelimit;//划扣是否可用
	private boolean usableRepay;//智能代还是否可用
	private boolean usableBalance;//结算是否可用



}

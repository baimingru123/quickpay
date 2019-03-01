package cn.pay.quickpay.xmlbean;
import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement( name = "channel" )
public class ChannelXmlBean {

	private int id;//
	private String name;//通道名称
	private String other_name;//通道代称
	private String code;//通道方标识
	private String secret_key;//通道方接口密钥
	private int type;//通道类型，1代还+支付、2单支付、3代还
	private int is_integral;// 是否带积分1有积分2无积分3双接口
	
	private int is_open;//是否开启（0关闭、1开启）
	private String channel_sign;//通道标识跟本项目通道配置文件对应
	
	//风控
	private String min_time_score;//开始时间范围0:00-23:59
	private String max_time_score;//结束时间范围0:00-23:59
	
	//封顶
	private int is_cap;//是否有封顶1是2否
	private BigDecimal cap_money;//封顶金额
	
	//快捷交易限额（有积分、无积分公用）
	private BigDecimal min_money_score;//最低金额限额（元）
	private BigDecimal max_money_score;//最高金额限额（元）
	
	//有积分
	private BigDecimal integral_fee1;//积分交易费率（百分比）T1
	private BigDecimal integral_fee0;//积分交易费率（百分比）D0
	private int integral_dfee;//积分单笔费用（分）
	private BigDecimal integral_min_rate_score0;//D0最低利率范围（百分比）
	private BigDecimal integral_max_rate_score0;//D0最高费率范围（百分比）
	private BigDecimal integral_min_rate_score1;//T1最低利率范围（百分比）
	private BigDecimal integral_max_rate_score1;//T1最高利率范围（百分比）
	private int integral_min_dfee_score;//最低代付费范围（分）
	private int integral_max_dfee_score;//最高代付费用范围（分）
	
	//无积分
	private BigDecimal fee0;//无积分费率（百分比）D0
	private BigDecimal fee1;//无积分费率（百分比）T1
	private int dfee;//无积分代付单笔费用（分）
	private BigDecimal min_rate_score0;//D0最低利率范围（百分比）
	private BigDecimal max_rate_score0;//D0最高费率范围（百分比）
	private BigDecimal min_rate_score1;//T1最低利率范围（百分比）
	private BigDecimal max_rate_score1;//T1最高利率范围（百分比）
	private int min_dfee_score;//最低代付费范围（分）
	private int max_dfee_score;//最高代付费用范围（分）
	
	//结算
	private int is_balance;//是否可以结算 1否2是
	private int balance_fee;//结算单笔手续费(分)
	private int min_balance_score;//最低结算费用范围
	private int max_balance_score;//最高结算范围
	
	//代扣
	private int withhold_fee;//单笔代扣费，（划扣用）(分)
	private int min_withhold_score;//最低代扣费用范围（划扣用）
	private int max_withhold_score;//最高代扣费用范围（划扣用）
	
	//代还
	private int repayment_fee;//代还手续费(分)
	private int min_repayment_score;//最低代还费用范围
	private int max_repayment_score;//最高代还范围
	
	private String remark;//备注信息
	
	private String support_bank;//通道支持银行
	
	private String is_card_sign;//是否需单卡进件(0否1是)
	
	private int channel_type;//通道类型（0商旅、1落地、2其他）
	
	private int is_open_deduction_section;//是否开启扣费区间（0否、1是）
	
	private List<ChannelDeductionSectionXmlBean> deductionSectionList;//通道扣费区间
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOther_name() {
		return other_name;
	}
	public void setOther_name(String other_name) {
		this.other_name = other_name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSecret_key() {
		return secret_key;
	}
	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIs_integral() {
		return is_integral;
	}
	public void setIs_integral(int is_integral) {
		this.is_integral = is_integral;
	}
	public int getIs_cap() {
		return is_cap;
	}
	public void setIs_cap(int is_cap) {
		this.is_cap = is_cap;
	}
	public BigDecimal getCap_money() {
		return cap_money;
	}
	public void setCap_money(BigDecimal cap_money) {
		this.cap_money = cap_money;
	}
	public BigDecimal getIntegral_fee1() {
		return integral_fee1;
	}
	public void setIntegral_fee1(BigDecimal integral_fee1) {
		this.integral_fee1 = integral_fee1;
	}
	public BigDecimal getIntegral_fee0() {
		return integral_fee0;
	}
	public void setIntegral_fee0(BigDecimal integral_fee0) {
		this.integral_fee0 = integral_fee0;
	}
	public int getIntegral_dfee() {
		return integral_dfee;
	}
	public void setIntegral_dfee(int integral_dfee) {
		this.integral_dfee = integral_dfee;
	}
	public int getIs_open() {
		return is_open;
	}
	public void setIs_open(int is_open) {
		this.is_open = is_open;
	}
	public BigDecimal getFee0() {
		return fee0;
	}
	public void setFee0(BigDecimal fee0) {
		this.fee0 = fee0;
	}
	public BigDecimal getFee1() {
		return fee1;
	}
	public void setFee1(BigDecimal fee1) {
		this.fee1 = fee1;
	}
	public int getDfee() {
		return dfee;
	}
	public void setDfee(int dfee) {
		this.dfee = dfee;
	}
	public String getChannel_sign() {
		return channel_sign;
	}
	public void setChannel_sign(String channel_sign) {
		this.channel_sign = channel_sign;
	}
	public String getMin_time_score() {
		return min_time_score;
	}
	public void setMin_time_score(String min_time_score) {
		this.min_time_score = min_time_score;
	}
	public String getMax_time_score() {
		return max_time_score;
	}
	public void setMax_time_score(String max_time_score) {
		this.max_time_score = max_time_score;
	}
	public BigDecimal getIntegral_min_rate_score0() {
		return integral_min_rate_score0;
	}
	public void setIntegral_min_rate_score0(BigDecimal integral_min_rate_score0) {
		this.integral_min_rate_score0 = integral_min_rate_score0;
	}
	public BigDecimal getIntegral_max_rate_score0() {
		return integral_max_rate_score0;
	}
	public void setIntegral_max_rate_score0(BigDecimal integral_max_rate_score0) {
		this.integral_max_rate_score0 = integral_max_rate_score0;
	}
	public BigDecimal getIntegral_min_rate_score1() {
		return integral_min_rate_score1;
	}
	public void setIntegral_min_rate_score1(BigDecimal integral_min_rate_score1) {
		this.integral_min_rate_score1 = integral_min_rate_score1;
	}
	public BigDecimal getIntegral_max_rate_score1() {
		return integral_max_rate_score1;
	}
	public void setIntegral_max_rate_score1(BigDecimal integral_max_rate_score1) {
		this.integral_max_rate_score1 = integral_max_rate_score1;
	}
	public int getIntegral_min_dfee_score() {
		return integral_min_dfee_score;
	}
	public void setIntegral_min_dfee_score(int integral_min_dfee_score) {
		this.integral_min_dfee_score = integral_min_dfee_score;
	}
	public int getIntegral_max_dfee_score() {
		return integral_max_dfee_score;
	}
	public void setIntegral_max_dfee_score(int integral_max_dfee_score) {
		this.integral_max_dfee_score = integral_max_dfee_score;
	}
	public BigDecimal getMin_money_score() {
		return min_money_score;
	}
	public void setMin_money_score(BigDecimal min_money_score) {
		this.min_money_score = min_money_score;
	}
	public BigDecimal getMax_money_score() {
		return max_money_score;
	}
	public void setMax_money_score(BigDecimal max_money_score) {
		this.max_money_score = max_money_score;
	}
	public BigDecimal getMin_rate_score0() {
		return min_rate_score0;
	}
	public void setMin_rate_score0(BigDecimal min_rate_score0) {
		this.min_rate_score0 = min_rate_score0;
	}
	public BigDecimal getMax_rate_score0() {
		return max_rate_score0;
	}
	public void setMax_rate_score0(BigDecimal max_rate_score0) {
		this.max_rate_score0 = max_rate_score0;
	}
	public BigDecimal getMin_rate_score1() {
		return min_rate_score1;
	}
	public void setMin_rate_score1(BigDecimal min_rate_score1) {
		this.min_rate_score1 = min_rate_score1;
	}
	public BigDecimal getMax_rate_score1() {
		return max_rate_score1;
	}
	public void setMax_rate_score1(BigDecimal max_rate_score1) {
		this.max_rate_score1 = max_rate_score1;
	}
	public int getMin_dfee_score() {
		return min_dfee_score;
	}
	public void setMin_dfee_score(int min_dfee_score) {
		this.min_dfee_score = min_dfee_score;
	}
	public int getMax_dfee_score() {
		return max_dfee_score;
	}
	public void setMax_dfee_score(int max_dfee_score) {
		this.max_dfee_score = max_dfee_score;
	}
	public int getIs_balance() {
		return is_balance;
	}
	public void setIs_balance(int is_balance) {
		this.is_balance = is_balance;
	}
	public int getBalance_fee() {
		return balance_fee;
	}
	public void setBalance_fee(int balance_fee) {
		this.balance_fee = balance_fee;
	}
	public int getMin_balance_score() {
		return min_balance_score;
	}
	public void setMin_balance_score(int min_balance_score) {
		this.min_balance_score = min_balance_score;
	}
	public int getRepayment_fee() {
		return repayment_fee;
	}
	public void setRepayment_fee(int repayment_fee) {
		this.repayment_fee = repayment_fee;
	}
	public int getMin_repayment_score() {
		return min_repayment_score;
	}
	public void setMin_repayment_score(int min_repayment_score) {
		this.min_repayment_score = min_repayment_score;
	}
	public int getMax_repayment_score() {
		return max_repayment_score;
	}
	public void setMax_repayment_score(int max_repayment_score) {
		this.max_repayment_score = max_repayment_score;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getMax_balance_score() {
		return max_balance_score;
	}
	public void setMax_balance_score(int max_balance_score) {
		this.max_balance_score = max_balance_score;
	}
	public String getSupport_bank() {
		return support_bank;
	}
	public void setSupport_bank(String support_bank) {
		this.support_bank = support_bank;
	}
	public String getIs_card_sign() {
		return is_card_sign;
	}
	public void setIs_card_sign(String is_card_sign) {
		this.is_card_sign = is_card_sign;
	}
	public int getChannel_type() {
		return channel_type;
	}
	public void setChannel_type(int channel_type) {
		this.channel_type = channel_type;
	}
	public int getWithhold_fee() {
		return withhold_fee;
	}
	public void setWithhold_fee(int withhold_fee) {
		this.withhold_fee = withhold_fee;
	}
	public int getMin_withhold_score() {
		return min_withhold_score;
	}
	public void setMin_withhold_score(int min_withhold_score) {
		this.min_withhold_score = min_withhold_score;
	}
	public int getMax_withhold_score() {
		return max_withhold_score;
	}
	public void setMax_withhold_score(int max_withhold_score) {
		this.max_withhold_score = max_withhold_score;
	}
	public int getIs_open_deduction_section() {
		return is_open_deduction_section;
	}
	public void setIs_open_deduction_section(int is_open_deduction_section) {
		this.is_open_deduction_section = is_open_deduction_section;
	}
	@XmlElementWrapper(name = "channel_deduction_section") 
	@XmlElement(name = "item")
	public List<ChannelDeductionSectionXmlBean> getDeductionSectionList() {
		return deductionSectionList;
	}
	public void setDeductionSectionList(
			List<ChannelDeductionSectionXmlBean> deductionSectionList) {
		this.deductionSectionList = deductionSectionList;
	}
	@Override
	public String toString() {
		return "ChannelXmlBean [id=" + id + ", name=" + name + ", other_name="
				+ other_name + ", code=" + code + ", secret_key=" + secret_key
				+ ", type=" + type + ", is_integral=" + is_integral
				+ ", is_open=" + is_open + ", channel_sign=" + channel_sign
				+ ", min_time_score=" + min_time_score + ", max_time_score="
				+ max_time_score + ", is_cap=" + is_cap + ", cap_money="
				+ cap_money + ", min_money_score=" + min_money_score
				+ ", max_money_score=" + max_money_score + ", integral_fee1="
				+ integral_fee1 + ", integral_fee0=" + integral_fee0
				+ ", integral_dfee=" + integral_dfee
				+ ", integral_min_rate_score0=" + integral_min_rate_score0
				+ ", integral_max_rate_score0=" + integral_max_rate_score0
				+ ", integral_min_rate_score1=" + integral_min_rate_score1
				+ ", integral_max_rate_score1=" + integral_max_rate_score1
				+ ", integral_min_dfee_score=" + integral_min_dfee_score
				+ ", integral_max_dfee_score=" + integral_max_dfee_score
				+ ", fee0=" + fee0 + ", fee1=" + fee1 + ", dfee=" + dfee
				+ ", min_rate_score0=" + min_rate_score0 + ", max_rate_score0="
				+ max_rate_score0 + ", min_rate_score1=" + min_rate_score1
				+ ", max_rate_score1=" + max_rate_score1 + ", min_dfee_score="
				+ min_dfee_score + ", max_dfee_score=" + max_dfee_score
				+ ", is_balance=" + is_balance + ", balance_fee=" + balance_fee
				+ ", min_balance_score=" + min_balance_score
				+ ", max_balance_score=" + max_balance_score
				+ ", withhold_fee=" + withhold_fee + ", min_withhold_score="
				+ min_withhold_score + ", max_withhold_score="
				+ max_withhold_score + ", repayment_fee=" + repayment_fee
				+ ", min_repayment_score=" + min_repayment_score
				+ ", max_repayment_score=" + max_repayment_score + ", remark="
				+ remark + ", support_bank=" + support_bank + ", is_card_sign="
				+ is_card_sign + ", channel_type=" + channel_type
				+ ", is_open_deduction_section=" + is_open_deduction_section
				+ ", deductionSectionList=" + deductionSectionList + "]";
	}
	
	
}

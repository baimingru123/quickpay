package cn.pay.quickpay.xmlbean;

import lombok.Data;

import java.math.BigDecimal;


/**
 * 通道扣费区间
 * 创建日期 2018-10-25 下午3:48:25   
 * @author 闪付时代 zll
 *
 */
@Data
public class ChannelDeductionSectionXmlBean {
	
	private int id;
	private int channel_id;//通道id
	private int type;//通道类型（1划扣）
	private int pay_type;//支付类型（1代扣、2协议支付）
	private BigDecimal price_start;//交易金额起始值（大于等于）
	private BigDecimal price_end;//交易金额结束值（小于）
	private BigDecimal cost_price;//通道成本


}

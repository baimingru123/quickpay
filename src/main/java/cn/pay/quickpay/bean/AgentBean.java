package cn.pay.quickpay.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgentBean {
	private int id;
	private String agentName;//机构名称
	private String agentNo;//机构号
	private BigDecimal authBalance;//鉴权余额（元）
	private BigDecimal authFee;//鉴权单笔费用（元）
	private BigDecimal contractBalance;//合同签署余额（元）
	private BigDecimal contractFee;//合同签署单笔费用（元）
	private BigDecimal balance;//机构代付余额（元）
	
	
	//不存库字段
	/** 大数据单笔鉴权费用*/
	private BigDecimal orderAmount;
	
	/** 扣费关联的平台订单号*/
	private String platformOrderNo;
	

	
	
}

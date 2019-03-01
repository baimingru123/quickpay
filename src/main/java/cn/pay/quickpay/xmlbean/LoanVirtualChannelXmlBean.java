package cn.pay.quickpay.xmlbean;


import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement( name = "item" )
@Data
public class LoanVirtualChannelXmlBean {
	
	private int id;

	private int agent_id;//

	private int loan_channel_id;//

	private String name;//

	private String code;//

	private int is_open;//是否开启（0关闭、1开启）


}

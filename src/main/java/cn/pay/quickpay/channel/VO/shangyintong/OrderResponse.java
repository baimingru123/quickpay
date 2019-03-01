package cn.pay.quickpay.channel.VO.shangyintong;

import lombok.Data;

@Data
public class OrderResponse {
	private String r1_merchantNo;//
	private String r2_orderNumber;//
	private String r3_amount;//
	private String r4_bankId;//
	private String r5_business;//
	private String r6_createDate;//
	private String r8_orderStatus;//
	private String r9_withdrawStatus;//
	private String retCode;//
	private String retMsg;//
	private String sign;//
	private String trxType;//

}

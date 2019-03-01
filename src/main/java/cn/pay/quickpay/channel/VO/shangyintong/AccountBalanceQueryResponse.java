package cn.pay.quickpay.channel.VO.shangyintong;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountBalanceQueryResponse {
	private String retCode;//
	private String retMsg;//
	private String agentNo;//
	private BigDecimal balance;//账户余额
	private BigDecimal canbanlance;//可提现余额

	
}

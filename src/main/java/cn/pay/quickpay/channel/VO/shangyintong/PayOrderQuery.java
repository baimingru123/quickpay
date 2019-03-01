package cn.pay.quickpay.channel.VO.shangyintong;

import lombok.Data;

@Data
public class PayOrderQuery {
	private String retCode;//
	private String retMsg;//
	private String withDrawCode;//代付状态（SUCCESS 成功、FAILED 失败、WAITING 处理中、INIT 初始化）
	private String withDrawMsg;//代付返回信息


}

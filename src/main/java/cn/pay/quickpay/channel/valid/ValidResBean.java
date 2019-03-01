package cn.pay.quickpay.channel.valid;
/**
 * @author bmr
 * 创建时间：2019-1-14 下午4:19:51
 * 通道前置校验返回实体类
 */

public class ValidResBean {
	
	private String code;
	
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ValidResBean [code=" + code + ", message=" + message + "]";
	}
	
	

}

package cn.pay.quickpay.xmlbean;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement( name = "channel" )
public class AuthChannelXmlBean {

	private int id;//
	private String name;//通道名称
	private String channel_sign;//通道标识跟本项目通道配置文件对应
	private String code;//通道方标识
	private String secret_key;//通道方接口密钥
	private int is_open;//是否开启（0关闭、1开启）
	private List<AuthProductXmlBean> product_list;//可用鉴权产品列表
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
	public String getChannel_sign() {
		return channel_sign;
	}
	public void setChannel_sign(String channel_sign) {
		this.channel_sign = channel_sign;
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
	public int getIs_open() {
		return is_open;
	}
	public void setIs_open(int is_open) {
		this.is_open = is_open;
	}
	@XmlElementWrapper(name = "product_list") 
	@XmlElement(name = "item")
	public List<AuthProductXmlBean> getProduct_list() {
		return product_list;
	}
	public void setProduct_list(List<AuthProductXmlBean> product_list) {
		this.product_list = product_list;
	}
	@Override
	public String toString() {
		return "AuthChannelXmlBean [id=" + id + ", name=" + name
				+ ", channel_sign=" + channel_sign + ", code=" + code
				+ ", secret_key=" + secret_key + ", is_open=" + is_open
				+ ", product_list=" + product_list + "]";
	}

	
}

package cn.pay.quickpay.xmlbean;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 鉴权产品对应xml文件的实体类
 * @author bmr
 * time： 20181107 10:40:51
 */
@XmlRootElement( name = "item" )
public class AuthProductXmlBean {
	
	private int id;
	
	/** 产品名称.*/
	private String name;
	
	/** 查询单价（元/次）.*/
	private BigDecimal price;
	
	/** 数据来源.*/
	private String source;
	
	/** 产品标识.*/
	private String identify;
	
	/** 该产品在product_item表中是否存在分项(0 不存在分项  1存在分项).*/
	private int is_item;
	
	/** 输入条件.*/
	private String input_condition;
	
	/** 输出结果.*/
	private String output_result;
	
	/** 所属鉴权通道id.*/
	private int auth_channel_id;
	
	/** 该产品下的分项鉴权产品.*/
	private List<AuthProductItemXmlBean> product_item_list;
	
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getIdentify() {
		return identify;
	}
	public void setIdentify(String identify) {
		this.identify = identify;
	}
	
	public int getIs_item() {
		return is_item;
	}
	public void setIs_item(int is_item) {
		this.is_item = is_item;
	}
	public String getInput_condition() {
		return input_condition;
	}
	public void setInput_condition(String input_condition) {
		this.input_condition = input_condition;
	}
	public String getOutput_result() {
		return output_result;
	}
	public void setOutput_result(String output_result) {
		this.output_result = output_result;
	}
	public int getAuth_channel_id() {
		return auth_channel_id;
	}
	public void setAuth_channel_id(int auth_channel_id) {
		this.auth_channel_id = auth_channel_id;
	}
	
	@XmlElementWrapper(name = "product_item_list") 
	@XmlElement(name = "item")
	public List<AuthProductItemXmlBean> getProduct_item_list() {
		return product_item_list;
	}
	public void setProduct_item_list(List<AuthProductItemXmlBean> product_item_list) {
		this.product_item_list = product_item_list;
	}
	@Override
	public String toString() {
		return "AuthProductXmlBean [id=" + id + ", name=" + name + ", price="
				+ price + ", source=" + source + ", identify=" + identify
				+ ", is_item=" + is_item + ", input_condition="
				+ input_condition + ", output_result=" + output_result
				+ ", auth_channel_id=" + auth_channel_id
				+ ", product_item_list=" + product_item_list + "]";
	}
	
	

}

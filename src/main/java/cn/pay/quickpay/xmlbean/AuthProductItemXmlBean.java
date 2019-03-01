package cn.pay.quickpay.xmlbean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 鉴权分项产品对应xml文件的实体类
 * @author bmr
 * time： 20181107 10:53:51
 */
@XmlRootElement( name = "item" )
@Data
public class AuthProductItemXmlBean {
	
	private int id;
	
	/** 所属产品id.*/
	private int auth_product_id;
	
	/** 分项产品标识.*/
	private String identify;
	
	/** 分项产品名称.*/
	private String name;
	
	/** 分项产品单次查询价格(元/次).*/
	private BigDecimal price;
	
	/** 分项产品来源.*/
	private String source;
	
	/** 备注.*/
	private String remark;

	
	

	
}

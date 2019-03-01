package cn.pay.quickpay.xmlbean;

import lombok.Data;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 鉴权产品xml对象的实体类
 * @author Administrator
 *
 */
@XmlRootElement( name = "item" )
@Data
public class AuthChannelProductOfAgentXmlBean {

	private int id;
	private String code;//通道号
	private int agent_id;//代理商id
	private int auth_channel_id;//鉴权通道id
	private int auth_product_id;//鉴权通道中的产品id
	private BigDecimal price;//产品单价
	private String name;//对外产品名称
	private String product_name;//产品名称
	private String identify;//产品标识

	
}

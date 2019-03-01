package cn.pay.quickpay.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Properties;


/**
 * 商户绑卡列表
 * 创建日期 2018-01-29 10:41:51   
 * @author 闪付时代 zll
 *
 */
@Data
public class MerchantCardBean {
	
	private int id;
	
	/** 商户id. */
	private int merchantId;
	
	/** 所属通道id. */
	private int channelId;
	
	/** 所属通道银行支持列表中的id. */
	private int bankCodeId;
	
	/** 机构id. */
	private int agentId;
	
	/** 机构号码. */
	private String agentNo;
	
	/** 创建日期. */
	private String createTime;
	
	/** 身份证号码. */
    private String idCardNo;
    
    /** 银行卡卡号. */
    private String bankCardNo;
    
    /** 信用卡安全码(选填项). */
    private String cvv2;
    
    /** 信用卡有效期年(选填项). */
    private String year;
    
    /** 信用卡有效期月(选填项). */
    private String month;
    
    /** 银行预留手机号. */
    private String phone;
    
    /** 持卡人姓名. */
    private String fullName;
    
    /** 绑定状态（0未绑定、1已绑定、2已解绑、3绑卡失败、4绑卡审核中、5冻结）. */
    private String bindState;
    
    /** 三方返回商户号. */
    private String thridMerId;
    
    /** 三方返回商户秘钥. */
    private String thridMerKey;
    
    /** 三方返回绑定状态描述. */
    private String remark;
    
    /** 三方返回绑卡协议号. */
    private String agreementNo;
    
    /** 下游上送绑卡交易号. */
    private String outTradeNo;
    
    /** 平台绑卡交易号. */
    private String platformOutTradeNo;
    
    /** 身份证正面照片. */
    private String idcardImg;
    
    /** 身份证反面照片. */
    private String idcardReverseImg;
    
    /** 本人手持身份证照片. */
    private String handIdcard;
    
    /** 身份证审核（0未绑定、1已绑定、2处理中）. */
    private String authState;
    
    
    
    //不存库字段
    
    /** 平台商户号. */
    private String platformMerNo;
    
    /** 短信验证码. */
    private String smsCode;
    
    /** 虚拟通道id. */
    private int virtualChannelId;

    /** 交易费率(百分比). */
  	private BigDecimal fee0 ;
  	
  	/** 交易单笔手续费（分）. */
  	private int fee;
  	
  	/** 代还费用（分）. */
  	private int repayFee;
  	
  	/** 同步页面回调地址. */
  	private String returnUrl;
  	
  	/** 绑卡类型 （1划扣绑卡、0普通绑卡）. */	
  	private int type;
	
	
	
}

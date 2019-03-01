package cn.pay.quickpay.channel.base;



import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.AgentBean;
import cn.pay.quickpay.bean.BankCertificationBean;

/**
 * 各类鉴权
 * 创建日期 2018-7-19 下午6:41:29   
 * @author 闪付时代 zll
 *
 */
public abstract class AuthChannelAbstract {

	// 接口地址
	private String channelAddress;
	private String notifyAddress;//
	// 用户名
	private String username;
	// 密码
	private String password;
	// 通道代码（唯一）
	private String channelCode;
	// 服务器异步通知地址
	private String serverCallbackUrl;
	// RSA公钥
	private String merchantPublicKey;
	
	private String publicKey;//公钥
	private String privateKey;//私钥
	
	//授权成功同步跳转servlet地址
	private String authBackUrl;
	//授权成功同步跳转页面
	private String authSuccessUrl;
	//授权失败同步跳转页面
	private String authFailUrl;
	//资讯云请求地址
	private String zixunChannelAddress;
	//资讯云商户秘钥
	private String zixunVerifyKey;



	/**
	 * 身份证号、姓名、银行卡号、预留手机号四要素核验
	 * @param bankCertificationBean 身份证号、姓名核验实体
	 * @param agentBean 代理商
	 * @return
	 */

	public ResultVO realAuth(BankCertificationBean bankCertificationBean, AgentBean agentBean){
		return null;
	}


	


}

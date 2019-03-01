package cn.pay.quickpay.channel.base;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.bean.MerchantCardBean;
import cn.pay.quickpay.bean.MerchantChannelBean;
import cn.pay.quickpay.bean.TransactionBean;
import cn.pay.quickpay.channel.quick.ShanghaiYitongBCChannel;
import cn.pay.quickpay.config.CoreConfig;
import cn.pay.quickpay.dto.PayConfirmDto;
import cn.pay.quickpay.service.impl.QuickpayServiceImpl;
import cn.pay.quickpay.utils.platform.GetChannelXmlBeanUtil;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import cn.pay.quickpay.xmlbean.VirtualChannelXmlBean;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/**
 * 通道抽象接口 
 * 创建日期 2018-1-18 上午9:54:51
 * @author 闪付时代 zll
 * 
 */
@Data
public abstract class ChannelAbstract {

	@Autowired
	public CoreConfig config;


	/** 通道接口请求地址.*/
	private String channelAddress;
	
	/** 异步通知地址.*/
	private String notifyAddress;//

	/** 用户名.*/
	private String username;

	/** 密码.*/
	private String password;
 
	/** 通道代码标识（唯一）.*/
	private String channelCode;
 
	/** 服务器异步通知地址.*/
	private String serverCallbackUrl;
	
	/** RSA公钥.*/
	private String merchantPublicKey;
	
	/** 公钥.*/
	private String publicKey;
	
	/** 私钥.*/
	private String privateKey;




	/**
	 * 商户多通道进件
	 * @param agentXmlBean
	 * @param bean
	 * @param merchantChannelBean
	 * @param platformProperties
	 * @param channelXmlBean
	 * @return
	 */
	public ResultVO addMemchantChannel(AgentXmlBean agentXmlBean, MerchantBean bean, MerchantChannelBean merchantChannelBean, Properties platformProperties, Properties agentProperties, ChannelXmlBean channelXmlBean){
		return null;

	}

	/**
	 * 商户结算卡和费率修改
	 * @param bean
	 * @param merchantChannelBean
	 * @param channelXmlBean
	 * @param agentXmlBean
	 * @param agentProperties
	 * @param platformProperties
	 * @return
	 */
	public ResultVO updateMemChannel(MerchantBean bean, MerchantChannelBean merchantChannelBean,ChannelXmlBean channelXmlBean,AgentXmlBean agentXmlBean,Properties agentProperties,Properties platformProperties){
		return null;
	}

	/**
	 * 快捷支付预下单
	 * @param bean
	 * @param merchantChannelBean
	 * @param resp
	 * @param channelXmlBean
	 * @param agentXmlBean
	 * @param merchantBean
	 * @param agentProperties
	 * @param virtualChannelXmlBean
	 * @return
	 */
	public ResultVO addOrder(TransactionBean bean,MerchantChannelBean merchantChannelBean, HttpServletResponse resp,ChannelXmlBean channelXmlBean,AgentXmlBean agentXmlBean,MerchantBean merchantBean,Properties agentProperties,VirtualChannelXmlBean virtualChannelXmlBean){
		return null;
	}

	/**
	 * 确认支付
	 * @param agentProperties
	 * @param transactionBean
	 * @param payConfirmDto
	 * @param response
	 * @return
	 */
	public ResultVO payConfirm(Properties agentProperties, TransactionBean transactionBean, PayConfirmDto payConfirmDto, HttpServletResponse response) {
		return null;
	}

	/**
	 * 订单查询
	 * @param bean
	 * @param agentProperties
	 * @param response
	 * @return
	 */
	public ResultVO queryOrder(TransactionBean bean,Properties agentProperties,HttpServletResponse response){
		return null;
	}

	/**
	 * 代付
	 * @return
	 */
	public ResultVO payForAnotherOne(TransactionBean bean, HttpServletResponse resp, ChannelXmlBean channelXmlBean, AgentXmlBean agentXmlBean, MerchantBean merchantBean, Properties agentProperties, MerchantChannelBean merchantChannelBean, VirtualChannelXmlBean virtualChannelXmlBean){
		return null;
	}

	/**
	 * 签约绑卡短信
	 * @return
	 */
	public ResultVO openCardCode(Properties agentProperties,MerchantCardBean bean,MerchantChannelBean merchantChannelBean,MerchantBean merchantBean,AgentXmlBean agentXmlBean,  HttpServletResponse resp){
		return null;
	}

	/**
	 * 签约绑卡确认
	 * @return
	 */
	public ResultVO openCard(Properties agentProperties,MerchantCardBean bean,MerchantChannelBean merchantChannelBean,MerchantBean merchantBean,  HttpServletResponse resp){
		return null;
	}

	
	public String balance(MerchantChannelBean merchantChannelBean){
		return "";
	}

	
	/**
	 * 同名付
	 * @return
	 */
	public String payForSameName(TransactionBean bean,HttpServletResponse resp,ChannelXmlBean channelXmlBean,AgentXmlBean agentXmlBean,MerchantBean merchantBean,Properties agentProperties){
		return "";
	}
	
	/**
	 * 同名付查询
	 * @return
	 */
	public String payForSameNameQuery(Properties agentProperties,TransactionBean bean){
		return "";
	}
	
	/**
	 * 发送短信
	 * @param bean
	 * @return
	 */
	public String sendSms(TransactionBean bean){
		return "";
	}
	

	

	
	/**
	 * 快捷
	 * 商户绑卡成功后发起下单操作
	 * @param bean
	 * @param resp
	 * @param channelBean
	 * @param agentBean
	 * @param merchantBean
	 * @return
	 */
	public String addOrderAfterBindCard(TransactionBean transactionBean,MerchantChannelBean merchantChannelBean,HttpServletResponse resp,ChannelXmlBean channelXmlBean,AgentXmlBean agentXmlBean,Properties agentProperties){
		return "";
	}
	
	/**
	 * 发起退款
	 * @param bean
	 * @param agentXmlBean
	 * @param merchantBean
	 * @param agentProperties
	 * @return
	 */
	public String refundOrder(TransactionBean bean,AgentXmlBean agentXmlBean,MerchantBean merchantBean,Properties agentProperties){
		return "";
	}
	

	
	
	
	/**
	 * 开通快捷
	 * 柜银
	 * @return
	 */
	public String openQuick(MerchantBean bean, MerchantChannelBean merchantChannelBean,HttpServletResponse resp,AgentXmlBean agentXmlBean,Properties agentProperties,Properties platformProperties){
		return "";
	}
	
	/**
	 * 身份证审核
	 * @param bean
	 * @param merchantChannelBean
	 * @param resp
	 * @param agentXmlBean
	 * @param agentProperties
	 * @param platformProperties
	 * @return
	 */
	public String openAuth(Properties agentProperties, MerchantCardBean bean, MerchantChannelBean merchantChannelBean, MerchantBean merchantBean, HttpServletResponse resp){
		return "";
	}
	

	
	/**
	 * 绑卡状态查询
	 * @return
	 */
	public String queryTreatyInfo(Properties agentProperties,MerchantCardBean bean, HttpServletResponse resp){
		return "";
	}
	
	/**
	 * 绑卡协议解除
	 * @return
	 */
	public String cancelTreatyInfo(Properties agentProperties,MerchantCardBean bean, HttpServletResponse resp){
		return "";
	}
	
	/**
	 * 签约绑卡短信发送
	 * @return
	 */
	public String openCardSms(Properties agentProperties,MerchantCardBean bean,MerchantChannelBean merchantChannelBean,MerchantBean merchantBean,  HttpServletResponse resp){
		return "";
	}
	
	
	/**
	 * 签约绑卡解绑
	 * @return
	 */
	public String delCard(Properties agentProperties,MerchantCardBean bean,MerchantChannelBean merchantChannelBean,MerchantBean merchantBean, HttpServletResponse resp){
		return "";
	}


	
//	/**
//	 * ①①银行卡支付下单
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String createOrder(CreateOrderBean bean, HttpServletResponse resp) {
//		return "";
//	}
//
//	/**
//	 * ①②发送支付验证码
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String sendValidateCode(SendValidateCodeBean bean, HttpServletResponse resp) {
//		return "";
//	}
//


//
//	/**
//	 * ②①鉴权绑卡短信
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String bindCardValidateCode(BindCardValidateCodeBean bean, HttpServletResponse resp) {
//		return "";
//	}
//
//	/**
//	 * ②②鉴权绑卡
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String bindCard(BindCardBean bean, HttpServletResponse resp) {
//		return "";
//	}
//
//	/**
//	 * ②③绑卡支付短信
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String bindPayValidateCode(BindPayValidateCodeBean bean, HttpServletResponse resp) {
//		return "";
//	}
//
//	/**
//	 * ②④绑卡支付
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String bindPay(BindPayBean bean, HttpServletResponse resp){
//		return "";
//	}
//
	/**
	 * ③①异步通知
	 * @param resp
	 * @return
	 */
	public String callbackOrder(TransactionBean transactionBean,AgentXmlBean agentXmlBean,HttpServletRequest req, HttpServletResponse resp,Properties agentProperties){
		return "";
	}
//
//	/**
//	 * 绑卡异步通知
//	 * @param merchantCardBean
//	 * @param agentXmlBean
//	 * @param req
//	 * @param resp
//	 * @param agentProperties
//	 * @return
//	 */
//	public String callbackTied(MerchantCardBean merchantCardBean,AgentXmlBean agentXmlBean,HttpServletRequest req, HttpServletResponse resp,Properties agentProperties){
//		return "";
//	}
//
//	/**
//	 * 同步通知
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String returnOrder(TransactionBean transactionBean,CallbackOrderVo vo,HttpServletRequest req, HttpServletResponse resp,Properties agentProperties){
//		return "";
//	}
//
//	/**
//	 * ④①订单查询
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String queryOrder(QueryOrderBean bean, HttpServletResponse resp){
//		return "";
//	}
//
//	/**
//	 * ⑤①绑结算卡
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String settlementCardBind(SettlementCardBindBean bean, HttpServletResponse resp){
//		return "";
//	}
//
//	/**
//	 * ⑤②结算卡提现
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String settlementCardWithdrawBean(SettlementCardWithdrawBean bean, HttpServletResponse resp){
//		return "";
//	}
//
//	/**
//	 * ⑤③结算卡查询
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String transferQuery(TransferQueryBean bean, HttpServletResponse resp){
//		return "";
//	}
//
//	/**
//	 * ⑤④账户余额查询接口
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String accountQuery(AccountQueryBean bean, HttpServletResponse resp){
//		return "";
//	}
//
//	/**
//	 * ⑤⑤用户结算卡查询
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String settlementCardQuery(SettlementCardQueryBean bean, HttpServletResponse resp){
//		return "";
//	}
//
//	/**
//	 * ⑤⑥银行卡解绑
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String bankCardUnbind(BankCardUnbindBean bean, HttpServletResponse resp){
//		return "";
//	}
//	/**
//	 * ⑤⑦用户绑定银行卡信息查询（仅限于交易卡）
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String bankCardbindList(BankCardbindListBean bean, HttpServletResponse resp){
//		return "";
//	}
//	/**
//	 * ⑥①商户提现
//	 * 这个不是给客户开发的，是平台用的
//	 * @param bean
//	 * @param resp
//	 * @return
//	 */
//	public String merchantWithdraw(MerchantWithdrawBean bean, HttpServletResponse resp){
//		return "";
//	}



	public static void main(String[] args) {
//		String[] coreXml = new String[] { "file:"  + "./channel/quick/71.xml" };
//		FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(coreXml);
//		ctx.registerShutdownHook();
//		ChannelAbstract channel = (ChannelAbstract)ctx.getBean("channel");

//		ChannelAbstract channelAbstract=new ShanghaiYitongBCChannel();

//		QuickpayServiceImpl quickpayService=new QuickpayServiceImpl();

	}
}

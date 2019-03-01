package cn.pay.quickpay.channel.quick;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.*;
import cn.pay.quickpay.channel.base.ChannelAbstract;
import cn.pay.quickpay.channel.notify.Notify;
import cn.pay.quickpay.channel.util.yitong.ResultConvertUtil;
import cn.pay.quickpay.channel.valid.ChannelValid;
import cn.pay.quickpay.config.CoreConfig;
import cn.pay.quickpay.dao.IAgentDao;
import cn.pay.quickpay.dao.IBankCardBinDao;
import cn.pay.quickpay.dao.ITransactionDao;
import cn.pay.quickpay.dto.OrderResponseDto;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.utils.platform.*;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import cn.pay.quickpay.xmlbean.VirtualChannelXmlBean;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 上海亿通银联扫码BC通道
 * 创建日期 2019-01-14 下午15:41:51   
 * @author 闪付时代 bmr
 *
 */

public class ShanghaiYitongBCChannel extends ChannelAbstract {

	private CoreConfig config;

	private ITransactionDao transactionDao;

	private IBankCardBinDao bankCardBinDao;

	private IAgentDao agentDao;

	private static Logger logger = LoggerFactory.getLogger(ShanghaiYitongBCChannel.class);
	private String channelCommonAddress;
	private String channelSettleAddress;
	private String merNo;
	private String privateKeyPath;
	private String publicKeyPath;	
	private String channelCode;
	private String payNotifyAddress;
	private String settleNotifyAddress;


	
	
	@Override
	public ResultVO addMemchantChannel(AgentXmlBean agentXmlBean, MerchantBean bean,
									   MerchantChannelBean merchantChannelBean,
									   Properties platformProperties, Properties agentProperties, ChannelXmlBean channelXmlBean) {
		//该通道既能进行交易，也可以进行结算，故进行无积分入驻前置校验和结算入驻前置校验
		
//		System.out.println("merchantChannelBean:"+merchantChannelBean);
		if("1".equals(merchantChannelBean.getType())){
			//无积分入驻前置校验
			ResultVO noIntegralValidResBean = ChannelValid.noIntegralChannelEnterCheck(merchantChannelBean, channelXmlBean);
			if (!"0000".equals(noIntegralValidResBean.getRetCode())) {
				return noIntegralValidResBean;
			}
		}
		
		if("3".equals(merchantChannelBean.getType())){
			//结算入驻前置校验
			ResultVO settleValidResBean = ChannelValid.settleChannelEnterCheck(merchantChannelBean, channelXmlBean);
			if (!"0000".equals(settleValidResBean.getRetCode())) {
				return settleValidResBean;
			}
		}
		
			
		merchantChannelBean.setThirdMerNo("");
		merchantChannelBean.setThirdMerKey("");
		logger.info("上海亿通银联扫码BC通道进件"+merchantChannelBean.toString());
		return ResultVOUtil.success(merchantChannelBean);
	}
	
	@Override
	public ResultVO updateMemChannel(MerchantBean bean, MerchantChannelBean merchantChannelBean,
			ChannelXmlBean channelXmlBean, AgentXmlBean agentXmlBean,
			Properties agentProperties, Properties platformProperties) {
		// 该通道既能进行交易，也可以进行结算，故进行无积分入驻前置校验和结算入驻前置校验
		if("1".equals(merchantChannelBean.getType())){
			//无积分入驻前置校验
			ResultVO noIntegralValidResBean = ChannelValid.noIntegralChannelEnterCheck(merchantChannelBean, channelXmlBean);
			if (!"0000".equals(noIntegralValidResBean.getRetCode())) {
				return noIntegralValidResBean;
			}
		}
		
		if("3".equals(merchantChannelBean.getType())){
			//结算入驻前置校验
			ResultVO settleValidResBean = ChannelValid.settleChannelEnterCheck(merchantChannelBean, channelXmlBean);
			if (!"0000".equals(settleValidResBean.getRetCode())) {
				return settleValidResBean;
			}
		}

		logger.info("上海亿通银联扫码BC通道修改进件"+merchantChannelBean.toString());
		return ResultVOUtil.success();
		
	}
	
	@Override
	public ResultVO addOrder(TransactionBean bean,
			MerchantChannelBean merchantChannelBean, HttpServletResponse resp,
			ChannelXmlBean channelXmlBean, AgentXmlBean agentXmlBean,
			MerchantBean merchantBean, Properties agentProperties,VirtualChannelXmlBean virtualChannelXmlBean) {

		Map<String,Object> resMap=new HashMap<>();
		
		//进行通道交易的前置校验
		ResultVO validResBean=ChannelValid.quickPayChannelTransactionCheck(bean, virtualChannelXmlBean,channelXmlBean);
		if(!"0000".equals(validResBean.getRetCode())){
			return validResBean;
		}

//		System.out.println("config:"+config);
		Map<String, Object> map = new HashMap<>();
		Map<String, String> resultMap = new HashMap<>();
		String orderAmount=String.valueOf((bean.getOrderAmount().multiply(new BigDecimal(100))).intValue());
		map.put("requestNo", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));//请求流水号
		map.put("version", "V1.0");//版本号
		map.put("productId", "1006");//产品类型  固定值1006
		map.put("transId", "20");//交易类型  固定值20
		map.put("merNo", merNo);//商户号
		map.put("orderDate", String.valueOf(System.currentTimeMillis() / 1000));//订单日期  时间戳
		map.put("orderNo", bean.getPlatformOrderNumber());//商户订单号
		map.put("notifyUrl", config.getNotifyHostUrl()+payNotifyAddress);//异步通知地址
		map.put("transAmt", orderAmount);//订单金额（单位为分）
		map.put("goodsName", "华为手机");//商品名称
		map.put("remark", "华为手机购买");//商品描述
		map.put("buyerName", bean.getPayerName());//买家姓名
		map.put("signature", SignUtil.getShanghaiYitongSign(map,privateKeyPath));

		Gson gson=new Gson();
		//以post+表单提交的方式进行发送
		logger.info("上海亿通银联扫码BC通道支付,请求地址:{},提交参数:{}",channelCommonAddress+"do",map);
		 String result =HttpsRequest.doPost(channelCommonAddress+"do", map);
//		 上海亿通银联扫码BC通道支付返回参数:buyerName=柏明儒&goodsName=华为手机&merNo=8504388758324&notifyUrl=http://azhe2p.natappfree.cc/shanghaiYitongBCPayNotify&orderDate=1547518145&orderNo=202019011510090600001&productId=1006&remark=华为手机购买&requestNo=20190115100905234&respCode=0003&respDesc=产品未开通&transAmt=2000&transId=20&version=V1.0
		 logger.info("上海亿通银联扫码BC通道支付返回参数:{}",result);
		 if("".equals(result)||result==null){
			 bean.setStatus("INIT");
			 transactionDao.insertTransaction(agentProperties,bean);
			 return ResultVOUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),"通道方未响应");
		 }

		 resultMap=ResultConvertUtil.convert2map(result);
		 String resCode=resultMap.get("respCode");
		 if("0000".equals(resCode)){

				bean.setStatus("INIT");
				int count = transactionDao.insertTransaction(agentProperties,bean);
				if(count>0){
					resMap.put("platformOrderNo",bean.getPlatformOrderNumber());
					resMap.put("returnUrl",resultMap.get("codeUrl"));
					return ResultVOUtil.success(resMap);
				}else{
					logger.info("【上海亿通银联扫码BC通道支付】订单存库失败");
				}
		}else{
			 return ResultVOUtil.error(ResultEnum.TRANS_FAIL.getCode(),"商户订单提交失败:"+resultMap.get("respDesc"));
		 }

		return ResultVOUtil.success(resMap);
	}
	

	public ResultVO queryOrder(TransactionBean bean, Properties agentProperties,HttpServletResponse resp) {
		OrderResponseDto resBean = new OrderResponseDto();
		//逻辑处理：接入方先查询平台系统，如果平台系统中状态已经是终态，则不去通道方查。如果平台未获取到终态，则调用通道方查询接口
		bean = transactionDao.getByOrder(agentProperties,bean);
		if(bean!=null){

				resBean.setAgentNo(bean.getAgentNo());
				resBean.setOrderNumber(bean.getOrderNumber());
				resBean.setPlatformMerNo(bean.getMerchantNumber());
				resBean.setPlatformOrderNo(bean.getPlatformOrderNumber());
				resBean.setStatus(bean.getStatus());
				resBean.setSettleState(bean.getSettleState());
				
				String queryOrderType="";
				if(bean.getType()==0){
					queryOrderType="上海亿通BC扫码交易";
				}else if(bean.getType()==3){
					queryOrderType="上海亿通BC扫码代付";
				}
		
				if((!"SUCCESS".equals(bean.getStatus().toUpperCase())) || (!"FAILED".equals(bean.getStatus().toUpperCase()))){
				
					Map<String, Object> map = new HashMap<String, Object>();
					Map<String, String> resultMap = new HashMap<String, String>();
					
					if(bean.getType()==0){//快捷交易订单查询
						map.put("version", "V1.1");//版本号
						map.put("productId", "3001");//产品类型  固定值1006
						map.put("transId", "30");//交易类型  固定值20
						map.put("merNo", merNo);//商户号
						map.put("orderNo", bean.getPlatformOrderNumber());//订单号
						map.put("signature", SignUtil.getShanghaiYitongSign(map,privateKeyPath));
						logger.info("上海亿通银联扫码BC通道交易订单查询,请求地址:{},提交参数:{}",channelCommonAddress+"order",map);
						 String result =HttpsRequest.doPost(channelCommonAddress+"order", map);
						 logger.info("上海亿通银联扫码BC通道交易订单查询返回参数:{}",result);
						 if("".equals(result)||result==null){
							 resBean.setStatus(bean.getStatus());
							 return ResultVOUtil.success(resBean);
						 }	
						 
						 resultMap=ResultConvertUtil.convert2map(result);
						 String resCode=resultMap.get("respCode");
						 String orderInfo= (String) resultMap.get("orderInfo");
						 JSONObject orderInfoJson=JSON.parseObject(orderInfo);
						 String respCode=orderInfoJson.getString("respCode");
						 String respDesc=orderInfoJson.getString("respDesc");
						 
						 if("0000".equals(resCode)){
							//respCode:  0000:成功   p000:交易处理中  其他失败
							 if("0000".equals(respCode)){
								 resBean.setMessage(respDesc);
								 bean.setStatus("SUCCESS");
								 bean.setThirdMsg(respDesc);
								int count = transactionDao.updateTransactionStatus(agentProperties,bean);
								if(count>0){
										resBean.setStatus(bean.getStatus());
								}	
								//异步通知
								Notify.orderNotify(bean,agentProperties,queryOrderType);
							 }else if("p000".equals(respCode)){
								 resBean.setMessage(respDesc);
								 bean.setStatus("DOING");
								 bean.setThirdMsg(respDesc);
								 int count = transactionDao.updateTransactionStatus(agentProperties, bean);
								 if(count>0){
										resBean.setStatus(bean.getStatus());
								 }
							 }else{
								 resBean.setMessage(respDesc);
								 bean.setStatus("FAILED");
								 bean.setThirdMsg(respDesc);
								 int count = transactionDao.updateTransactionStatus(agentProperties, bean);
								 if(count>0){
										resBean.setStatus(bean.getStatus());
								 } 
								//异步通知
									Notify.orderNotify(bean,agentProperties,queryOrderType);
							 }
							 	
						 }
						 
						
					}else if(bean.getType()==3){//代付订单查询
						map.put("version", "V1.1");//版本号
						map.put("productId", "3003");//产品类型  固定值1006
						map.put("transId", "30");//交易类型  固定值20
						map.put("merNo", merNo);//商户号
						map.put("orderNo", bean.getPlatformOrderNumber());//订单号
						map.put("signature", SignUtil.getShanghaiYitongSign(map,privateKeyPath));
						logger.info("上海亿通银联扫码BC通道代付订单查询,请求地址:{},提交参数:{}",channelCommonAddress+"settlement",map);
						 String result =HttpsRequest.doPost(channelCommonAddress+"settlement", map);
						 logger.info("上海亿通银联扫码BC通道代付订单查询返回参数:{}",result);
//						 orderInfo={"customerName":"柏明儒","acctNo":"6214835410389207","orderDate":"1547546521","transAmt":"1300.00","version":"V1.0","productId":"0203","bankCode":"招商银行","orderNo":"202019011518020100001","requestNo":"20190115180201936","transId":"26","isCompay":"0","account":"22","notifyUrl":"https:\/\/payapi.36pay.cc\/shanghaiYitongBCSettleNotify","respCode":"0001","respDesc":"用户已申请，交易处理中"}&productId=3003&respCode=0000&respDesc=查询成功&signature=j9p6hJy/RIX1/T6znF%2BIGog5KJo94L%2BLrPmoPwYJ7SirvjhfRz/f5XS2Bg3ZJ%2BHaplKKRemyAFL2nWuKZvjIbmLxAYAgQGmfmIS2ja3AhYeis0ChMbuYpnmR5xILioJR2C6FLnpsiQarGwrBCBnMRInWgtT4ZFoj7w%2BquJIv5g8=&transId=30&version=V1.1
						 if("".equals(result)||result==null){
							 resBean.setStatus(bean.getStatus());
							 return ResultVOUtil.success(resBean);
						 }	
						 
						 resultMap=ResultConvertUtil.convert2map(result);
						 System.out.println("resultMap:"+resultMap);
						 String resCode=resultMap.get("respCode");
						 String orderInfo= (String) resultMap.get("orderInfo");
						 JSONObject orderInfoJson=JSON.parseObject(orderInfo);
						 String respCode=orderInfoJson.getString("respCode");
						 String respDesc=orderInfoJson.getString("respDesc");
						 //respCode:   0000:成功  0001:处理中  0002：弃单  其他失败
						 if("0000".equals(resCode)){
							 if("0000".equals(respCode)){
								 resBean.setMessage(respDesc);
								 bean.setStatus("SUCCESS");
								 bean.setThirdMsg(respDesc);
								 int count = transactionDao.updateTransactionStatus(PropertiesUtil.readProperties("jdbc.properties"),bean);
								 if(count>0){
										 resBean.setStatus(bean.getStatus());
								 }	
//								 	//异步通知
									Notify.orderNotify(bean,agentProperties,queryOrderType);
							 }else if("0001".equals(respCode)){
								 resBean.setMessage(respDesc);
								 bean.setStatus("DOING");
								 bean.setThirdMsg(respDesc);
								 int count = transactionDao.updateTransactionStatus(PropertiesUtil.readProperties("jdbc.properties"),bean);
								 if(count>0){
										resBean.setStatus(bean.getStatus());
								 }
							 }else{
								 resBean.setMessage(respDesc);
								 bean.setStatus("FAILED");
								 bean.setThirdMsg(respDesc);
								 int count = transactionDao.updateTransactionStatus(PropertiesUtil.readProperties("jdbc.properties"),bean);
								 if(count>0){
										resBean.setStatus(bean.getStatus());
								 } 
//								 	//异步通知
									Notify.orderNotify(bean,agentProperties,queryOrderType);
							 }
						 }
						
					}
					
					
				
				}else{
					//平台库中已经是终态了，对下游进行异步通知
					Notify.orderNotify(bean,agentProperties,queryOrderType);
			}
		
		}else{
			logger.info("【订单查询】订单不存在");
			return ResultVOUtil.error(ResultEnum.ORDER_NOT_EXIST);
		}
		return ResultVOUtil.success(resBean);
	}
	
	
	@Override
	public String callbackOrder(TransactionBean transactionBean,AgentXmlBean agentXmlBean,HttpServletRequest req, HttpServletResponse resp,Properties agentProperties) {
			int count = transactionDao.updateTransactionCallback(PropertiesUtil.readProperties("jdbc.properties"),transactionBean);
    		if(count>0){
    			//调起第三方回调地址
				Notify.orderNotify(transactionBean, agentProperties,"上海亿通银联扫码BC通道");
    		}
		   	return "";    	
	}
	
	
	@Override
	public ResultVO payForAnotherOne(TransactionBean bean,
			HttpServletResponse resp, ChannelXmlBean channelXmlBean,
			AgentXmlBean agentXmlBean, MerchantBean merchantBean,
			Properties agentProperties, MerchantChannelBean merchantChannelBean,VirtualChannelXmlBean virtualChannelXmlBean) {

		logger.info("【上海亿通代付】订单明细:{}",bean);
//		MerResponseBean resBean = new MerResponseBean();
		Map<String,Object> resMap=new HashMap<>();

		//进行通道交易的前置校验
		ResultVO validResBean=ChannelValid.settlementChannelTransactionCheck(bean, channelXmlBean, virtualChannelXmlBean, agentXmlBean);
		if(!ResultEnum.SUCCESS.getCode().equals(validResBean.getRetCode())){
			return validResBean;
		}



		Map<String, Object> map = new HashMap<String, Object>();
		ResultVO resultVO=new ResultVO();
		Map<String, String> resultMap = new HashMap<String, String>();
		//因通道方代付扣除的是平台的余额，所以这里需要将交易金额减去手续费，再发送给通道方
		String orderAmount=String.valueOf(bean.getArrivalAmount().multiply(new BigDecimal(100)).intValue());
		BankCardBinBean bankCardbinBean = bankCardBinDao.getByCardBin(bean.getBankCardNo().substring(0, 6),
				bean.getBankCardNo().length()+ "");
		map.put("requestNo", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));//请求流水号
		map.put("version", "V1.0");//版本号
		map.put("productId", "0203");//产品类型  固定值0203
		map.put("transId", "26");//交易类型  固定值20
		map.put("merNo", merNo);//商户号
		map.put("orderDate", String.valueOf(System.currentTimeMillis() / 1000));//订单日期  时间戳
		map.put("orderNo", bean.getPlatformOrderNumber());//商户订单号
		map.put("notifyUrl", config.getNotifyHostUrl()+settleNotifyAddress);//异步通知地址
		map.put("transAmt", orderAmount);//订单金额（单位为分）
		map.put("isCompay", "0");//对公对私标识 0(0为对私，目前代付交易尚不对公）
		map.put("customerName", bean.getPayerName());//代付账户名
		map.put("bankCode", bankCardbinBean.getBankName());//银行名称
		map.put("acctNo", bean.getBankCardNo());//代付账号
		map.put("account", "22");//代付通道编号
		map.put("signature", SignUtil.getShanghaiYitongSign(map,privateKeyPath));

//		Gson gson=new Gson();
		//以post+表单提交的方式进行发送
		logger.info("上海亿通银联扫码BC通道代付,请求地址:{},提交参数:{}",channelSettleAddress,map);
		 String result =HttpsRequest.doPost(channelSettleAddress, map);
//		 上海亿通银联扫码BC通道代付返回参数:account=22&acctNo=6214835410389207&bankCode=招商银行&customerName=柏明儒&isCompay=0&merNo=8504388758324&notifyUrl=http://dghv65.natappfree.cc/shanghaiYitongBCSettleNotify&orderDate=1547540901&orderNo=202019011516282100001&productId=0203&requestNo=20190115162821521&respCode=0000&respDesc=提现成功&transAmt=1100&transId=26&version=V1.0&signature=LE0fPDBZ2%2BR1gESJ/IOhRVFvo/pvNdh7M2gIyGKvCvyrFYGc3iudKE%2BCceRUwutqvUVF3hdu0DtmtEp3GmqaCEaiKDJBBGNkbUcJswaN/WYF2ot%2Bn8jQk8AF2%2BE%2BFFJEESrGYjca1WPqI6a1jo602T91WunDQ2IUbK5OL6WjN8I=
		 logger.info("上海亿通银联扫码BC通道代付返回参数:{}",result);
		 if("".equals(result)||result==null){
			 bean.setStatus("INIT");
			 transactionDao.insertTransaction(agentProperties,bean);
			 return ResultVOUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),"通道方未响应");
		 }

		 if(result.contains("html")){
			 //设置订单状态
			 bean.setStatus("DOING");
			 resultVO.setRetCode("0000");
			 resultVO.setRetMsg("订单处理中");

		 }else{
			 resultMap=ResultConvertUtil.convert2map(result);
			 String resCode=resultMap.get("respCode");
			 String resMsg=resultMap.get("respDesc");
			//状态：0000成功 其他失败
			if("0000".equals(resCode) ){
				//设置订单状态
				bean.setStatus("SUCCESS");
				resultVO.setRetCode("0000");
				resultVO.setRetMsg("订单提交成功");
			}else{
				//设置订单状态
				bean.setStatus("FAILED");
				//存储失败原因
				bean.setThirdMsg(resMsg);
				resultVO.setRetCode(ResultEnum.TRANS_FAIL.getCode());
				resultVO.setRetMsg("代付失败："+resMsg);
			}
		 }


		//除取消、退款、失败状态的订单外，其余状态先扣机构费用
		if (!"CANCELLED".equals(bean.getStatus())&& !"REFUNDED".equals(bean.getStatus())
				&& !"FAILED".equals(bean.getStatus())) {
			AgentBean ab = new AgentBean();
			ab.setId(agentXmlBean.getId());
			ab.setOrderAmount(bean.getArrivalAmount().add(
					bean.getAgentCollection()));
			int count1 = agentDao.updateAgentBalance(ab);
			if (count1 == 0) {
				logger.info("【上海亿通BC代付】代付扣机构费用失败,机构号：{}：订单明细：{}, 代理商明细:{}",
						agentXmlBean.getAgent_no(), bean.toString(),
						ab.toString());

			}
		}

		// 往数据库中保存该笔订单信息
		int id = transactionDao.insertTransaction(agentProperties, bean);
		if (id > 0) {
			if ("FAILED".equals(bean.getStatus())) {
				ResultVOUtil.error(ResultEnum.TRANS_FAIL.getCode(),"代付失败："+bean.getThirdMsg());
			} else {
				// 进行异步通知
				Notify.orderNotify(bean, agentProperties, "上海亿通BC代付");
				resMap.put("platformOrderNo",bean.getPlatformOrderNumber());
				resultVO.setData(resMap);

			}

		} else {
			logger.info("【上海亿通BC代付】订单存库失败,机构号：{}：订单明细：{}",
					agentXmlBean.getAgent_no(), bean.toString());
		}

		return resultVO;
	}


	public String getChannelCommonAddress() {
		return channelCommonAddress;
	}

	public void setChannelCommonAddress(String channelCommonAddress) {
		this.channelCommonAddress = channelCommonAddress;
	}

	public String getChannelSettleAddress() {
		return channelSettleAddress;
	}

	public void setChannelSettleAddress(String channelSettleAddress) {
		this.channelSettleAddress = channelSettleAddress;
	}

	public String getMerNo() {
		return merNo;
	}

	public void setMerNo(String merNo) {
		this.merNo = merNo;
	}

	public String getPrivateKeyPath() {
		return privateKeyPath;
	}

	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}

	public String getPublicKeyPath() {
		return publicKeyPath;
	}

	public void setPublicKeyPath(String publicKeyPath) {
		this.publicKeyPath = publicKeyPath;
	}

	@Override
	public String getChannelCode() {
		return channelCode;
	}

	@Override
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getPayNotifyAddress() {
		return payNotifyAddress;
	}

	public void setPayNotifyAddress(String payNotifyAddress) {
		this.payNotifyAddress = payNotifyAddress;
	}

	public String getSettleNotifyAddress() {
		return settleNotifyAddress;
	}

	public void setSettleNotifyAddress(String settleNotifyAddress) {
		this.settleNotifyAddress = settleNotifyAddress;
	}

	public ShanghaiYitongBCChannel() {
		this.config=ContextUtil.getBean("config",CoreConfig.class);
		this.transactionDao=ContextUtil.getBean("transactionDaoImpl",ITransactionDao.class);
		this.bankCardBinDao=ContextUtil.getBean("bankCardbinDaoImpl",IBankCardBinDao.class);
		this.agentDao=ContextUtil.getBean("agentDaoImpl",IAgentDao.class);
	}
}

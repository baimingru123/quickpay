package cn.pay.quickpay.channel.notify;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.bean.TransactionBean;
import cn.pay.quickpay.channel.base.ChannelAbstract;
import cn.pay.quickpay.constant.RedisKeys;
import cn.pay.quickpay.dao.IMerchantDao;
import cn.pay.quickpay.dto.NotifyOrderDto;
import cn.pay.quickpay.utils.platform.*;
import cn.pay.quickpay.utils.platform.security.Disguiser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import org.springframework.beans.factory.annotation.Autowired;

public class Notify extends ChannelAbstract {

	static IMerchantDao merchantDao=ContextUtil.getBean("merchantDaoImpl",IMerchantDao.class);

	private static Logger logger = LoggerFactory.getLogger(Notify.class);
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");



	/**
	 * 支付订单   发起异步通知
	 * @param bean
	 * @param agentProperties
	 * @param isSleep 是否让程序休眠
	 * @return
	 */
	public static String orderNotify(TransactionBean bean,Properties agentProperties,String msg){
		if(bean.getIsSleep()==1){
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String res = "";
		//调起第三方回调地址
		if(bean.getNotifyUrl()!=null&&!"".equals(bean.getNotifyUrl())){
			MerchantBean merchantBean = merchantDao.getById(agentProperties,bean.getMerchantId());
			//机构号、平台订单号、商户号、订单状态、结算状态
			Hashtable<String, String> paraMap = new Hashtable<>();
			paraMap.put("platformMerNo", merchantBean.getPlatformMerNo());
			paraMap.put("agentNo", bean.getAgentNo());
			paraMap.put("orderNumber", bean.getOrderNumber());
			paraMap.put("platformOrderNumber", bean.getPlatformOrderNumber());
			paraMap.put("status", bean.getStatus().toUpperCase());
			paraMap.put("settleState", bean.getSettleState()+"");
			paraMap.put("message", bean.getThirdMsg()!=null?bean.getThirdMsg():"");
			
			String parStr1 = SignUtil.formatUrlMap(paraMap, false, false,true);//所有参数除sign进行ASCII 排序
			String sign1 = Disguiser.disguiseMD5(parStr1+"&key="+merchantBean.getPlatformMerKey()).toUpperCase();//md5 转大写
			long execTime=0;
			paraMap.put("sign", sign1);
			String response1= HttpUtil.doPost(bean.getNotifyUrl(), paraMap);
			logger.info("推送地址：{},通知结果:{}",bean.getNotifyUrl(),response1);
			if(response1!=null&&response1.contains("success")){//"success".equals(response1)
				//需要了解一下异步通知发送几次，支付状态和结算状态代表的含义、数据库要加异步通知是否成功的标识吧
				//异步回调可不可以挂参，挂的参数参与签名吗
				//t0费率
				//要不要加结算字段
				//异步通知通过id取渠道配置文件
				logger.info(msg+"异步通知成功反馈渠道:{}",bean.getPlatformOrderNumber());
				res = "成功"+response1;
			}else{
				NotifyOrderDto notifyOrderBean = new NotifyOrderDto();
				notifyOrderBean.setAgentNo(paraMap.get("agentNo"));
				notifyOrderBean.setPlatformMerNo(paraMap.get("platformMerNo"));
				notifyOrderBean.setOrderNumber(paraMap.get("orderNumber"));
				notifyOrderBean.setPlatformOrderNumber(paraMap.get("platformOrderNumber"));
				notifyOrderBean.setStatus(paraMap.get("status"));
				notifyOrderBean.setSettleState(paraMap.get("settleState"));
				notifyOrderBean.setMessage(paraMap.get("message"));
				notifyOrderBean.setFrequency(1);
				notifyOrderBean.setSign(paraMap.get("sign"));
				notifyOrderBean.setUrl(bean.getNotifyUrl());
				String nextPushTime=sdf.format(DateUtils.getDateByMinuteAdd(new Date(),3));
				JedisUtil.lpush((RedisKeys.NOTIFYKEY+nextPushTime).getBytes(), JSON.toJSON(notifyOrderBean).toString().getBytes());
				logger.info(msg+"异步通知失败反馈渠道:{}",bean.getPlatformOrderNumber());
				res = "失败"+response1;
			}
			
		}else{
			logger.info("该订单不需要异步通知，商户交易未填异步通知地址:{}",bean.getPlatformOrderNumber());
			res = "该订单不需要异步通知，商户交易未填异步通知地址";
		}
		return res;
	}
	
	/**
	 * 签订电子合同   发起异步通知
	 * @param bean
	 * @param agentProperties
	 * @return
	 */
//	public static String contractNotify(ContractSignBean bean,Properties agentProperties,AgentXmlBean agentXmlBean,String msg){
//		String res = "";
//		//调起第三方回调地址
//		if(bean.getNotifyUrl()!=null&&!"".equals(bean.getNotifyUrl())){
//			//机构号、平台订单号、商户号、订单状态、结算状态
//			Hashtable<String, String> paraMap = new Hashtable<>();
//			paraMap.put("agentNo", bean.getAgentNo());
//			paraMap.put("certNumber", bean.getCertNumber());
//			paraMap.put("contractNumber", bean.getContractNumber());
//			paraMap.put("status", bean.getStatus().toUpperCase());
//			paraMap.put("message", bean.getThirdMsg()!=null?bean.getThirdMsg():"");
//
//			String parStr1 = Sign.formatUrlMap(paraMap, false, false,true);//所有参数除sign进行ASCII 排序
//			String sign1 = Disguiser.disguiseMD5(parStr1+"&key="+agentXmlBean.getSecret_key()).toUpperCase();//md5 转大写
//			long execTime=0;
//			paraMap.put("sign", sign1);
//
//
//			String response1 = response1 = HttpUtil.doPost(bean.getNotifyUrl(), paraMap);
//			logger.info("推送地址：{},通知结果:{}",bean.getNotifyUrl(),response1);
//			if(response1!=null&&response1.contains("success")){//"success".equals(response1)
//
//				//需要了解一下异步通知发送几次，支付状态和结算状态代表的含义、数据库要加异步通知是否成功的标识吧
//				//异步回调可不可以挂参，挂的参数参与签名吗
//				//t0费率
//				//要不要加结算字段
//				//异步通知通过id取渠道配置文件
//				logger.info(msg+"异步通知成功反馈渠道:{},{},{}",bean.getCertNumber(),bean.getContractNumber(),agentXmlBean.getAgent_no());
//				res = "成功"+response1;
//			}else{
//				NotifyContractBean notifyContractBean = new NotifyContractBean();
//				notifyContractBean.setAgentNo(paraMap.get("agentNo"));
//				notifyContractBean.setCertNumber(paraMap.get("certNumber"));
//				notifyContractBean.setContractNumber(paraMap.get("contractNumber"));
//				notifyContractBean.setStatus(paraMap.get("status"));
//				notifyContractBean.setMessage(paraMap.get("message"));
//				notifyContractBean.setFrequency(1);
//				notifyContractBean.setSign(paraMap.get("sign"));
//				notifyContractBean.setUrl(bean.getNotifyUrl());
//				String nextPushTime=sdf.format(DateUtils.getDateByMinuteAdd(new Date(),3));
//				JedisUtil.lpush((RedisKeys.CONTRACTNOTIFYKEY+nextPushTime).getBytes(), JSON.toJSON(notifyContractBean).toString().getBytes());
//				logger.info(msg+"异步通知失败反馈渠道:{},{},{}",bean.getCertNumber(),bean.getContractNumber(),agentXmlBean.getAgent_no());
//				res = "失败"+response1;
//			}
//
//		}else{
//			logger.info("该订单不需要异步通知，商户交易未填异步通知地址:{},:{},:{}",bean.getCertNumber(),bean.getContractNumber(),agentXmlBean.getAgent_no());
//			res = "该订单不需要异步通知，商户交易未填异步通知地址";
//		}
//		return res;
//	}
//
//	/**
//	 * 用户授权结果   异步通知
//	 * @param bean
//	 * @param agentProperties
//	 * @param agentXmlBean
//	 * @return
//	 */
//	public static String authNotify(AuthQueryBean bean,Properties agentProperties,AgentXmlBean agentXmlBean){
//		String res="";
//		//调起第三方回调地址
//		if(bean.getNotifyAddress()!=null&&!"".equals(bean.getNotifyAddress())){
//			//机构号、平台订单号
//			Hashtable<String, String> paraMap1 = new Hashtable<>();
//			paraMap1.put("agentNo", agentXmlBean.getAgent_no());
//			paraMap1.put("orderNo", bean.getOrderNo());
//			paraMap1.put("platformOrderNo", bean.getPlatformOrderNo());
//			paraMap1.put("retCode", "0000");
//			paraMap1.put("retMsg", "授权成功");
//			paraMap1.put("name", bean.getFullName());
//			paraMap1.put("idCardNo", bean.getIdCardNo());
//			paraMap1.put("mobile", bean.getMobile());
//
//
//			String parStr1 = Sign.formatUrlMap(paraMap1, false, false,true);//所有参数除sign进行ASCII 排序
//			String sign1 = Disguiser.disguiseMD5(parStr1+"&key="+agentXmlBean.getSecret_key()).toUpperCase();//md5 转大写
//			long execTime=0;
//			paraMap1.put("sign", sign1);
//			logger.info("白骑士授权结果异步通知下游数据为:"+paraMap1);
//
//			String response = HttpUtil.doPost(bean.getNotifyAddress(), paraMap1);
//			logger.info("推送地址：{},通知结果:{}",bean.getNotifyAddress(),response);
//			if(response!=null&&response.contains("success")){//"success".equals(response1)
//				res="success";
//			}else{
//				//如果未接收到正常反馈，将数据存放到新的队列中  队列名是：当前时间加上3分钟后的时间
//				res="fail";
//				AuthNotifyOrderBean authNotifyOrderBean = new AuthNotifyOrderBean();
//				authNotifyOrderBean.setAgentNo(paraMap1.get("agentNo"));
//				authNotifyOrderBean.setOrderNumber(paraMap1.get("orderNo"));
//				authNotifyOrderBean.setPlatformOrderNumber(paraMap1.get("platformOrderNo"));
//				authNotifyOrderBean.setCode(paraMap1.get("retCode"));
//				authNotifyOrderBean.setMsg(paraMap1.get("retMsg"));
////				authNotifyOrderBean.setData(paraMap1.get("data"));
//				authNotifyOrderBean.setFrequency(1);
//				authNotifyOrderBean.setSign(paraMap1.get("sign"));
//				authNotifyOrderBean.setName(paraMap1.get("name"));
//				authNotifyOrderBean.setIdCardNo(paraMap1.get("idCardNo"));
//				authNotifyOrderBean.setMobile(paraMap1.get("mobile"));
//				authNotifyOrderBean.setUrl(bean.getNotifyAddress());
//
//				String nextPushTime=sdf.format(DateUtils.getDateByMinuteAdd(new Date(),3));
//				JedisUtil.lpush((RedisKeys.AUTHNOTIFYKEY+nextPushTime).getBytes(), JSON.toJSON(authNotifyOrderBean).toString().getBytes());
//			}
//
//			logger.info("白骑士授权查询结果异步通知下游地址为{},得到的反馈是{},此授权的平台订单号为{}",bean.getNotifyAddress(),response,bean.getPlatformOrderNo());
//
//		}else{
//			logger.info("该授权结果订单不需要异步通知，商户交易未填异步通知地址:{}",bean.getPlatformOrderNo());
//			res = "该订单不需要异步通知，商户交易未填异步通知地址";
//		}
//		return res;
//	}



}

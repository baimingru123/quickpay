package cn.pay.quickpay.channel.valid;

import java.math.BigDecimal;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.AgentBean;
import cn.pay.quickpay.bean.MerchantChannelBean;
import cn.pay.quickpay.bean.TransactionBean;
import cn.pay.quickpay.dao.IAgentDao;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.utils.platform.DateUtils;
import cn.pay.quickpay.utils.platform.ResultVOUtil;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import cn.pay.quickpay.xmlbean.VirtualChannelXmlBean;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author bmr
 * 创建时间：2019-1-14 下午4:17:51
 * 用于商户在各类型通道入驻、交易的前置校验功能
 * 商户通道入驻表中的费率信息与通道成本xml文件中的费率进行比较，防止用户入驻通道费率未修改成功
 */

public class ChannelValid {

	@Autowired
	static IAgentDao agentDao;
	
	/**
	 * 无积分通道入驻前置校验
	 * @param merchantChannelBean  下游传递的入驻数据
	 * @param channelXmlBean  该通道配置文件
	 * @return
	 */
	public static ResultVO noIntegralChannelEnterCheck(MerchantChannelBean merchantChannelBean, ChannelXmlBean channelXmlBean){
//		System.out.println("校验时merchantChannelBean："+merchantChannelBean);
		// 校验交易费率
		if (merchantChannelBean.getFee0().compareTo(channelXmlBean.getMin_rate_score0()) == -1
				|| merchantChannelBean.getFee0().compareTo(channelXmlBean.getMax_rate_score0()) == 1) {
			return ResultVOUtil.error(ResultEnum.MERCHANT_TRANS_FEE_SCORE_ERROR);
		}
		// 检验交易单笔代付费
		if (merchantChannelBean.getFee() < channelXmlBean.getMin_dfee_score() 
				|| merchantChannelBean.getFee() > channelXmlBean.getMax_dfee_score()) {
			return ResultVOUtil.error(ResultEnum.MERCHANT_DF_FEE_SCORE_ERROR);
		}
		
		//全部校验通过，返回0000
		return ResultVOUtil.success();
		
	}
	
	/**
	 * 快捷有积分、无积分通道交易前置校验
	 * @param transactionBean  下游传递的交易数据
	 * @param channelXmlBean  该通道配置文件
	 * @return
	 */
	public static ResultVO quickPayChannelTransactionCheck(TransactionBean transactionBean, VirtualChannelXmlBean virtualChannelXmlBean, ChannelXmlBean channelXmlBean){
		ResultVO resBean=new ResultVO();
		
		// 校验交易时间
		String currentDateTime = DateUtils.getCurrentDateTime();
		String startDate = virtualChannelXmlBean.getMin_time_score().trim();
		String endDate = virtualChannelXmlBean.getMax_time_score().trim();
		if (startDate.length() == 4) {
			startDate = "0" + startDate;
		}
		if (endDate.length() == 4) {
			endDate = "0" + endDate;
		}
		String yyyyMMdd = DateUtils.getLongYMD();
		String startTime = yyyyMMdd + " " + startDate + ":00";
		String endTime = yyyyMMdd + " " + endDate + ":59";

		if (currentDateTime.compareTo(startTime) < 0
				|| currentDateTime.compareTo(endTime) > 0) {
			resBean.setRetCode(ResultEnum.TRANS_TIME_SCORE_ERROR.getCode());
			resBean.setRetMsg("交易时间范围:" + virtualChannelXmlBean.getMin_time_score()+ "-" + virtualChannelXmlBean.getMax_time_score());
			return resBean;
		}

		// 校验单笔交易金额
		if (transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMin_money_score()) == -1
				|| transactionBean.getOrderAmount().compareTo(virtualChannelXmlBean.getMax_money_score()) == 1) {
			resBean.setRetCode(ResultEnum.TRANS_MONEY_SCORE_ERROR.getCode());
			resBean.setRetMsg("单笔限额:" + virtualChannelXmlBean.getMin_money_score()+ "元-" + virtualChannelXmlBean.getMax_money_score() + "元");
			return resBean;
		}

		// 校验交易费率(虚拟通道)
		if (transactionBean.getMerchantTransactionRate().compareTo(virtualChannelXmlBean.getMin_rate_score0()) == -1
				|| transactionBean.getMerchantTransactionRate().compareTo(virtualChannelXmlBean.getMax_rate_score0()) == 1) {
			resBean.setRetCode(ResultEnum.MERCHANT_TRANS_FEE_SCORE_ERROR.getCode());
			resBean.setRetMsg("交易费率范围:"+ virtualChannelXmlBean.getMin_rate_score0() + "%-"+ virtualChannelXmlBean.getMax_rate_score0() + "%");
			return resBean;
		}

		// 校验交易单笔手续费(虚拟通道)
		if (transactionBean.getMerchantCollection().multiply(new BigDecimal(100)).intValue() < virtualChannelXmlBean.getMin_dfee_score()
				|| transactionBean.getMerchantCollection().multiply(new BigDecimal(100)).intValue() > virtualChannelXmlBean.getMax_dfee_score()) {
			resBean.setRetCode(ResultEnum.MERCHANT_DF_FEE_SCORE_ERROR.getCode());
			resBean.setRetMsg("交易单笔代付费范围:"+ virtualChannelXmlBean.getMin_dfee_score() + "分-"+ virtualChannelXmlBean.getMax_dfee_score() + "分");
			return resBean;
		}
		
		//校验交易费率和交易单笔手续费(实际通道)
		if(transactionBean.getIsIntegral()==0){//无积分
			if(transactionBean.getMerchantTransactionRate().compareTo(channelXmlBean.getMin_rate_score0())==-1
					||transactionBean.getMerchantTransactionRate().compareTo(channelXmlBean.getMax_rate_score0())==1){
				resBean.setRetCode(ResultEnum.MERCHANT_TRANS_FEE_SCORE_ERROR.getCode());
				resBean.setRetMsg("交易费率范围:"+channelXmlBean.getMin_rate_score0()+"%-"+channelXmlBean.getMax_rate_score0()+"%");
				return resBean;
			}
		
			if(transactionBean.getMerchantCollection().multiply(new BigDecimal(100)).intValue()<channelXmlBean.getMin_dfee_score()
					||transactionBean.getMerchantCollection().multiply(new BigDecimal(100)).intValue()>channelXmlBean.getMax_dfee_score()){
				resBean.setRetCode(ResultEnum.MERCHANT_DF_FEE_SCORE_ERROR.getCode());
				resBean.setRetMsg("交易代付费范围:"+channelXmlBean.getMin_dfee_score()+"分-"+channelXmlBean.getMax_dfee_score()+"分");
				return resBean;
			}
		}else if(transactionBean.getIsIntegral()==1){//有积分
			if(transactionBean.getMerchantTransactionRate().compareTo(channelXmlBean.getIntegral_min_rate_score0())==-1
					||transactionBean.getMerchantTransactionRate().compareTo(channelXmlBean.getIntegral_max_rate_score0())==1){
				resBean.setRetCode(ResultEnum.MERCHANT_TRANS_FEE_SCORE_ERROR.getCode());
				resBean.setRetMsg("交易费率范围:"+channelXmlBean.getIntegral_min_rate_score0()+"%-"+channelXmlBean.getIntegral_max_rate_score0()+"%");
				return resBean;
			}
		
			if(transactionBean.getMerchantCollection().multiply(new BigDecimal(100)).intValue()<channelXmlBean.getIntegral_min_dfee_score()
					||transactionBean.getMerchantCollection().multiply(new BigDecimal(100)).intValue()>channelXmlBean.getIntegral_max_dfee_score()){
				resBean.setRetCode(ResultEnum.MERCHANT_DF_FEE_SCORE_ERROR.getCode());
				resBean.setRetMsg("交易代付费范围:"+channelXmlBean.getIntegral_min_dfee_score()+"分-"+channelXmlBean.getIntegral_max_dfee_score()+"分");
				return resBean;
			}
		}
		
		
		//全部校验通过，返回0000
		return ResultVOUtil.success();
	}
	
	/**
	 * 代付通道交易前置校验
	 * @param bean  下游传递的交易数据
	 * @param channelXmlBean  该通道配置文件
	 * @return
	 */
	public static ResultVO settlementChannelTransactionCheck(TransactionBean bean, ChannelXmlBean channelXmlBean, VirtualChannelXmlBean virtualChannelXmlBean, AgentXmlBean agentXmlBean){
		ResultVO resBean=new ResultVO();
		
		//校验交易时间是否在合法时间内
		String currentDateTime = DateUtils.getCurrentDateTime();
		String startDate = virtualChannelXmlBean.getMin_time_score().trim();
		String endDate = virtualChannelXmlBean.getMax_time_score().trim();
		if (startDate.length() == 4) {
			startDate = "0" + startDate;
		}
		if (endDate.length() == 4) {
			endDate = "0" + endDate;
		}
		String yyyyMMdd = DateUtils.getLongYMD();
		String startTime = yyyyMMdd + " " + startDate + ":00";
		String endTime = yyyyMMdd + " " + endDate + ":59";
		
		if (currentDateTime.compareTo(startTime) < 0|| currentDateTime.compareTo(endTime) > 0) {
			resBean.setRetCode(ResultEnum.TRANS_TIME_SCORE_ERROR.getCode());
			resBean.setRetMsg("交易时间范围"+ virtualChannelXmlBean.getMin_time_score() + "-"
					+ virtualChannelXmlBean.getMax_time_score());
			return resBean;
		}

		// 校验交易金额是否在合法区间内
		if (bean.getOrderAmount().compareTo(virtualChannelXmlBean.getMin_money_score()) == -1
				|| bean.getOrderAmount().compareTo(virtualChannelXmlBean.getMax_money_score()) == 1) {
			resBean.setRetCode(ResultEnum.TRANS_MONEY_SCORE_ERROR.getCode());
			resBean.setRetMsg("单笔限额" + virtualChannelXmlBean.getMin_money_score()+ "元-" + virtualChannelXmlBean.getMax_money_score() + "元");
			return resBean;
		}
		
		// 校验单笔代付手续费是否在合法区间内(虚拟通道)
		if (bean.getMerchantCollection().multiply(new BigDecimal(100)).intValue() < virtualChannelXmlBean.getMin_balance_score()
				|| bean.getMerchantCollection().multiply(new BigDecimal(100)).intValue() > virtualChannelXmlBean.getMax_balance_score()) {
			resBean.setRetCode(ResultEnum.MERCHANT_DF_FEE_SCORE_ERROR.getCode());
			resBean.setRetMsg("代付费范围" + virtualChannelXmlBean.getMin_balance_score()+ "分-" + virtualChannelXmlBean.getMax_balance_score() + "分");
			return resBean;
		}
		
		// 校验单笔代付手续费是否在合法区间内(真实通道)
		if (bean.getMerchantCollection().multiply(new BigDecimal(100)).intValue() < channelXmlBean.getMin_balance_score()
				|| bean.getMerchantCollection().multiply(new BigDecimal(100)).intValue() > channelXmlBean.getMax_balance_score()) {
			resBean.setRetCode(ResultEnum.MERCHANT_DF_FEE_SCORE_ERROR.getCode());
			resBean.setRetMsg("代付费范围" + channelXmlBean.getMin_balance_score()+ "分-" + channelXmlBean.getMax_balance_score() + "分");
			return resBean;
		}

		// 校验代理商余额是否大于代付金额
		AgentBean agentBean = agentDao.getById(agentXmlBean.getId());
		if (agentBean.getBalance().compareTo(bean.getOrderAmount()) == -1) {
			resBean.setRetCode(ResultEnum.AGENT_DF_BALANCE_INSUFFICIENT.getCode());
			resBean.setRetMsg(ResultEnum.AGENT_DF_BALANCE_INSUFFICIENT.getMessage());
			return resBean;
		}

		// 获取支持银行列表以及银行卡行别
//		BankCardBinBean bankCardbinBean = config.bankCardbinDao.getByCardBin(bean.getBankCardNo().substring(0, 6),
//								bean.getBankCardNo().length()+ "");
//		if (bankCardbinBean == null) {
//			resBean.setCode("2015");
//			resBean.setMessage("未搜索到该银行卡所属银行");
//			return resBean;
//		}
		
		//全部校验通过，返回0000
		return ResultVOUtil.success();
	}
	
	
	/**
	 * 结算通道入驻前置校验
	 * @param merchantChannelBean  下游传递的入驻数据
	 * @param channelXmlBean  该通道配置文件
	 * @return
	 */
	public static ResultVO settleChannelEnterCheck(MerchantChannelBean merchantChannelBean,ChannelXmlBean channelXmlBean){
//		System.out.println("balanceFee:"+merchantChannelBean.getBalanceFee()+",min:"+channelXmlBean.getMin_balance_score()+",max:"+channelXmlBean.getMax_balance_score());
		//校验结算单笔费用
		if(merchantChannelBean.getBalanceFee()<channelXmlBean.getMin_balance_score() || merchantChannelBean.getBalanceFee()>channelXmlBean.getMax_balance_score()){
			return ResultVOUtil.error(ResultEnum.MERCHANT_JS_FEE_SCORE_ERROR);
		}
		
		//全部校验通过，返回0000
		return ResultVOUtil.success();
		
	}
}

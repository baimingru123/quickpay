package cn.pay.quickpay.convert;

import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.bean.MerchantCardBean;
import cn.pay.quickpay.bean.TransactionBean;
import cn.pay.quickpay.dto.PayConfirmDto;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author bmr
 * @time 2019-02-15 14:23
 * request对象中的数据转换为对应的实体类
 */
@Slf4j
public class Request2BeanConvert {

    /**
     * 实名认证接口request转换为实体类
     * @param req
     * @return
     */
    public static MerchantBean convertToMerchantBeanRealAuth(HttpServletRequest req){
        MerchantBean bean = new MerchantBean();
        bean.setIdCard(req.getParameter("idCard")!=null?req.getParameter("idCard"):"");
        bean.setSettleBankCardNo(req.getParameter("settleBankCardNo")!=null?req.getParameter("settleBankCardNo"):"");
        bean.setSettleName(req.getParameter("settleName")!=null?req.getParameter("settleName"):"");
        bean.setSettlePhone(req.getParameter("settlePhone")!=null?req.getParameter("settlePhone"):"");
        log.info("结算卡校验:{}",bean);
        return bean;
    }


    /**
     * 商户入驻实体转换
     * 将下游传递进来的数据转换为商户实体类
     * @param req
     * @return
     */
    public static MerchantBean convertToMerchantBean(HttpServletRequest req){
        MerchantBean bean = new MerchantBean();
        bean.setAgentNo(req.getParameter("agentNo")!=null?req.getParameter("agentNo"):"");
        bean.setIdCard(req.getParameter("idCard")!=null?req.getParameter("idCard"):"");
        bean.setMerNo(req.getParameter("merNo")!=null?req.getParameter("merNo"):"");
        bean.setPlatformMerNo(req.getParameter("platformMerNo")!=null?req.getParameter("platformMerNo"):"");
        bean.setMerName(req.getParameter("merName")!=null?req.getParameter("merName"):"");
        bean.setMerAddress(req.getParameter("merAddress")!=null?req.getParameter("merAddress"):"");
        bean.setSettleBank(req.getParameter("settleBank")!=null?req.getParameter("settleBank"):"");
        bean.setSettleBankBranch(req.getParameter("settleBankBranch")!=null?req.getParameter("settleBankBranch"):"");
        bean.setSettleBankCardNo(req.getParameter("settleBankCardNo")!=null?req.getParameter("settleBankCardNo"):"");
        bean.setSettleBankNo(req.getParameter("settleBankNo")!=null?req.getParameter("settleBankNo"):"");
        bean.setSettleBankSub(req.getParameter("settleBankSub")!=null?req.getParameter("settleBankSub"):"");
        bean.setSettleName(req.getParameter("settleName")!=null?req.getParameter("settleName"):"");
        bean.setSettlePhone(req.getParameter("settlePhone")!=null?req.getParameter("settlePhone"):"");
        bean.setSettleSubCity(req.getParameter("settleSubCity")!=null?req.getParameter("settleSubCity"):"");
        bean.setSettleSubProvince(req.getParameter("settleSubProvince")!=null?req.getParameter("settleSubProvince"):"");


        //有积分费率
        bean.setFee0((req.getParameter("fee0")!=null&&!"".equals(req.getParameter("fee0")))?new BigDecimal(req.getParameter("fee0")):new BigDecimal("0"));
        bean.setD0fee((req.getParameter("d0fee")!=null&&!"".equals(req.getParameter("d0fee")))?Integer.parseInt(req.getParameter("d0fee")):0);
        //检验用户传递的费率与0进行比较 (-1小于  0等于  1大于)  -1时表示传递的费率不合法    0表示用户未开通此类支付通道或者传递过来的费率为0  1表示用户开通了此类通道，初步校验通过
        if(bean.getFee0().compareTo(new BigDecimal(0))==1){
            bean.setUsableIntegral(true);
//            log.info("商户进件快捷有积分");
        }

        //无积分费率
        bean.setNoIntegralFee0((req.getParameter("noIntegralFee0")!=null&&!"".equals(req.getParameter("noIntegralFee0")))?new BigDecimal(req.getParameter("noIntegralFee0")):new BigDecimal("0"));
        bean.setNoIntegralFee((req.getParameter("noIntegralFee")!=null&&!"".equals(req.getParameter("noIntegralFee")))?Integer.parseInt(req.getParameter("noIntegralFee")):0);
        if(bean.getNoIntegralFee0().compareTo(new BigDecimal(0))==1){
            bean.setUsableNoIntegral(true);
//            log.info("商户进件快捷无积分");
        }

        //划扣费率
        bean.setDelimitFee0((req.getParameter("delimitFee0")!=null&&!"".equals(req.getParameter("delimitFee0")))?new BigDecimal(req.getParameter("delimitFee0")):new BigDecimal("0"));
        bean.setDelimitFee((req.getParameter("delimitFee")!=null&&!"".equals(req.getParameter("delimitFee")))?Integer.parseInt(req.getParameter("delimitFee")):0);
        bean.setWithholdFee((req.getParameter("withholdFee")!=null&&!"".equals(req.getParameter("withholdFee")))?Integer.parseInt(req.getParameter("withholdFee")):0);
        if(bean.getDelimitFee0().compareTo(new BigDecimal(0))==1 || bean.getWithholdFee()>0){
            bean.setUsableDelimit(true);
//            log.info("商户进件划扣");
        }

        //智能代还费率
        bean.setRepayFee0((req.getParameter("repayFee0")!=null&&!"".equals(req.getParameter("repayFee0")))?new BigDecimal(req.getParameter("repayFee0")):new BigDecimal("0"));
        bean.setRepayFee((req.getParameter("repayFee")!=null&&!"".equals(req.getParameter("repayFee")))?Integer.parseInt(req.getParameter("repayFee")):0);
        bean.setRepayFee1((req.getParameter("repayFee1")!=null&&!"".equals(req.getParameter("repayFee1")))?Integer.parseInt(req.getParameter("repayFee1")):0);
        if(bean.getRepayFee0().compareTo(new BigDecimal(0))==1){
            bean.setUsableRepay(true);
//            log.info("商户进件智能代还");
        }

        //结算单笔手续费
        bean.setBalanceFee((req.getParameter("balanceFee")!=null&&!"".equals(req.getParameter("balanceFee")))?Integer.parseInt(req.getParameter("balanceFee")):0);
        if(bean.getBalanceFee()>0){
            bean.setUsableBalance(true);
//            log.info("商户进件结算");
        }

        //结算方式  不填默认D0结算
        bean.setSettleType((req.getParameter("settleType")!=null&&!"".equals(req.getParameter("settleType")))?Integer.parseInt(req.getParameter("settleType")):2);

        //修改信息时，选择的修改类型
        bean.setMerFlag(req.getParameter("merFlag")!=null?req.getParameter("merFlag"):"");

        return bean;
    }

    /**
     * 预下单实体转换
     * 将下游传递进来的数据转换为transaction实体类
     * @param req
     * @return
     */
    public static TransactionBean convertTransactionBean(HttpServletRequest req){
        TransactionBean bean = new TransactionBean();
        bean.setMerchantNumber(req.getParameter("platformMerNo")!=null?req.getParameter("platformMerNo"):"");//商户号由闪付时代分配
        bean.setOrderNumber(req.getParameter("orderNumber")!=null?req.getParameter("orderNumber"):"");//商户系统内部订单号，要求50字符以内，同一商户号下订单号唯一
        bean.setPayerName(req.getParameter("payerName")!=null?req.getParameter("payerName"):"");//姓名
        bean.setIdCardType(req.getParameter("idCardType")!=null?req.getParameter("idCardType"):"0");//证件类型（0身份证、1其他）
        bean.setIdCardNo(req.getParameter("idCardNo")!=null?req.getParameter("idCardNo"):"");//身份证号码
        bean.setBankCardNo(req.getParameter("bankCardNo")!=null?req.getParameter("bankCardNo"):"");//银行卡号
        bean.setBankCardName(req.getParameter("bankCardName")!=null?req.getParameter("bankCardName"):"");
        bean.setYear(req.getParameter("year")!=null?req.getParameter("year"):"");//当银行卡是信用卡时必输信用卡有效期年
        bean.setMonth(req.getParameter("month")!=null?req.getParameter("month"):"");//当银行卡是信用卡时必输信用卡有效期月
        bean.setCvv2(req.getParameter("cvv2")!=null?req.getParameter("cvv2"):"");//当银行卡是信用卡时必输信用卡安全码
        bean.setPhone(req.getParameter("phone")!=null?req.getParameter("phone"):"");//手机号
        bean.setCurrency(req.getParameter("currency")!=null?req.getParameter("currency"):"CNY");//暂只支持人民币：CNY
        bean.setOrderAmount(new BigDecimal(req.getParameter("orderAmount")!=null?req.getParameter("orderAmount"):"0"));//订单金额，以元为单位，最小金额为0.01
        bean.setGoodsName(req.getParameter("goodsName")!=null?req.getParameter("goodsName"):"");//商品名称
        bean.setGoodsDesc(req.getParameter("goodsDesc")!=null?req.getParameter("goodsDesc"):"");//商品描述
        bean.setTerminalType(req.getParameter("terminalType")!=null?req.getParameter("terminalType"):"OTHER");//IMEI、MAC、UUID（针对 IOS 系统）、OTHER
        bean.setTerminalId(req.getParameter("terminalId")!=null?req.getParameter("terminalId"):"");//终端唯一标识，如手机序列号
        bean.setOrderIp(req.getParameter("orderIp")!=null?req.getParameter("orderIp"):"");//用户支付时使用的网络终端 IP
        bean.setPeriod(req.getParameter("period")!=null?Integer.parseInt(req.getParameter("period")):1);//过了订单有效时间的订单会被设置为取消状态不能再重新进行支付
        bean.setPeriodUnit(req.getParameter("periodUnit")!=null?req.getParameter("periodUnit"):"Day");//Day：天、Hour：时、Minute：分
        bean.setNotifyUrl(req.getParameter("notifyUrl")!=null?req.getParameter("notifyUrl"):"");
        bean.setReturnUrl(req.getParameter("returnUrl")!=null?req.getParameter("returnUrl"):"");
        bean.setPlatformOrderNumber(req.getParameter("platformOrderNumber")!=null?req.getParameter("platformOrderNumber"):"");//平台订单号
        bean.setIsIntegral(req.getParameter("isIntegral")!=null?new Integer(req.getParameter("isIntegral")):0);//是否有积分（1有积分、0无积分）
        bean.setType(req.getParameter("type")!=null?new Integer(req.getParameter("type")):0);//交易类型（0快捷消费、1智能代还消费、2智能代还还款、3代付）
        bean.setSettleType(req.getParameter("settleType")!=null?new Integer(req.getParameter("settleType")):0);//结算方式(0：直清由我方直接代付出款，直接结算到商户结算卡。1刷卡金额记入商户余额账户，接入方可主动发起代付。每笔代付都收取提现费,2：刷卡金额记入机构余额账户)
        bean.setPlatformOrderNumberList(req.getParameter("platformOrderNumberList")!=null?req.getParameter("platformOrderNumberList"):"");//平台订单号集合
        bean.setPayType(req.getParameter("payType")!=null?Integer.parseInt(req.getParameter("payType")):0);
        bean.setIssms(req.getParameter("issms")!=null?Boolean.parseBoolean(req.getParameter("issms")):false);
        return bean;
    }


    /**
     * 确认支付实体转换
     * 将下游传递进来的数据转换为PayConfirmDto
     * @param req
     * @return
     */
    public static PayConfirmDto convertConfirmPay(HttpServletRequest req){
        PayConfirmDto payConfirmBean = new PayConfirmDto();
        payConfirmBean.setBizType(req.getParameter("bizType"));
        payConfirmBean.setAgentNo(req.getParameter("agentNo")!=null?req.getParameter("agentNo"):"");
        payConfirmBean.setPlatformMerNo(req.getParameter("platformMerNo")!=null?req.getParameter("platformMerNo"):"");
        payConfirmBean.setOrderNumber(req.getParameter("orderNumber"));//商户系统内部订单号，要求50字符以内，同一商户号下订单号唯一
        payConfirmBean.setPlatformOrderNumber(req.getParameter("platformOrderNumber"));//平台订单号
        payConfirmBean.setTimestamp(req.getParameter("timestamp"));//格式：yyyyMMddHHmmss 14 位数字，精确到秒
        payConfirmBean.setValidateCode(req.getParameter("validateCode"));//验证码
        payConfirmBean.setOrderIp(req.getParameter("orderIp"));//支付ip
        return payConfirmBean;
    }


    /**
     * 商户绑卡实体转换
     * 将下游传递进来的数据转换为MerchantCardBean
     * @param req
     * @return
     */
    public static MerchantCardBean convertMerchantCardBean(HttpServletRequest req){
        MerchantCardBean bean = new MerchantCardBean();
        bean.setAgentNo(req.getParameter("agentNo")!=null?req.getParameter("agentNo"):"");
        bean.setPlatformMerNo(req.getParameter("platformMerNo")!=null?req.getParameter("platformMerNo"):"");
        bean.setOutTradeNo(req.getParameter("outTradeNo")!=null?req.getParameter("outTradeNo"):"");
        bean.setPlatformOutTradeNo(req.getParameter("platformOutTradeNo")!=null?req.getParameter("platformOutTradeNo"):"");
        bean.setSmsCode(req.getParameter("smsCode")!=null?req.getParameter("smsCode"):"");
        bean.setYear(req.getParameter("year")!=null?req.getParameter("year"):"");
        bean.setMonth(req.getParameter("month")!=null?req.getParameter("month"):"");
        bean.setCvv2(req.getParameter("cvv2")!=null?req.getParameter("cvv2"):"");
        bean.setPhone(req.getParameter("phone")!=null?req.getParameter("phone"):"");//手机号
        bean.setBankCardNo(req.getParameter("bankCardNo")!=null?req.getParameter("bankCardNo"):"");
        bean.setAgreementNo(req.getParameter("agreementNo")!=null?req.getParameter("agreementNo"):"");
        bean.setIdcardImg(req.getParameter("idcardImg")!=null?req.getParameter("idcardImg"):"");
        bean.setIdcardReverseImg(req.getParameter("idcardReverseImg")!=null?req.getParameter("idcardReverseImg"):"");
        bean.setHandIdcard(req.getParameter("handIdcard")!=null?req.getParameter("handIdcard"):"");
        bean.setReturnUrl(req.getParameter("returnUrl")!=null?req.getParameter("returnUrl"):"");
        bean.setType((req.getParameter("type")!=null&&!"".equals(req.getParameter("type")))?Integer.parseInt(req.getParameter("type")):0);
        bean.setIdCardNo(req.getParameter("idCardNo")!=null?req.getParameter("idCardNo"):"");
        bean.setFullName(req.getParameter("fullName")!=null?req.getParameter("fullName"):"");
        log.info("商户绑卡:{}",bean.toString());
        return bean;
    }
}

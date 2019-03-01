package cn.pay.quickpay.service.impl;

import cn.pay.quickpay.VO.AddMerResultVO;
import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.AgentBean;
import cn.pay.quickpay.bean.BankCertificationBean;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.bean.MerchantChannelBean;
import cn.pay.quickpay.channel.base.AuthChannelAbstract;
import cn.pay.quickpay.channel.base.ChannelAbstract;
import cn.pay.quickpay.channel.quick.ShanghaiYitongBCChannel;
import cn.pay.quickpay.config.CoreConfig;
import cn.pay.quickpay.convert.Request2BeanConvert;
import cn.pay.quickpay.dao.IAgentDao;
import cn.pay.quickpay.dao.IBankCertificationDao;
import cn.pay.quickpay.dao.IMerchantChannelDao;
import cn.pay.quickpay.dao.IMerchantDao;
import cn.pay.quickpay.enums.BizTypeEnum;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.exception.QuickPayException;
import cn.pay.quickpay.form.QuickpayForm;
import cn.pay.quickpay.service.*;
import cn.pay.quickpay.utils.platform.*;
import cn.pay.quickpay.utils.platform.security.MD5Tools;
import cn.pay.quickpay.valid.QuickpayRequsetValid;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.AuthChannelXmlBean;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import cn.pay.quickpay.xmlbean.VirtualChannelXmlBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author bmr
 * @time 2019-02-15 8:22
 * 统一入口service层
 * 主要用来签名校验、代理商、商户状态是否异常等
 */
@Service
@Slf4j
public class QuickpayServiceImpl implements QuickpayService {

    @Autowired
    CoreConfig config;

    @Autowired
    private GetChannelXmlBeanUtil getChannelXmlBeanUtil;

    @Autowired
    private IMerchantDao merchantDao;

    @Autowired
    private IAgentDao agentDao;

    @Autowired
    private RealAuthService realAuthService;

    @Autowired
    private AddMerService addMerService;

    @Autowired
    private UpdateMerService updateMerService;

    @Autowired
    private AddOrderService addOrderService;

    @Autowired
    private PayConfirmService payConfirmService;

    @Autowired
    private QueryOrderService queryOrderService;

    @Autowired
    private PayForAnotherService payForAnotherService;

    @Autowired
    private OpenCardCodeService openCardCodeService;

    @Autowired
    private OpenCardService openCardService;

    @Autowired
    private AgentService agentService;





    @Override
    public ResultVO checkChannel(QuickpayForm quickpayForm, HttpServletRequest request, HttpServletResponse response) {

        String agentNo=quickpayForm.getAgentNo();
        String bizType=quickpayForm.getBizType();
        String signStr=quickpayForm.getSign();


        // 客户端IP
        String remoteIP = IpUtils.getIpAddr(request);
//    	logger.info("bizType:{}，agentNo：{}，remoteIP:{}",bizType,agentNo,remoteIP);

        //校验机构是否存在
        AgentXmlBean agentXmlBean = getChannelXmlBeanUtil.getAgentXml(agentNo);
        if(agentXmlBean==null){
            log.info("【机构不存在】bizType:{}，agentNo：{}",bizType,agentNo);
            return ResultVOUtil.error(ResultEnum.AGENT_NOT_EXIST);
        }

        //校验是否为渠道商，运营商不可用该接口
        if(agentXmlBean.getIs_operator()==1){
            log.info("【机构为运营商,该接口不可用】bizType:{}，agentNo：{}",bizType);
            return ResultVOUtil.error(ResultEnum.AGENT_TYPE_ERROR);
        }

        //渠道商分库属性文件
        Properties agentProperties = new Properties();
        agentProperties.setProperty("driver", agentXmlBean.getDb_driver());
        agentProperties.setProperty("url", agentXmlBean.getDb_url());
        agentProperties.setProperty("username", agentXmlBean.getDb_username());
        agentProperties.setProperty("password", agentXmlBean.getDb_password());

//        System.out.println("机构配置实体类："+agentXmlBean);

        //总库属性文件
        Properties platformProperties=PropertiesUtil.readProperties("jdbc.properties");


        //商户入驻用机构秘钥验签，其余用商户秘钥验签
        MerchantBean merchantBean = null;
        String key = "";
        if(bizType.equals("addMem")||bizType.equals("queryChannel")||bizType.equals("realAuth")||bizType.equals("agentBalance")){
            key = agentXmlBean.getSecret_key();
        }else{
            //验证商户和机构号
            String platformMerNo = quickpayForm.getPlatformMerNo();// 平台商户号
            if(platformMerNo==null || "".equals(platformMerNo)){
                log.info("【必填参数为空】bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"平台商户号必填");
                return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),"平台商户号必填");
            }

            //校验商户是否存在
            merchantBean = merchantDao.getByPlatformMerNo(agentProperties,platformMerNo);
            if(merchantBean==null){
                log.info("【商户不存在】bizType:{}，agentNo：{}",bizType,agentNo);
                return ResultVOUtil.error(ResultEnum.MERCHANT_NOT_EXIST);
            }

            if(!"0".equals(merchantBean.getStatus())){
                log.info("【商户状态异常】bizType:{}，agentNo：{}",bizType,agentNo);
                return ResultVOUtil.error(ResultEnum.MERCHANT_STATUS_ERROR);
            }
            key = merchantBean.getPlatformMerKey();

        }

        //组合验签参数，并进行签名验证
        Hashtable<String, String> paraMap = new Hashtable<>();
        Enumeration<String> pNames=request.getParameterNames();
        String paramsStr = "";
        while(pNames.hasMoreElements()){
            String name=(String)pNames.nextElement();
            if(!name.equals("sign")){
                String value=request.getParameter(name);
                paramsStr += name + "=" + value+"&";
                paraMap.put(name,value);
            }
        }
        log.info("【下游传递的数据拼接串】bizType:{}，agentNo：{}，请求参数：{}",bizType,agentNo,paramsStr);

        String parStr = SignUtil.formatUrlMap(paraMap, false, false,true);//所有参数除sign进行ASCII 排序
        log.info("【组装参与签名的参数】bizType:{}，agentNo：{}，parStr：{}", bizType, agentNo,parStr);
        String sign = MD5Tools.MD5(parStr+"&key="+key).toUpperCase();
        log.info("【签名校验】bizType:{}，agentNo：{}，平台组装的签名:{},下游上传的签名:{}",bizType, agentNo, sign, signStr);
        if(!sign.equals(signStr)){
            log.info("【签名校验失败】bizType:{}，agentNo：{}", bizType, agentNo);
            return ResultVOUtil.error(ResultEnum.SIGN_CHECK_ERROR);
        }




        if(BizTypeEnum.REAL_AUTH.getBizTypeCode().equals(bizType)){//实名认证
            return realAuthService.realAuth(request,agentXmlBean);
        }else if(BizTypeEnum.ADD_MERCHANT_INFO.getBizTypeCode().equals(bizType)){//商户入驻
            return addMerService.addMer(request,merchantBean,agentXmlBean,agentProperties,platformProperties);
        }else if(BizTypeEnum.UPDATE_MERCHANT_INFO.getBizTypeCode().equals(bizType)){//修改商户入驻信息
            return updateMerService.updateMer(request,merchantBean,agentXmlBean,agentProperties,platformProperties);
        }else if(BizTypeEnum.ADD_ORDER.getBizTypeCode().equals(bizType)){//预下单
            return addOrderService.addOrder(request,response,merchantBean,agentXmlBean,agentProperties,platformProperties);
        }else if(BizTypeEnum.PAY_CONFIRM.getBizTypeCode().equals(bizType)){//确认支付
            return payConfirmService.quickPayConfirm(request,response,merchantBean,agentXmlBean,agentProperties,platformProperties);
        }else if(BizTypeEnum.QUERY_ORDER.getBizTypeCode().equals(bizType)){//交易订单查询
            return queryOrderService.queryOrder(request,response,merchantBean,agentXmlBean,agentProperties,platformProperties);
        }else if(BizTypeEnum.PAY_FOR_ANOTHER.getBizTypeCode().equals(bizType)){//代付
            return payForAnotherService.payForAnotherOne(request,response,merchantBean,agentXmlBean,agentProperties,platformProperties);
        }else if(BizTypeEnum.OPEN_CARD_CODE.getBizTypeCode().equals(bizType)){//卡签约发送短信验证码
            return openCardCodeService.openCardCode(request,response,merchantBean,agentXmlBean,agentProperties,platformProperties);
        }else if(BizTypeEnum.OPEN_CARD.getBizTypeCode().equals(bizType)){//卡签约确认
            return openCardService.openCard(request,response,merchantBean,agentXmlBean,agentProperties,platformProperties);
        }else if(BizTypeEnum.GET_AGENT_BALANCE.getBizTypeCode().equals(bizType)){
            return agentService.getAgentBalance(request,response,agentXmlBean);
        }else{
            return ResultVOUtil.error(ResultEnum.BIZ_TYPE_ERROR);
        }


    }

}

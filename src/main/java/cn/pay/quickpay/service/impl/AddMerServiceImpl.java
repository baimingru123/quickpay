package cn.pay.quickpay.service.impl;

import cn.pay.quickpay.VO.AddMerResultVO;
import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.BankCertificationBean;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.bean.MerchantChannelBean;
import cn.pay.quickpay.channel.base.ChannelAbstract;
import cn.pay.quickpay.config.CoreConfig;
import cn.pay.quickpay.convert.Request2BeanConvert;
import cn.pay.quickpay.dao.IBankCertificationDao;
import cn.pay.quickpay.dao.IMerchantChannelDao;
import cn.pay.quickpay.dao.IMerchantDao;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.exception.QuickPayException;
import cn.pay.quickpay.service.AddMerService;
import cn.pay.quickpay.utils.platform.*;
import cn.pay.quickpay.utils.platform.valid.VirtualChannelValid;
import cn.pay.quickpay.valid.QuickpayRequsetValid;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import cn.pay.quickpay.xmlbean.VirtualChannelXmlBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author bmr
 * @time 2019-02-21 16:27
 */
@Service
@Slf4j
public class AddMerServiceImpl implements AddMerService {

    @Autowired
    CoreConfig config;

    @Autowired
    private IMerchantDao merchantDao;

    @Autowired
    private IMerchantChannelDao merchantChannelDao;

    @Autowired
    private IBankCertificationDao bankCertificationDao;

    @Autowired
    private GetChannelXmlBeanUtil getChannelXmlBeanUtil;

    @Autowired
    private ChannelUseCheckUtil channelUseCheckUtil;

    @Override
    public ResultVO addMer(HttpServletRequest request, MerchantBean mb, AgentXmlBean agentXmlBean, Properties agentProperties, Properties platformProperties) {
        String bizType=request.getParameter("bizType");
        String agentNo=request.getParameter("agentNo");

        ResultVO addMerParamVaildResult=QuickpayRequsetValid.addMerParamVaild(request);
        if(!addMerParamVaildResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
            log.info("【必填参数为空】bizType:{}，agentNo：{}，错误信息：{}", bizType, agentNo,addMerParamVaildResult.getRetMsg());
            return ResultVOUtil.error(addMerParamVaildResult);
        }


        MerchantBean merchantBeanOfAddMer = Request2BeanConvert.convertToMerchantBean(request);
        log.info("【商户入驻下游传递数据】:{}",merchantBeanOfAddMer);


        //机构
        merchantBeanOfAddMer.setAgentId(agentXmlBean.getId());
        merchantBeanOfAddMer.setAgentNo(agentXmlBean.getAgent_no());
        //商户进件
        MerchantChannelBean integralMerchant = null;//有积分
        MerchantChannelBean noIntegralMerchant =  null;//无积分
        MerchantChannelBean repayMerchant =  null;//智能代还
        MerchantChannelBean balanceMerchant =  null;//结算
        MerchantChannelBean delimitMerchant =  null;//划扣


        //判断给商户配置的可用通道(有积分、无积分、智能代还、结算、划扣)
        if(merchantBeanOfAddMer.isUsableIntegral()||merchantBeanOfAddMer.isUsableNoIntegral()||merchantBeanOfAddMer.isUsableRepay()||merchantBeanOfAddMer.isUsableBalance()||merchantBeanOfAddMer.isUsableDelimit()){

            //有积分通道
            if(merchantBeanOfAddMer.isUsableIntegral()){
                //当前机构可用有积分通道
                List<VirtualChannelXmlBean> integralChannelList = agentXmlBean.getIntegralChannelList();
                if(integralChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【有积分】");
                    return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【有积分】");
                }

                //渠道商每种类型只配一个虚拟通道，因为渠道商下边的商户在各通道进件的时候费率固定
                //运营商每种类型可配多个虚拟通道，因为运营商下边的商户可以选择不同费率的通道进件
                VirtualChannelXmlBean virtualChannelXmlBean = integralChannelList.get(0);
                String channelCode = virtualChannelXmlBean.getCode();//通道号
                //校验通道是否可用
                ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                    return useCheckResult;
                }

                //进行费率的前置校验
                ResultVO validResult=VirtualChannelValid.integralChannelEnterCheck(merchantBeanOfAddMer,virtualChannelXmlBean);
                if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                    return validResult;
                }



                int channelId 	= virtualChannelXmlBean.getDefault_channel();//通道id
                MerchantChannelBean merchantChannelBean = new MerchantChannelBean();
                merchantChannelBean.setChannelId(channelId);
                merchantChannelBean.setChannelCode(channelCode);
                merchantChannelBean.setCreateTime(DateUtils.getCurrentDateTime());
                merchantChannelBean.setFee0(merchantBeanOfAddMer.getFee0());
                merchantChannelBean.setFee(merchantBeanOfAddMer.getD0fee());
                merchantChannelBean.setRepayFee(0);
                merchantChannelBean.setBalanceFee(0);
                merchantChannelBean.setWithholdFee(0);
                merchantChannelBean.setType("0");//有积分
                merchantChannelBean.setOpenQuick("0");//开通快捷（0未开通、1已开通）
                merchantChannelBean.setUpdateTime(DateUtils.getCurrentDateTime());
                merchantChannelBean.setVirtualChannelId(virtualChannelXmlBean.getId());
                //缺商户id
                //通道方返回第三方商户号、秘钥
                //动态加载通道配置文件
                ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                ResultVO addMemchantChannelResult = channel.addMemchantChannel(agentXmlBean,merchantBeanOfAddMer, merchantChannelBean,null,null,channelXmlBean);
                if(ResultEnum.SUCCESS.getCode().equals(addMemchantChannelResult.getRetCode())){//商户在通道方进件成功
                    integralMerchant = merchantChannelBean;
                }else{
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,addMemchantChannelResult.getRetMsg());
                    return ResultVOUtil.error( addMemchantChannelResult);
                }


            }

            //无积分通道
            if(merchantBeanOfAddMer.isUsableNoIntegral()){

                //当前机构可用无积分通道
                List<VirtualChannelXmlBean> noIntegralChannelList = agentXmlBean.getChannelList();
                if(noIntegralChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【无积分】");
                    return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【无积分】");
                }

                //渠道商每种类型只配一个虚拟通道，因为渠道商下边的商户在各通道进件的时候费率固定
                //运营商每种类型可配多个虚拟通道，因为运营商下边的商户可以选择不同费率的通道进件
                VirtualChannelXmlBean virtualChannelXmlBean = noIntegralChannelList.get(0);
                String channelCode = virtualChannelXmlBean.getCode();//通道号
                //校验通道是否可用
                ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                    return useCheckResult;
                }

                //进行费率的前置校验
                ResultVO validResult=VirtualChannelValid.noIntegralChannelEnterCheck(merchantBeanOfAddMer,virtualChannelXmlBean);
                if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                    return validResult;
                }

                int channelId 	= virtualChannelXmlBean.getDefault_channel();//通道id
                MerchantChannelBean merchantChannelBean = new MerchantChannelBean();
                merchantChannelBean.setChannelId(channelId);
                merchantChannelBean.setChannelCode(channelCode);
                merchantChannelBean.setCreateTime(DateUtils.getCurrentDateTime());
                merchantChannelBean.setFee0(merchantBeanOfAddMer.getNoIntegralFee0());
                merchantChannelBean.setFee(merchantBeanOfAddMer.getNoIntegralFee());
                merchantChannelBean.setRepayFee(0);
                merchantChannelBean.setBalanceFee(0);
                merchantChannelBean.setWithholdFee(0);
                merchantChannelBean.setType("1");//无积分
                merchantChannelBean.setOpenQuick("0");//开通快捷（0未开通、1已开通）
                merchantChannelBean.setUpdateTime(DateUtils.getCurrentDateTime());
                merchantChannelBean.setVirtualChannelId(virtualChannelXmlBean.getId());
                //缺商户id
                //通道方返回第三方商户号、秘钥
                //动态加载通道配置文件
                ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                ResultVO addMemchantChannelResult = channel.addMemchantChannel(agentXmlBean,merchantBeanOfAddMer, merchantChannelBean,null,null,channelXmlBean);
                if("0000".equals(addMemchantChannelResult.getRetCode())){//商户可以在通道方进件
                    noIntegralMerchant = merchantChannelBean;
                }else{
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,addMemchantChannelResult.getRetMsg());
                    return ResultVOUtil.error( addMemchantChannelResult);
                }

            }

            //智能代还通道
            if(merchantBeanOfAddMer.isUsableRepay()){

                //当前机构可用代还通道
                List<VirtualChannelXmlBean> repayChannelList = agentXmlBean.getRepaymentList();
                if(repayChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【智能代还】");
                    return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【智能代还】");

                }

                //渠道商每种类型只配一个虚拟通道，因为渠道商下边的商户在各通道进件的时候费率固定
                //运营商每种类型可配多个虚拟通道，因为运营商下边的商户可以选择不同费率的通道进件
                VirtualChannelXmlBean virtualChannelXmlBean = repayChannelList.get(0);
                String channelCode = virtualChannelXmlBean.getCode();//通道号
                //校验通道是否可用
                ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                    return useCheckResult;
                }

                //进行费率的前置校验
                ResultVO validResult=VirtualChannelValid.repayChannelEnterCheck(merchantBeanOfAddMer,virtualChannelXmlBean);
                if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                    return validResult;
                }

                int channelId 	= virtualChannelXmlBean.getDefault_channel();//通道id
                MerchantChannelBean merchantChannelBean = new MerchantChannelBean();
                merchantChannelBean.setChannelId(channelId);
                merchantChannelBean.setChannelCode(channelCode);
                merchantChannelBean.setCreateTime(DateUtils.getCurrentDateTime());
                merchantChannelBean.setFee0(merchantBeanOfAddMer.getRepayFee0());
                merchantChannelBean.setFee(merchantBeanOfAddMer.getRepayFee());
                merchantChannelBean.setRepayFee(merchantBeanOfAddMer.getRepayFee1());
                merchantChannelBean.setBalanceFee(0);
                merchantChannelBean.setWithholdFee(0);
                merchantChannelBean.setType("2");//智能代还
                merchantChannelBean.setOpenQuick("0");//开通快捷（0未开通、1已开通）
                merchantChannelBean.setUpdateTime(DateUtils.getCurrentDateTime());
                merchantChannelBean.setVirtualChannelId(virtualChannelXmlBean.getId());
                //缺商户id
                //通道方返回第三方商户号、秘钥
                //动态加载通道配置文件
                ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                ResultVO addMemchantChannelResult = channel.addMemchantChannel(agentXmlBean,merchantBeanOfAddMer, merchantChannelBean,null,null,channelXmlBean);
                if("0000".equals(addMemchantChannelResult.getRetCode())){//商户可以在通道方进件
                    repayMerchant = merchantChannelBean;
                }else{
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,addMemchantChannelResult.getRetMsg());
                    return ResultVOUtil.error( addMemchantChannelResult);
                }
            }

            //结算通道
            if(merchantBeanOfAddMer.isUsableBalance()){

                //当前机构可用结算通道
                List<VirtualChannelXmlBean> balanceChannelList = agentXmlBean.getBalaceList();
                if(balanceChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【结算】");
                    return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【结算】");
                }

                //渠道商每种类型只配一个虚拟通道，因为渠道商下边的商户在各通道进件的时候费率固定
                //运营商每种类型可配多个虚拟通道，因为运营商下边的商户可以选择不同费率的通道进件
                VirtualChannelXmlBean virtualChannelXmlBean = balanceChannelList.get(0);
                String channelCode = virtualChannelXmlBean.getCode();//通道号
                //校验通道是否可用
                ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                    return useCheckResult;
                }

                //进行费率的前置校验
                ResultVO validResult=VirtualChannelValid.settleChannelEnterCheck(merchantBeanOfAddMer,virtualChannelXmlBean);
                if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                    return validResult;
                }

                int channelId 	= virtualChannelXmlBean.getDefault_channel();//通道id
                MerchantChannelBean merchantChannelBean = new MerchantChannelBean();
                merchantChannelBean.setChannelId(channelId);
                merchantChannelBean.setChannelCode(channelCode);
                merchantChannelBean.setCreateTime(DateUtils.getCurrentDateTime());
                merchantChannelBean.setFee(0);
                merchantChannelBean.setFee0(new BigDecimal(0));
                merchantChannelBean.setRepayFee(0);
                merchantChannelBean.setBalanceFee(merchantBeanOfAddMer.getBalanceFee());
                merchantChannelBean.setWithholdFee(0);
                merchantChannelBean.setType("3");//结算
                merchantChannelBean.setOpenQuick("0");//开通快捷（0未开通、1已开通）
                merchantChannelBean.setUpdateTime(DateUtils.getCurrentDateTime());
                merchantChannelBean.setVirtualChannelId(virtualChannelXmlBean.getId());
                //缺商户id
                //通道方返回第三方商户号、秘钥
                //动态加载通道配置文件
                ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                ResultVO addMemchantChannelResult = channel.addMemchantChannel(agentXmlBean,merchantBeanOfAddMer, merchantChannelBean,null,null,channelXmlBean);
                if("0000".equals(addMemchantChannelResult.getRetCode())){//商户可以在通道方进件
                    balanceMerchant = merchantChannelBean;
                }else{
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,addMemchantChannelResult.getRetMsg());
                    return ResultVOUtil.error( addMemchantChannelResult);
                }
            }

            //划扣通道
            if(merchantBeanOfAddMer.isUsableDelimit()){

                //当前机构可用划扣通道
                List<VirtualChannelXmlBean> delimitChannelList = agentXmlBean.getDelimitList();
                if(delimitChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【划扣】");
                    return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【划扣】");
                }
                //渠道商每种类型只配一个虚拟通道，因为渠道商下边的商户在各通道进件的时候费率固定
                //运营商每种类型可配多个虚拟通道，因为运营商下边的商户可以选择不同费率的通道进件
                VirtualChannelXmlBean virtualChannelXmlBean = delimitChannelList.get(0);
                String channelCode = virtualChannelXmlBean.getCode();//通道号
                //校验通道是否可用
                ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                    return useCheckResult;
                }

                //进行费率的前置校验
                ResultVO validResult=VirtualChannelValid.delimitChannelEnterCheck(merchantBeanOfAddMer,virtualChannelXmlBean);
                if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                    return validResult;
                }

                int channelId 	= virtualChannelXmlBean.getDefault_channel();//通道id
                MerchantChannelBean merchantChannelBean = new MerchantChannelBean();
                merchantChannelBean.setChannelId(channelId);
                merchantChannelBean.setChannelCode(channelCode);
                merchantChannelBean.setCreateTime(DateUtils.getCurrentDateTime());
                merchantChannelBean.setFee0(merchantBeanOfAddMer.getDelimitFee0());
                merchantChannelBean.setFee(merchantBeanOfAddMer.getDelimitFee());
                merchantChannelBean.setRepayFee(0);
                merchantChannelBean.setBalanceFee(0);
                merchantChannelBean.setWithholdFee(merchantBeanOfAddMer.getWithholdFee());
                merchantChannelBean.setType("5");//划扣
                merchantChannelBean.setOpenQuick("0");//开通快捷（0未开通、1已开通）
                merchantChannelBean.setUpdateTime(DateUtils.getCurrentDateTime());
                merchantChannelBean.setVirtualChannelId(virtualChannelXmlBean.getId());
                //缺商户id
                //通道方返回第三方商户号、秘钥
                //动态加载通道配置文件
                ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                ResultVO addMemchantChannelResult = channel.addMemchantChannel(agentXmlBean,merchantBeanOfAddMer, merchantChannelBean,null,null,channelXmlBean);
                if("0000".equals(addMemchantChannelResult.getRetCode())){//商户可以在通道方进件
                    delimitMerchant = merchantChannelBean;
                }else{
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,addMemchantChannelResult.getRetMsg());
                    return ResultVOUtil.error( addMemchantChannelResult);
                }

            }


        }else{
            //当前机构可用升级通道
            List<VirtualChannelXmlBean> upgradeChannelList = agentXmlBean.getUpgradeList();
            if(upgradeChannelList.size()==0){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【升级】");
                return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【升级】");
            }

        }

        BankCertificationBean bcb = null;
        if(agentXmlBean.getIs_auth()==1){//四要素实名认证
            //四要素鉴权
            BankCertificationBean bankCertificationBean = new BankCertificationBean();
            bankCertificationBean.setBankCardName(merchantBeanOfAddMer.getSettleName());
            bankCertificationBean.setBankCardNo(merchantBeanOfAddMer.getSettleBankCardNo());
            bankCertificationBean.setPhone(merchantBeanOfAddMer.getSettlePhone());
            bankCertificationBean.setIdCard(merchantBeanOfAddMer.getIdCard());
            bcb = bankCertificationDao.getByFourElements(bankCertificationBean);
            if(bcb==null){
                log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"未进行实名认证");
                return ResultVOUtil.error(ResultEnum.NO_AUTH);
            }else{
                if(bcb.getAuthStatus()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"实名信息未通过，需先进行实名认证，未通过原因："+bcb.getAuthMsg());
                    return ResultVOUtil.error(ResultEnum.NO_PASS_AUTH.getCode(),ResultEnum.NO_PASS_AUTH.getMessage()+":"+bcb.getAuthMsg());
                }
            }
        }

        //生成不重复商户号
        Map<String, String> map1 = new HashMap<>();
        map1.put("agent_id", agentXmlBean.getId()+"");
        map1.put("merchantNamePre", config.getOrderPrefix());
        map1.put("num", "8");
        map1 = merchantDao.getMerchantNumber(map1);
        String platformMerchantNumber = map1.get("platformMerchantNumber");
//            System.out.println("调用存储过程后的map:"+map1);
        log.info("【通过存储过程生成的平台商户号】bizType:{}，agentNo：{}，渠道商户号：{}，平台商户号:{}",bizType,agentNo,merchantBeanOfAddMer.getMerNo(),platformMerchantNumber);
        merchantBeanOfAddMer.setPlatformMerNo(platformMerchantNumber);//平台商户号
        merchantBeanOfAddMer.setPlatformMerKey(UUID.randomUUID().toString().trim().replaceAll("-", ""));//平台商户秘钥
        merchantBeanOfAddMer.setCreateTime(DateUtils.getCurrentDateTime());
        merchantBeanOfAddMer.setUpdateTime(DateUtils.getCurrentDateTime());

        //往商户表和商户通道信息表中存入数据
        int merchantId = merchantDao.insertMerchant(agentProperties,merchantBeanOfAddMer);
        if(merchantId>0){
            if(integralMerchant!=null){
                integralMerchant.setMerchantId(merchantId);
                int merchantChannelId = merchantChannelDao.insertMerchantChannel(agentProperties,integralMerchant);
                if(merchantChannelId==0){
                    throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户进件信息保存通道信息表失败[有积分]");
                }
            }

            if(noIntegralMerchant!=null){
                noIntegralMerchant.setMerchantId(merchantId);
                int merchantChannelId = merchantChannelDao.insertMerchantChannel(agentProperties,noIntegralMerchant);
                if(merchantChannelId==0){
                    throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户进件信息保存通道信息表失败[无积分]");
                }
            }

            if(repayMerchant!=null) {
                repayMerchant.setMerchantId(merchantId);
                int merchantChannelId = merchantChannelDao.insertMerchantChannel(agentProperties,repayMerchant);
                if (merchantChannelId == 0) {
                    throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(), "商户进件信息保存通道信息表失败[代还]");
                }
            }


            if(balanceMerchant!=null){
                balanceMerchant.setMerchantId(merchantId);
                int merchantChannelId = merchantChannelDao.insertMerchantChannel(agentProperties,balanceMerchant);
                if(merchantChannelId==0){
                    throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户进件信息保存通道信息表失败[结算]");
                }
            }

            if(delimitMerchant!=null){
                delimitMerchant.setMerchantId(merchantId);
                int merchantChannelId = merchantChannelDao.insertMerchantChannel(agentProperties,delimitMerchant);
                if(merchantChannelId==0){
                    throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户进件信息保存通道信息表失败[划扣]");
                }
            }

            log.info("bizType:{}，agentNo：{}，商户进件成功",bizType,agentNo);
            AddMerResultVO resultData=new AddMerResultVO();
            resultData.setPlatformMerNo(merchantBeanOfAddMer.getPlatformMerNo());
            resultData.setPlatformMerKey(merchantBeanOfAddMer.getPlatformMerKey());
            return ResultVOUtil.success(resultData);

        }else{
            throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户进件信息保存失败");
        }

    }
}

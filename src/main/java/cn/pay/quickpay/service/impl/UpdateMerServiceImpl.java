package cn.pay.quickpay.service.impl;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.BankCertificationBean;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.bean.MerchantChannelBean;
import cn.pay.quickpay.channel.base.ChannelAbstract;
import cn.pay.quickpay.convert.Request2BeanConvert;
import cn.pay.quickpay.dao.IBankCertificationDao;
import cn.pay.quickpay.dao.IMerchantChannelDao;
import cn.pay.quickpay.dao.IMerchantDao;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.exception.QuickPayException;
import cn.pay.quickpay.service.UpdateMerService;
import cn.pay.quickpay.utils.platform.*;
import cn.pay.quickpay.utils.platform.valid.VirtualChannelValid;
import cn.pay.quickpay.valid.QuickpayRequsetValid;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import cn.pay.quickpay.xmlbean.VirtualChannelXmlBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author bmr
 * @time 2019-02-21 11:00
 */
@Slf4j
@Service
public class UpdateMerServiceImpl implements UpdateMerService {

    @Autowired
    private IBankCertificationDao bankCertificationDao;

    @Autowired
    private IMerchantChannelDao merchantChannelDao;

    @Autowired
    private IMerchantDao merchantDao;

    @Autowired
    private GetChannelXmlBeanUtil getChannelXmlBeanUtil;

    @Autowired
    private ChannelUseCheckUtil channelUseCheckUtil;



    @Override
    public ResultVO updateMer(HttpServletRequest request, MerchantBean mb,AgentXmlBean agentXmlBean,Properties agentProperties, Properties platformProperties) {
        String bizType=request.getParameter("bizType");
        String agentNo=request.getParameter("agentNo");

//        Properties agentProperties = new Properties();
//        agentProperties.setProperty("driver", agentXmlBean.getDb_driver());
//        agentProperties.setProperty("url", agentXmlBean.getDb_url());
//        agentProperties.setProperty("username", agentXmlBean.getDb_username());
//        agentProperties.setProperty("password", agentXmlBean.getDb_password());
//
//        Properties platformProperties=PropertiesUtil.readProperties("jdbc.properties");

        //校验必填参数
        ResultVO updateMerParamVaildResult=QuickpayRequsetValid.updateMerParamVaild(request);
        if(!updateMerParamVaildResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
            log.info("【必填参数为空】bizType:{}，agentNo：{}，错误信息：{}", bizType, agentNo,updateMerParamVaildResult.getRetMsg());
            return ResultVOUtil.error(updateMerParamVaildResult);
        }


        MerchantBean merchantBean = Request2BeanConvert.convertToMerchantBean(request);
        log.info("【商户信息修改下游传递数据】:{}",merchantBean);

        merchantBean.setId(mb.getId());
        merchantBean.setMerAddress(mb.getMerAddress());
        if("1".equals(merchantBean.getMerFlag())){//修改结算卡。费率读取商户的

            BankCertificationBean bcb = null;
            if(agentXmlBean.getIs_auth()==1){//鉴权
                //运营商要做四要素鉴权
                BankCertificationBean bankCertificationBean = new BankCertificationBean();
                bankCertificationBean.setBankCardName(merchantBean.getSettleName());
                bankCertificationBean.setBankCardNo(merchantBean.getSettleBankCardNo());
                bankCertificationBean.setPhone(merchantBean.getSettlePhone());
                bankCertificationBean.setIdCard(merchantBean.getIdCard());
                bcb = bankCertificationDao.getByFourElements(bankCertificationBean);
                if(bcb==null){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}", bizType, agentNo,"未进行实名认证");
                    return ResultVOUtil.error(ResultEnum.NO_AUTH);
                }else{
                    if(bcb.getAuthStatus()==0){
                        log.info("bizType:{}，agentNo：{}，错误信息：{}", bizType, agentNo,"请先进行实名认证:"+bcb.getAuthMsg());
                        return ResultVOUtil.error(ResultEnum.NO_PASS_AUTH.getCode(),"请先进行实名认证:"+bcb.getAuthMsg());
                    }
                }
            }

            merchantBean.setD0fee(mb.getD0fee());
            merchantBean.setFee0(mb.getFee0());
            merchantBean.setRepayFee(mb.getRepayFee());
            merchantBean.setRepayFee0(mb.getRepayFee0());
            merchantBean.setRepayFee1(mb.getRepayFee1());
            merchantBean.setNoIntegralFee(mb.getNoIntegralFee());
            merchantBean.setNoIntegralFee0(mb.getNoIntegralFee0());
            merchantBean.setDelimitFee0(mb.getDelimitFee0());
            merchantBean.setDelimitFee(mb.getDelimitFee());
            merchantBean.setWithholdFee(mb.getWithholdFee());
            merchantBean.setBalanceFee(mb.getBalanceFee());

            //有积分费率
            //-1为小于、0为等于、1为大于
            if(merchantBean.getFee0().compareTo(new BigDecimal(0))==1){//费率为0表示未开通此类支付通道
                merchantBean.setUsableIntegral(true);
                log.info("商户修改快捷有积分通道结算卡信息");
            }

            //无积分费率
            //-1为小于、0为等于、1为大于
            if(merchantBean.getNoIntegralFee0().compareTo(new BigDecimal(0))==1){//费率为0表示未开通此类支付通道
                merchantBean.setUsableNoIntegral(true);
                log.info("商户修改快捷无积分通道结算卡信息");
            }

            //划扣费率
            //-1为小于、0为等于、1为大于
            if(merchantBean.getDelimitFee0().compareTo(new BigDecimal(0))==1){//费率为0表示未开通此类支付通道
                merchantBean.setUsableDelimit(true);
                log.info("商户修改划扣通道结算卡信息");
            }

            //智能代还费率
            //-1为小于、0为等于、1为大于
            if(merchantBean.getRepayFee0().compareTo(new BigDecimal(0))==1){//费率为0表示未开通此类支付通道
                merchantBean.setUsableRepay(true);
                log.info("商户修改智能代还通道结算卡信息");
            }

            //结算
            if(merchantBean.getBalanceFee()>0){
                merchantBean.setUsableBalance(true);
                log.info("商户修改结算通道结算卡信息");
            }

        }else if("2".equals(merchantBean.getMerFlag())){//修改费率。结算卡信息读取商户的
            merchantBean.setSettleBank(mb.getSettleBank());
            merchantBean.setSettleBankBranch(mb.getSettleBankBranch());
            merchantBean.setSettleBankCardNo(mb.getSettleBankCardNo());
            merchantBean.setSettleBankNo(mb.getSettleBankNo());
            merchantBean.setSettleBankSub(mb.getSettleBankSub());
            merchantBean.setSettleName(mb.getSettleName());
            merchantBean.setSettlePhone(mb.getSettlePhone());
            merchantBean.setSettleSubCity(mb.getSettleSubCity());
            merchantBean.setSettleSubProvince(mb.getSettleSubProvince());
            merchantBean.setSettleType(mb.getSettleType());
            merchantBean.setIdCard(mb.getIdCard());
            merchantBean.setMerAddress(mb.getMerAddress());
            merchantBean.setMerNo(mb.getMerNo());
            merchantBean.setMerName(mb.getMerName());
        }


        if(merchantBean.isUsableIntegral()||merchantBean.isUsableNoIntegral()||merchantBean.isUsableRepay()||merchantBean.isUsableBalance()||merchantBean.isUsableDelimit()){

            //根据平台商户号，查询商户入驻的所有通道
            List<MerchantChannelBean> merchantChannelList = merchantChannelDao.getByMerchantId(agentProperties, mb.getId());
            List<MerchantChannelBean> integralList = new ArrayList<>();
            List<MerchantChannelBean> noIntegralList = new ArrayList<>();
            List<MerchantChannelBean> delimitList = new ArrayList<>();
            List<MerchantChannelBean> repayList = new ArrayList<>();
            List<MerchantChannelBean> balanceList = new ArrayList<>();
            Set<Integer> integralChannelIds = new HashSet<>();
            Set<Integer> noIntegralChannelIds = new HashSet<>();
            Set<Integer> repayChannelIds = new HashSet<>();
            Set<Integer> balanceChannelIds = new HashSet<>();
            Set<Integer> delimitChannelIds = new HashSet<>();
            if(merchantChannelList.size()>0){
                for (MerchantChannelBean merchantChannelBean : merchantChannelList) {
                    //类型（0有积分、1无积分、2智能代还、3结算、4升级、5划扣）
                    if("0".equals(merchantChannelBean.getType())){
                        integralList.add(merchantChannelBean);
                        integralChannelIds.add(merchantChannelBean.getChannelId());
                    }else if("1".equals(merchantChannelBean.getType())){
                        noIntegralList.add(merchantChannelBean);
                        noIntegralChannelIds.add(merchantChannelBean.getChannelId());
                    }else if("2".equals(merchantChannelBean.getType())){
                        repayList.add(merchantChannelBean);
                        repayChannelIds.add(merchantChannelBean.getChannelId());
                    }else if("3".equals(merchantChannelBean.getType())){
                        balanceList.add(merchantChannelBean);
                        balanceChannelIds.add(merchantChannelBean.getChannelId());
                    }else if("5".equals(merchantChannelBean.getType())){
                        delimitList.add(merchantChannelBean);
                        delimitChannelIds.add(merchantChannelBean.getChannelId());
                    }
                }
            }


            if(merchantBean.isUsableIntegral()){//有积分通道
                //当前机构可用有积分通道
                List<VirtualChannelXmlBean> integralChannelList = agentXmlBean.getIntegralChannelList();
                if(integralChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【有积分】");
                    return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【有积分】");

                }
                VirtualChannelXmlBean virtualChannelXmlBean = integralChannelList.get(0);
                if("1".equals(merchantBean.getMerFlag())){//修改结算卡

                }else if("2".equals(merchantBean.getMerFlag())){//修改费率
                    //进行费率的前置校验
                    ResultVO validResult=VirtualChannelValid.integralChannelEnterCheck(merchantBean,virtualChannelXmlBean);
                    if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                        return validResult;
                    }
                }

                if(!integralChannelIds.contains(virtualChannelXmlBean.getDefault_channel())){//不存在新增

                    int channelId = virtualChannelXmlBean.getDefault_channel();
                    String channelCode = virtualChannelXmlBean.getCode();
                    //校验通道是否可用
                    ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                    if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                        return useCheckResult;
                    }


                    MerchantChannelBean merchantChannelBean = new MerchantChannelBean();
                    merchantChannelBean.setChannelId(channelId);
                    merchantChannelBean.setChannelCode(channelCode);
                    merchantChannelBean.setCreateTime(DateUtils.getCurrentDateTime());
                    merchantChannelBean.setFee0(merchantBean.getFee0());
                    merchantChannelBean.setFee(merchantBean.getD0fee());
                    merchantChannelBean.setRepayFee(0);
                    merchantChannelBean.setBalanceFee(0);
                    merchantChannelBean.setWithholdFee(0);
                    merchantChannelBean.setType("0");//有积分
                    merchantChannelBean.setOpenQuick("0");//开通快捷（0未开通、1已开通）
                    merchantChannelBean.setUpdateTime(DateUtils.getCurrentDateTime());
                    merchantChannelBean.setMerchantId(mb.getId());
                    merchantChannelBean.setVirtualChannelId(virtualChannelXmlBean.getId());
                    //缺商户id
                    //通道方返回第三方商户号、秘钥
                    //动态加载通道配置文件
                    ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                    ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                    ResultVO addMemchantChannelResult = channel.addMemchantChannel(agentXmlBean,merchantBean, merchantChannelBean,platformProperties,agentProperties,channelXmlBean);
                    if(ResultEnum.SUCCESS.getCode().equals(addMemchantChannelResult.getRetCode())){//商户可以在通道方进件
                        int count = merchantChannelDao.insertMerchantChannel(agentProperties, merchantChannelBean);
                        if(count==0){
                            throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户进件信息保存通道信息表失败[有积分]");
                        }
                    }else{
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,addMemchantChannelResult.getRetMsg());
                        return ResultVOUtil.error( addMemchantChannelResult);
                    }
                }

                if(integralList.size()>0){//存在的修改
                    for (MerchantChannelBean mcb : integralList) {
                        String channelCode = mcb.getChannelCode();
                        //校验通道是否可用
                        ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                        if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                            return useCheckResult;
                        }

                        ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                        ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                        mcb.setFee(merchantBean.getD0fee());
                        mcb.setFee0(merchantBean.getFee0());
                        mcb.setUpdateTime(DateUtils.getCurrentDateTime());
                        ResultVO updateMemResult = channel.updateMemChannel(merchantBean, mcb,channelXmlBean, agentXmlBean, agentProperties, null);

                        if(ResultEnum.SUCCESS.getCode().equals(updateMemResult.getRetCode())){//商户通道方修改进件信息成功，在数据库中进行修改信息
                            int count = merchantChannelDao.updateMerchantChannel(agentProperties, mcb);
                            if(count==0){
                                throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户修改进件信息失败（通道进件信息表）[有积分]");
                            }
                        }else{
                            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,updateMemResult.getRetMsg());
                            return updateMemResult;
                        }
                    }
                }



            }
            if(merchantBean.isUsableNoIntegral()){//无积分通道
                //当前机构可用有积分通道
                List<VirtualChannelXmlBean> noIntegralChannelList = agentXmlBean.getChannelList();
                if(noIntegralChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【无积分】");
                    return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【无积分】");

                }
                VirtualChannelXmlBean virtualChannelXmlBean = noIntegralChannelList.get(0);

                if("1".equals(merchantBean.getMerFlag())){//修改结算卡

                }else if("2".equals(merchantBean.getMerFlag())){//修改费率
                    //进行费率的前置校验
                    ResultVO validResult=VirtualChannelValid.noIntegralChannelEnterCheck(merchantBean,virtualChannelXmlBean);
                    if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                        return validResult;
                    }

                }

                if(!noIntegralChannelIds.contains(virtualChannelXmlBean.getDefault_channel())){//不存在新增
                    String channelCode = virtualChannelXmlBean.getCode();
                    int channelId 	= virtualChannelXmlBean.getDefault_channel();
                    //校验通道是否可用
                    ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                    if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                        return useCheckResult;
                    }



                    MerchantChannelBean merchantChannelBean = new MerchantChannelBean();
                    merchantChannelBean.setChannelId(channelId);
                    merchantChannelBean.setChannelCode(channelCode);
                    merchantChannelBean.setCreateTime(DateUtils.getCurrentDateTime());
                    merchantChannelBean.setFee0(merchantBean.getNoIntegralFee0());
                    merchantChannelBean.setFee(merchantBean.getNoIntegralFee());
                    merchantChannelBean.setRepayFee(0);
                    merchantChannelBean.setBalanceFee(0);
                    merchantChannelBean.setWithholdFee(0);
                    merchantChannelBean.setType("1");//无积分
                    merchantChannelBean.setOpenQuick("0");//开通快捷（0未开通、1已开通）
                    merchantChannelBean.setUpdateTime(DateUtils.getCurrentDateTime());
                    merchantChannelBean.setMerchantId(mb.getId());
                    merchantChannelBean.setVirtualChannelId(virtualChannelXmlBean.getId());
                    //缺商户id
                    //通道方返回第三方商户号、秘钥
                    //动态加载通道配置文件
                    ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                    ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                    ResultVO addMemchantChannelResult = channel.addMemchantChannel(agentXmlBean,merchantBean, merchantChannelBean, platformProperties,agentProperties,channelXmlBean);
                    if(ResultEnum.SUCCESS.getCode().equals(addMemchantChannelResult.getRetCode())){//商在通道方进件成功，往数据库中记录信息
                        int count = merchantChannelDao.insertMerchantChannel(agentProperties, merchantChannelBean);
                        if(count==0){
                            throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户进件信息保存通道信息表失败[无积分]");
                        }
                    }else{
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,addMemchantChannelResult.getRetMsg());
                        return ResultVOUtil.error( addMemchantChannelResult);
                    }
                }

                if(noIntegralList.size()>0){//存在的修改
                    for (MerchantChannelBean mcb : noIntegralList) {
                        String channelCode = mcb.getChannelCode();
                        ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                        if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                            return useCheckResult;
                        }
                        ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                        ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                        mcb.setFee0(merchantBean.getNoIntegralFee0());
                        mcb.setFee(merchantBean.getNoIntegralFee());
                        mcb.setUpdateTime(DateUtils.getCurrentDateTime());
                        ResultVO  updateMemResult= channel.updateMemChannel(merchantBean, mcb,channelXmlBean, agentXmlBean, agentProperties, platformProperties);

                        if(ResultEnum.SUCCESS.getCode().equals(updateMemResult.getRetCode())){//商户通道方修改进件信息成功，在数据库中进行修改信息
                            int count = merchantChannelDao.updateMerchantChannel(agentProperties, mcb);
                            if(count==0){
                                throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户修改进件信息失败（通道进件信息表）[无积分]");
                            }
                        }else{
                            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,updateMemResult.getRetMsg());
                            return updateMemResult;
                        }

                    }
                }

            }


            if(merchantBean.isUsableDelimit()){//划扣通道
                //当前机构可用划扣通道
                List<VirtualChannelXmlBean> delimitChannelList = agentXmlBean.getDelimitList();
                if(delimitChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【划扣】");
                    return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【划扣】");
                }
                VirtualChannelXmlBean virtualChannelXmlBean = delimitChannelList.get(0);

                if("1".equals(merchantBean.getMerFlag())){//修改结算卡

                }else if("2".equals(merchantBean.getMerFlag())){//修改费率
                    //进行费率的前置校验
                    ResultVO validResult=VirtualChannelValid.delimitChannelEnterCheck(merchantBean,virtualChannelXmlBean);
                    if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                        return validResult;
                    }

                }

                if(!delimitChannelIds.contains(virtualChannelXmlBean.getDefault_channel())){//不存在新增
                    String channelCode = virtualChannelXmlBean.getCode();
                    int channelId 	= virtualChannelXmlBean.getDefault_channel();
                    //校验通道是否可用
                    ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                    if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                        return useCheckResult;
                    }


                    MerchantChannelBean merchantChannelBean = new MerchantChannelBean();
                    merchantChannelBean.setChannelId(channelId);
                    merchantChannelBean.setChannelCode(channelCode);
                    merchantChannelBean.setCreateTime(DateUtils.getCurrentDateTime());
                    merchantChannelBean.setFee0(merchantBean.getDelimitFee0());
                    merchantChannelBean.setFee(merchantBean.getDelimitFee());
                    merchantChannelBean.setWithholdFee(merchantBean.getWithholdFee());
                    merchantChannelBean.setRepayFee(0);
                    merchantChannelBean.setBalanceFee(0);
                    merchantChannelBean.setType("5");//划扣
                    merchantChannelBean.setOpenQuick("0");//开通快捷（0未开通、1已开通）
                    merchantChannelBean.setUpdateTime(DateUtils.getCurrentDateTime());
                    merchantChannelBean.setMerchantId(mb.getId());
                    merchantChannelBean.setVirtualChannelId(virtualChannelXmlBean.getId());
                    //缺商户id
                    //通道方返回第三方商户号、秘钥
                    //动态加载通道配置文件
                    ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                    ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                    ResultVO  addMemchantChannelResult= channel.addMemchantChannel(agentXmlBean,merchantBean, merchantChannelBean, platformProperties,agentProperties,channelXmlBean);
                    if(ResultEnum.SUCCESS.getCode().equals(addMemchantChannelResult.getRetCode())){//商在通道方进件成功，往数据库中记录信息
                        int count = merchantChannelDao.insertMerchantChannel(agentProperties, merchantChannelBean);
                        if(count==0){
                            throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户进件信息保存通道信息表失败[划扣]");
                        }
                    }else{
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,addMemchantChannelResult.getRetMsg());
                        return ResultVOUtil.error( addMemchantChannelResult);
                    }

                }
                if(delimitList.size()>0){//存在的修改
                    for (MerchantChannelBean mcb : delimitList) {
                        String channelCode = mcb.getChannelCode();
                        ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                        if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                            return useCheckResult;
                        }
                        ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                        ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                        mcb.setFee0(merchantBean.getDelimitFee0());
                        mcb.setFee(merchantBean.getDelimitFee());
                        mcb.setWithholdFee(merchantBean.getWithholdFee());
                        mcb.setUpdateTime(DateUtils.getCurrentDateTime());
                        ResultVO  updateMemResult= channel.updateMemChannel(merchantBean, mcb,channelXmlBean, agentXmlBean, agentProperties, platformProperties);

                        if(ResultEnum.SUCCESS.getCode().equals(updateMemResult.getRetCode())){//商户通道方修改进件信息成功，在数据库中进行修改信息
                            int count = merchantChannelDao.updateMerchantChannel(agentProperties, mcb);
                            if(count==0){
                                throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户修改进件信息失败（通道进件信息表）[划扣]");
                            }
                        }else{
                            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,updateMemResult.getRetMsg());
                            return updateMemResult;
                        }

                    }
                }

            }

            if(merchantBean.isUsableRepay()){//智能代还通道
                //当前机构可用代还通道
                List<VirtualChannelXmlBean> repayChannelList = agentXmlBean.getRepaymentList();
                if(repayChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【智能代还】");
                    return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【智能代还】");
                }
                //渠道商每种类型只配一个虚拟通道，因为渠道商下边的商户在各通道进件的时候费率固定
                //运营商每种类型可配多个虚拟通道，因为运营商下边的商户可以选择不同费率的通道进件
                VirtualChannelXmlBean virtualChannelXmlBean = repayChannelList.get(0);

                if("1".equals(merchantBean.getMerFlag())){//修改结算卡

                }else if("2".equals(merchantBean.getMerFlag())){//修改费率
                    //进行费率的前置校验
                    ResultVO validResult=VirtualChannelValid.repayChannelEnterCheck(merchantBean,virtualChannelXmlBean);
                    if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                        return validResult;
                    }

                }

                if(!repayChannelIds.contains(virtualChannelXmlBean.getDefault_channel())){//不存在新增

                    String channelCode = virtualChannelXmlBean.getCode();
                    int channelId 	= virtualChannelXmlBean.getDefault_channel();
                    //校验通道是否可用
                    ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                    if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                        return useCheckResult;
                    }


                    MerchantChannelBean merchantChannelBean = new MerchantChannelBean();
                    merchantChannelBean.setChannelId(channelId);
                    merchantChannelBean.setChannelCode(channelCode);
                    merchantChannelBean.setCreateTime(DateUtils.getCurrentDateTime());
                    merchantChannelBean.setFee0(merchantBean.getRepayFee0());
                    merchantChannelBean.setFee(merchantBean.getRepayFee());
                    merchantChannelBean.setRepayFee(merchantBean.getRepayFee1());
                    merchantChannelBean.setWithholdFee(0);
                    merchantChannelBean.setBalanceFee(0);
                    merchantChannelBean.setType("2");//智能代还
                    merchantChannelBean.setOpenQuick("0");//开通快捷（0未开通、1已开通）
                    merchantChannelBean.setUpdateTime(DateUtils.getCurrentDateTime());
                    merchantChannelBean.setMerchantId(mb.getId());
                    merchantChannelBean.setVirtualChannelId(virtualChannelXmlBean.getId());

                    //缺商户id
                    //通道方返回第三方商户号、秘钥
                    //动态加载通道配置文件
                    ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                    ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                    ResultVO  addMemchantChannelResult= channel.addMemchantChannel(agentXmlBean,merchantBean, merchantChannelBean, platformProperties,agentProperties,channelXmlBean);
                    if(ResultEnum.SUCCESS.getCode().equals(addMemchantChannelResult.getRetCode())){//商在通道方进件成功，往数据库中记录信息
                        int count = merchantChannelDao.insertMerchantChannel(agentProperties, merchantChannelBean);
                        if(count==0){
                            throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户进件信息保存通道信息表失败[智能代还]");
                        }
                    }else{
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,addMemchantChannelResult.getRetMsg());
                        return ResultVOUtil.error( addMemchantChannelResult);
                    }

                }

                if(repayList.size()>0){//存在的修改
                    for (MerchantChannelBean mcb : repayList) {
                        String channelCode = mcb.getChannelCode();
                        ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                        if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                            return useCheckResult;
                        }
                        ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                        ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);

                        mcb.setFee0(merchantBean.getRepayFee0());
                        mcb.setFee(merchantBean.getRepayFee());
                        mcb.setRepayFee(merchantBean.getRepayFee1());
                        mcb.setUpdateTime(DateUtils.getCurrentDateTime());
                        ResultVO  updateMemResult= channel.updateMemChannel(merchantBean, mcb,channelXmlBean, agentXmlBean, agentProperties, platformProperties);

                        if(ResultEnum.SUCCESS.getCode().equals(updateMemResult.getRetCode())){//商户通道方修改进件信息成功，在数据库中进行修改信息
                            int count = merchantChannelDao.updateMerchantChannel(agentProperties, mcb);
                            if(count==0){
                                throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户修改进件信息失败（通道进件信息表）[智能代还]");
                            }
                        }else{
                            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,updateMemResult.getRetMsg());
                            return updateMemResult;
                        }
                    }
                }


            }

            if(merchantBean.isUsableBalance()){//结算通道
                //当前机构可用代还通道
                List<VirtualChannelXmlBean> balanceChannelList = agentXmlBean.getBalaceList();
                if(balanceChannelList.size()==0){
                    log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【结算】");
                    return ResultVOUtil.error(ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getCode(),ResultEnum.AGENT_NOT_SUPPORT_OPEN_CHANNEL.getMessage()+"【结算】");

                }
                //渠道商每种类型只配一个虚拟通道，因为渠道商下边的商户在各通道进件的时候费率固定
                //运营商每种类型可配多个虚拟通道，因为运营商下边的商户可以选择不同费率的通道进件
                VirtualChannelXmlBean virtualChannelXmlBean = balanceChannelList.get(0);

                if("1".equals(merchantBean.getMerFlag())){//修改结算卡

                }else if("2".equals(merchantBean.getMerFlag())){//修改费率
                    //进行费率的前置校验
                    ResultVO validResult=VirtualChannelValid.settleChannelEnterCheck(merchantBean,virtualChannelXmlBean);
                    if(!validResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,validResult.getRetMsg());
                        return validResult;
                    }

                }

                if(!balanceChannelIds.contains(virtualChannelXmlBean.getDefault_channel())){//不存在新增

                    String channelCode = virtualChannelXmlBean.getCode();
                    //校验通道是否可用
                    ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                    if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                        return useCheckResult;
                    }

                    int channelId 	= virtualChannelXmlBean.getDefault_channel();
                    MerchantChannelBean merchantChannelBean = new MerchantChannelBean();
                    merchantChannelBean.setChannelId(channelId);
                    merchantChannelBean.setChannelCode(channelCode);
                    merchantChannelBean.setCreateTime(DateUtils.getCurrentDateTime());
                    merchantChannelBean.setFee0(new BigDecimal(0));
                    merchantChannelBean.setFee(0);
                    merchantChannelBean.setRepayFee(0);
                    merchantChannelBean.setBalanceFee(merchantBean.getBalanceFee());
                    merchantChannelBean.setWithholdFee(0);
                    merchantChannelBean.setType("3");//结算
                    merchantChannelBean.setOpenQuick("0");//开通快捷（0未开通、1已开通）
                    merchantChannelBean.setUpdateTime(DateUtils.getCurrentDateTime());
                    merchantChannelBean.setMerchantId(mb.getId());
                    merchantChannelBean.setVirtualChannelId(virtualChannelXmlBean.getId());

                    //缺商户id
                    //通道方返回第三方商户号、秘钥
                    //动态加载通道配置文件
                    ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                    ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                    ResultVO  addMemchantChannelResult= channel.addMemchantChannel(agentXmlBean,merchantBean, merchantChannelBean, platformProperties,agentProperties,channelXmlBean);
                    if(ResultEnum.SUCCESS.getCode().equals(addMemchantChannelResult.getRetCode())){//商在通道方进件成功，往数据库中记录信息
                        int count = merchantChannelDao.insertMerchantChannel(agentProperties, merchantChannelBean);
                        if(count==0){
                            throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户进件信息保存通道信息表失败[结算]");
                        }
                    }else{
                        log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,addMemchantChannelResult.getRetMsg());
                        return ResultVOUtil.error( addMemchantChannelResult);
                    }

                }

                if(balanceList.size()>0){//存在的修改
                    for (MerchantChannelBean mcb : balanceList) {
                        String channelCode = mcb.getChannelCode();
                        ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
                        if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
                            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
                            return useCheckResult;
                        }
                        ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
                        ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
                        mcb.setBalanceFee(merchantBean.getBalanceFee());
                        mcb.setUpdateTime(DateUtils.getCurrentDateTime());
                        ResultVO  updateMemResult= channel.updateMemChannel(merchantBean, mcb,channelXmlBean, agentXmlBean, agentProperties, platformProperties);

                        if(ResultEnum.SUCCESS.getCode().equals(updateMemResult.getRetCode())){//商户通道方修改进件信息成功，在数据库中进行修改信息
                            int count = merchantChannelDao.updateMerchantChannel(agentProperties, mcb);
                            if(count==0){
                                throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户修改进件信息失败（通道进件信息表）[结算]");
                            }
                        }else{
                            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,updateMemResult.getRetMsg());
                            return updateMemResult;
                        }

                    }
                }



            }

        }else{
            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,"未输入任何一项有效费率");
            return ResultVOUtil.error( ResultEnum.PARAM_ERROR.getCode(),"请输入正确的费率，有积分、无积分、智能代还、结算、划扣必填一项");
        }

        //将各通道入驻信息表数据修改成功后，再修改商户表中的信息
        if("1".equals(merchantBean.getMerFlag())){//修改结算卡
            int count = merchantDao.updateSettle(agentProperties, merchantBean);
            if(count==0){
                throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户修改结算卡信息保存失败");
            }

        }else if("2".equals(merchantBean.getMerFlag())){//修改费率
            int count = merchantDao.updateFee(agentProperties, merchantBean);
            if(count==0){
                throw new QuickPayException(ResultEnum.SYSTEM_ERROR.getCode(),"商户修改费率信息保存失败");
            }
        }

       return ResultVOUtil.success();

    }
}

package cn.pay.quickpay.service.impl;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.bean.MerchantCardBean;
import cn.pay.quickpay.bean.MerchantChannelBean;
import cn.pay.quickpay.channel.base.ChannelAbstract;
import cn.pay.quickpay.convert.Request2BeanConvert;
import cn.pay.quickpay.dao.IMerchantChannelDao;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.service.OpenCardCodeService;
import cn.pay.quickpay.utils.platform.ChannelUseCheckUtil;
import cn.pay.quickpay.utils.platform.GetChannelXmlBeanUtil;
import cn.pay.quickpay.utils.platform.ResultVOUtil;
import cn.pay.quickpay.valid.QuickpayRequsetValid;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Properties;

/**
 * @author bmr
 * @time 2019-02-22 11:27
 */
@Slf4j
@Service
public class OpenCardCodeServiceImpl implements OpenCardCodeService {

    @Autowired
    private IMerchantChannelDao merchantChannelDao;

    @Autowired
    private ChannelUseCheckUtil channelUseCheckUtil;

    @Autowired
    private GetChannelXmlBeanUtil getChannelXmlBeanUtil;

    @Override
    public ResultVO openCardCode(HttpServletRequest request, HttpServletResponse response, MerchantBean mb, AgentXmlBean agentXmlBean, Properties agentProperties, Properties platformProperties) {

        String bizType=request.getParameter("bizType");
        String agentNo=request.getParameter("agentNo");

        //必填参数校验
        ResultVO openCardCodeParamVaildResult=QuickpayRequsetValid.openCardCodeParamVaild(request);
        if(openCardCodeParamVaildResult != null){
            log.info("【必填参数为空】bizType:{}，agentNo：{}，错误信息：{}", bizType, agentNo,openCardCodeParamVaildResult.getRetMsg());
            return ResultVOUtil.error(openCardCodeParamVaildResult);
        }

        MerchantCardBean merchantCardBean   = Request2BeanConvert.convertMerchantCardBean(request);
        merchantCardBean.setAgentId(agentXmlBean.getId());

        merchantCardBean.setMerchantId(mb.getId());
        List<MerchantChannelBean> list = null;
        if(merchantCardBean.getType()==1){//划扣绑卡
            list = this.merchantChannelDao.getByType(agentProperties, mb.getId(), "5");
        }else{
            list = this.merchantChannelDao.getByMerchantId(agentProperties, mb.getId());
        }

        if(list.size()==0){
            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage());
            return ResultVOUtil.error(ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getCode(),ResultEnum.AGENT_HAVE_NULL_USED_CHANNEL.getMessage());

        }
        MerchantChannelBean merchantChannelBean = list.get(0);

        merchantCardBean.setChannelId(merchantChannelBean.getChannelId());
        String channelCode = merchantChannelBean.getChannelCode();
        //校验通道是否可用
        ResultVO useCheckResult=channelUseCheckUtil.useCheck(channelCode);
        if(!useCheckResult.getRetCode().equals(ResultEnum.SUCCESS.getCode())){
            log.info("bizType:{}，agentNo：{}，错误信息：{}",bizType,agentNo,useCheckResult.getRetMsg());
            return useCheckResult;
        }
        ChannelXmlBean channelXmlBean=(ChannelXmlBean) useCheckResult.getData();
        ChannelAbstract channel = getChannelXmlBeanUtil.getChannelAbstract(channelXmlBean);
        ResultVO openCardCodeResult= channel.openCardCode(agentProperties,merchantCardBean, merchantChannelBean, mb, agentXmlBean, response);

        return openCardCodeResult;
    }
}

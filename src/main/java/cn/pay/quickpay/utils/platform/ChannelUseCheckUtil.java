package cn.pay.quickpay.utils.platform;

import cn.pay.quickpay.VO.ResultVO;
import cn.pay.quickpay.enums.ResultEnum;
import cn.pay.quickpay.exception.QuickPayException;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author bmr
 * @time 2019-02-21 11:51
 * 校验通道是否为可用状态
 */
@Component
public class ChannelUseCheckUtil {
    @Autowired
    private GetChannelXmlBeanUtil getChannelXmlBeanUtil;

    public ResultVO useCheck(String channelCode){
        if(StringUtils.isEmpty(channelCode)){
            throw new QuickPayException(ResultEnum.GET_CHANNEL_CODE_ERROR);
        }

        ChannelXmlBean channelXmlBean = getChannelXmlBeanUtil.getChannelXml(channelCode);
        if(channelXmlBean==null){
            throw new QuickPayException(ResultEnum.CHANNEL_NOT_EXIST.getCode(),ResultEnum.CHANNEL_NOT_EXIST.getMessage()+"[通道号:"+channelCode+"]");
        }

        if(channelXmlBean.getIs_open()==0){
            return ResultVOUtil.error(ResultEnum.CHANNEL_CANNOT_USE.getCode(),ResultEnum.CHANNEL_CANNOT_USE.getMessage()+"[通道号:"+channelCode+"]");
        }

        return ResultVOUtil.success(channelXmlBean);
    }
}

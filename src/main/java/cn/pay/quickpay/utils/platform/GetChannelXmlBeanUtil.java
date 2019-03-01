package cn.pay.quickpay.utils.platform;

import cn.pay.quickpay.channel.base.AuthChannelAbstract;
import cn.pay.quickpay.channel.base.ChannelAbstract;
import cn.pay.quickpay.config.CoreConfig;
import cn.pay.quickpay.utils.platform.JaxbObjectAndXmlUtil;
import cn.pay.quickpay.xmlbean.AgentXmlBean;
import cn.pay.quickpay.xmlbean.AuthChannelXmlBean;
import cn.pay.quickpay.xmlbean.ChannelXmlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author bmr
 * @time 2019-02-15 8:41
 */
@Component
public class GetChannelXmlBeanUtil {

    @Autowired
    private CoreConfig config;

    /**
     * 获取渠道商配置文件
     * @param agentNo
     * @return
     */
    public  AgentXmlBean getAgentXml(String agentNo){
        String agentFileName = config.getAgentFilePath()+"/"+agentNo+".xml";
//        System.out.println("渠道商配置文件路径为："+agentFileName);
        AgentXmlBean agentXmlBean = JaxbObjectAndXmlUtil.xml2Object(JaxbObjectAndXmlUtil.readFileByBytes(agentFileName), AgentXmlBean.class);
        return agentXmlBean;
    }

    /**
     * 获取通道配置文件
     * @param channelCode
     * @return
     */
    public ChannelXmlBean getChannelXml(String channelCode){
        String channelFileName = config.getChannelFilePath()+"/"+channelCode+".xml";
        ChannelXmlBean channelXmlBean = JaxbObjectAndXmlUtil.xml2Object(JaxbObjectAndXmlUtil.readFileByBytes(channelFileName), ChannelXmlBean.class);
        return channelXmlBean;
    }

    /**
     * 获取鉴权通道配置文件
     * @param channelCode
     * @return
     */
    public AuthChannelXmlBean getAuthChannelXml(String channelCode){
        String authChannelFileName = config.getAuthChannelFilePath()+"/"+channelCode+".xml";
        AuthChannelXmlBean authChannelXmlBean = JaxbObjectAndXmlUtil.xml2Object(JaxbObjectAndXmlUtil.readFileByBytes(authChannelFileName), AuthChannelXmlBean.class);
        return authChannelXmlBean;
    }

    /**
     * 获取通道抽象类
     * @param channelXmlBean
     * @return
     */
    public ChannelAbstract getChannelAbstract(ChannelXmlBean channelXmlBean){
        String[] coreXml = new String[] { "file:"  + "./channel/quick/"+channelXmlBean.getChannel_sign()+".xml" };
        FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(coreXml);
        ctx.registerShutdownHook();
        ChannelAbstract channel = (ChannelAbstract)ctx.getBean("channel");
        return channel;
    }

    /**
     * 获取项目中channel文件夹下的通道基础配置文件
     * @param channelCode
     * @return
     */
    public AuthChannelAbstract getAuthChannelAbstract(String channelCode){
        String[] coreXml = new String[] { "file:"  + "./channel/auth/"+channelCode+".xml" };
        FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(coreXml);
        ctx.registerShutdownHook();
        AuthChannelAbstract authChannel = (AuthChannelAbstract)ctx.getBean("channel");
        return authChannel;
    }


}

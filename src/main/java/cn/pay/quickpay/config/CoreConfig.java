package cn.pay.quickpay.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author bmr
 * @time 2019-02-15 9:42
 */
//@PropertySource({"file:./config.properties"})
//@ConfigurationProperties(prefix = "coreConfig")
@Data
//@Component
public class CoreConfig {

//        @Value("${agentFilePath}")
    private String agentFilePath;//渠道配置文件路径

//        @Value("${channelFilePath}")
    private String channelFilePath;//通道配置文件路径

//        @Value("${authChannelFilePath}")
    private String authChannelFilePath;//鉴权通道配置文件路径

//        @Value("${loanChannelFilePath}")
    private String loanChannelFilePath;//贷款通道配置文件路径

//        @Value("${agreementChannelFilePath}")
    private String agreementChannelFilePath;//电子合同通道配置文件路径

//        @Value("${imageFilePath}")
    private String imageFilePath;//图片服务器存放路径

//        @Value("${contractFilePath}")
    private String contractFilePath;//合同服务器存放路径

//        @Value("${fileHostUrl}")
    private String fileHostUrl;//文件服务器地址

//        @Value("${notifyHostUrl}")
    private String notifyHostUrl;//接受通道异步通知的服务器域名地址

//        @Value("${orderPrefix}")
    private String orderPrefix;//生成平台唯一订单号时的前缀

//        @Value("${production}")
    private boolean production;//是否为生产环境  true生产环境  false开发环境

//        @Value("${port}")
    private  int port;//port 监听的端口号




}

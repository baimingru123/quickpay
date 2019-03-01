package cn.pay.quickpay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ImportResource({"file:./Quickpay-core.xml"})
//@MapperScan("cn.pay.quickpay.dao")
public class QuickpayApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickpayApplication.class, args);
    }

}


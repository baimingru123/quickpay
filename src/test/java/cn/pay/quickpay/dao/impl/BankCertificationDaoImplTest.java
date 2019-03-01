package cn.pay.quickpay.dao.impl;

import cn.pay.quickpay.bean.BankCertificationBean;
import cn.pay.quickpay.dao.IBankCertificationDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author bmr
 * @time 2019-02-21 10:11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BankCertificationDaoImplTest {

    @Autowired
    private IBankCertificationDao dao;

    @Test
    public void insertBankCertification() {
        BankCertificationBean bean=new BankCertificationBean();
        bean.setBankCardName("小白");
        bean.setBankCardNo("12354455");
        bean.setIdCard("122336554");
        bean.setPhone("18754190709");
        int id=dao.insertBankCertification(bean);
        System.out.println("id:"+id);
    }

    @Test
    public void getByBankCardNo() {
    }

    @Test
    public void getByFourElements() {
    }
}
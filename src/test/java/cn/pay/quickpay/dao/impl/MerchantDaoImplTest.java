package cn.pay.quickpay.dao.impl;

import cn.pay.quickpay.dao.IMerchantDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * @author bmr
 * @time 2019-02-25 9:22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MerchantDaoImplTest {
    @Autowired
    private IMerchantDao merchantDao;

    @Test
    public void insertMerchant() {

    }
}
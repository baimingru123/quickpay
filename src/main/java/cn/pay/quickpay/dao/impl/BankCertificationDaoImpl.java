package cn.pay.quickpay.dao.impl;


import cn.pay.quickpay.bean.BankCertificationBean;
import cn.pay.quickpay.dao.IBankCertificationDao;
import cn.pay.quickpay.utils.platform.MyBatisCoreUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 
 * 创建日期 2018-1-29 下午3:29:09   
 * @author 闪付时代 zll
 *
 */
@Component
public class BankCertificationDaoImpl implements IBankCertificationDao {

	private static Logger logger = LoggerFactory.getLogger(BankCertificationDaoImpl.class);
	
	@Override
	public int insertBankCertification(BankCertificationBean bean) {
		int id = 0;
		//获得会话对象
        SqlSession session= MyBatisCoreUtil.getSqlSession(true);
        try {     
        	logger.info("【银行卡实名认证数据插入】{}",bean);
        	IBankCertificationDao dao = session.getMapper(IBankCertificationDao.class);
        	dao.insertBankCertification(bean);
//        	session.commit();
            id = bean.getId();
        }catch (Exception e) {
        	logger.info("error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return id;
	}
	@Override
	public BankCertificationBean getByBankCardNo(String bankCardNo) {
		//获得会话对象
        SqlSession session= MyBatisCoreUtil.getSqlSession(true);
        try {          
        	IBankCertificationDao dao = session.getMapper(IBankCertificationDao.class);
            return dao.getByBankCardNo(bankCardNo);
        } finally {
            session.close();
        }
	}
	
	@Override
	public BankCertificationBean getByFourElements(BankCertificationBean bean) {
		//获得会话对象
        SqlSession session= MyBatisCoreUtil.getSqlSession(true);
        try {          
        	IBankCertificationDao dao = session.getMapper(IBankCertificationDao.class);
            return dao.getByFourElements(bean);
        } finally {
            session.close();
        }
	}
}

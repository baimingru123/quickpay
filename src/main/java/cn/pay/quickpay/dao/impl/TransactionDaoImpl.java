package cn.pay.quickpay.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.pay.quickpay.bean.TransactionBean;
import cn.pay.quickpay.config.CoreConfig;
import cn.pay.quickpay.dao.ITransactionDao;
import cn.pay.quickpay.utils.platform.MyBatisCoreUtil;
import cn.pay.quickpay.utils.platform.MyBatisSubUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 
 * 创建日期 2018-2-1 上午10:52:02   
 * @author 闪付时代 zll
 *
 */
@Component
public class TransactionDaoImpl implements ITransactionDao {

	@Autowired
	private CoreConfig config;

	private static Logger logger = LoggerFactory.getLogger(TransactionDaoImpl.class);

	
	@Override
	public TransactionBean getById(Properties agentProperties, int id) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);
            return dao.getById(agentProperties,id);
        } finally {
            session.close();
        }
	}
	
	@Override
	public List<TransactionBean> getByPlatformOrderNumberList(
			Properties agentProperties, String sql) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);
            return dao.getByPlatformOrderNumberList(agentProperties,sql);
        } finally {
            session.close();
        }
	}
	
	@Override
	public TransactionBean getByOrder(Properties agentProperties,
			TransactionBean bean) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);
//        	logger.info("【根据商户订单号查询订单】"+bean);
            return dao.getByOrder(agentProperties,bean);
        } finally {
            session.close();
        }
	}
	
	@Override
	public TransactionBean getByRepayIds(Properties agentProperties,
			String repayIds) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);
            return dao.getByRepayIds(agentProperties,repayIds);
        } finally {
            session.close();
        }
	}
	
	@Override
	public TransactionBean getByPlatformOrderNumber(Properties properties,String platformOrderNumber) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(properties);
        try {          
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);
            return dao.getByPlatformOrderNumber(properties,platformOrderNumber);
        } finally {
            session.close();
        }
	}
	
	@Override
	public TransactionBean getByOrderNumber(Properties properties,String orderNumber) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(properties);
        try {          
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);
            return dao.getByOrderNumber(properties,orderNumber);
        } finally {
            session.close();
        }
	}
	
	@Override
	public Map<String, String> getOrderNumber(Map<String, String> map) {
		
		SqlSession session= MyBatisCoreUtil.getSqlSession(true);
		try {  
			ITransactionDao dao = session.getMapper(ITransactionDao.class);  
	        map.put("orderNamePre", config.getOrderPrefix());
	        map.put("num", "14");
	        dao.getOrderNumber(map);
			return map;
		}catch (Exception e) {//订单号重复无法入库
        	logger.info("订单号重复无法入库 error：{}",e.getMessage());
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);  
	        map.put("orderNamePre", config.getOrderPrefix());
	        map.put("num", "14");
	        dao.getOrderNumber(map);
			return map;
		} finally {
            session.close();
        }
	}
	
	@Override
	public int updateTransactionStatus(Properties agentProperties,
			TransactionBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("【更新订单状态】{}",bean);
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);
        	dao.updateTransactionStatus(agentProperties,bean);
        	session.commit();
        	count = 1;
        }catch (Exception e) {
        	logger.info("error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return count;
	}
	
	@Override
	public int updateContractSignStatus(Properties agentProperties,
			TransactionBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新{}",bean);
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);
        	dao.updateContractSignStatus(agentProperties,bean);
        	session.commit();
        	count = 1;
        }catch (Exception e) {
        	logger.info("error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return count;
	}
	
	@Override
	public int updateTransactionCallback(Properties agentProperties,TransactionBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新{}",bean);
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);
        	dao.updateTransactionCallback(agentProperties,bean);
        	session.commit();
        	count = 1;
        }catch (Exception e) {
        	logger.info("error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return count;
	}
	
	@Override
	public int updateTransaction(Properties agentProperties,TransactionBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新{}",bean);
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);
        	dao.updateTransaction(agentProperties,bean);
        	session.commit();
        	count = 1;
        }catch (Exception e) {
        	logger.info("error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return count;
	}
	
	@Override
	public int insertTransaction(Properties agentProperties,TransactionBean bean) {
		int id = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("【插入transaction表信息】{}",bean);
        	ITransactionDao dao = session.getMapper(ITransactionDao.class);
        	dao.insertTransaction(agentProperties,bean);
        	session.commit();
            id = bean.getId();
        }catch (Exception e) {
        	logger.info("【插入transaction表信息异常】{}",e.getMessage());
		} finally {
            session.close();
        }
        return id;
	}
}

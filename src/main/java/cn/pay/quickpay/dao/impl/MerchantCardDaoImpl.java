package cn.pay.quickpay.dao.impl;

import java.util.Map;
import java.util.Properties;

import cn.pay.quickpay.bean.MerchantCardBean;
import cn.pay.quickpay.config.CoreConfig;
import cn.pay.quickpay.dao.IMerchantCardDao;
import cn.pay.quickpay.utils.platform.MyBatisCoreUtil;
import cn.pay.quickpay.utils.platform.MyBatisSubUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 创建日期 2018-1-29 下午3:29:09   
 * @author 闪付时代 zll
 *
 */
@Component
public class MerchantCardDaoImpl implements IMerchantCardDao {

	@Autowired
	private CoreConfig config;

	private static Logger logger = LoggerFactory.getLogger(MerchantCardDaoImpl.class);

	
	@Override
	public int updatePlatformOutTradeNo(Properties agentProperties,
			MerchantCardBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新{}",bean);
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
        	dao.updatePlatformOutTradeNo(agentProperties,bean);
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
	public int updatePlatformOutTradeNoAndBindCode(Properties agentProperties,
			MerchantCardBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新平台绑卡交易号和通道返回的绑卡申请流水号 {}",bean);
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
        	dao.updatePlatformOutTradeNoAndBindCode(agentProperties,bean);
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
	public int updateMerchantCardDel(Properties agentProperties,MerchantCardBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新{}",bean);
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
        	dao.updateMerchantCardDel(agentProperties,bean);
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
	public int updateBindState(Properties agentProperties, MerchantCardBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新{}",bean);
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
        	dao.updateBindState(agentProperties,bean);
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
	public int updateMerchantCard(Properties agentProperties,MerchantCardBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新{}",bean);
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
        	dao.updateMerchantCard(agentProperties,bean);
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
	public int updateAuth(Properties agentProperties, MerchantCardBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新{}",bean);
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
        	dao.updateAuth(agentProperties,bean);
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
	public int updateCardInfo(Properties agentProperties, MerchantCardBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新信用卡基本信息{}",bean);
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
        	dao.updateCardInfo(agentProperties,bean);
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
	public int updateRate(Properties agentProperties, MerchantCardBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新{}",bean);
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
        	dao.updateRate(agentProperties,bean);
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
	public Map<String, String> getMerchantCardNumber(Map<String, String> map) {
		SqlSession session= MyBatisCoreUtil.getSqlSession(true);
		try {  
			IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);  
	        map.put("orderNamePre", config.getOrderPrefix());
	        map.put("num", "8");
	        dao.getMerchantCardNumber(map);
			return map;
		}catch (Exception e) {//订单号重复无法入库
        	logger.info("订单号重复无法入库 error：{}",e.getMessage());
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);  
	        map.put("orderNamePre", config.getOrderPrefix());
	        map.put("num", "8");
	        dao.getMerchantCardNumber(map);
			return map;
		} finally {
            session.close();
        }
	}
	
	@Override
	public MerchantCardBean getByInfo(Properties agentProperties,
			int merchantId,String bankCardNo,int channelId) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
            return dao.getByInfo(agentProperties,merchantId,bankCardNo,channelId);
        } finally {
            session.close();
        }
	}
	
	@Override
	public MerchantCardBean getByPlatformOutTradeNo(Properties agentProperties,
			int merchantId, String platformOutTradeNo, int channelId) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
            return dao.getByPlatformOutTradeNo(agentProperties,merchantId,platformOutTradeNo,channelId);
        } finally {
            session.close();
        }
	}
	
	@Override
	public MerchantCardBean getByOrderNo(Properties agentProperties,MerchantCardBean bean) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
            return dao.getByOrderNo(agentProperties,bean);
        } finally {
            session.close();
        }
	}
	
	@Override
	public int insertMerchantCard(Properties agentProperties,MerchantCardBean bean) {
		int id = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("插入{}",bean);
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
        	dao.insertMerchantCard(agentProperties,bean);
        	session.commit();
            id = bean.getId();
        }catch (Exception e) {
        	logger.info("error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return id;
	}
	
	@Override
	public MerchantCardBean getByPlatformOrderNumber(Properties properties,
			String platformOrderNumber) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(properties);
        try {          
        	IMerchantCardDao dao = session.getMapper(IMerchantCardDao.class);
            return dao.getByPlatformOrderNumber(properties,platformOrderNumber);
        } finally {
            session.close();
        }
	}


	
	
}

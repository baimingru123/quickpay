package cn.pay.quickpay.dao.impl;

import java.util.Map;
import java.util.Properties;

import cn.pay.quickpay.bean.MerchantBean;
import cn.pay.quickpay.dao.IMerchantDao;
import cn.pay.quickpay.utils.platform.MyBatisCoreUtil;
import cn.pay.quickpay.utils.platform.MyBatisSubUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 
 * 创建日期 2018-1-29 下午3:29:09
 * 
 * @author 闪付时代 zll
 *
 */
@Component
public class MerchantDaoImpl implements IMerchantDao {

	private static Logger logger = LoggerFactory.getLogger(MerchantDaoImpl.class);

	@Override
	public MerchantBean getById(Properties properties, int id) {
		// 获得会话对象
		SqlSession session = MyBatisSubUtil.getSqlSession(properties);
		try {
			IMerchantDao dao = session.getMapper(IMerchantDao.class);
			return dao.getById(properties, id);
		} finally {
			session.close();
		}
	}

	@Override
	public Map<String, String> getMerchantNumber(Map<String, String> map) {
		SqlSession session = MyBatisCoreUtil.getSqlSession(true);
		try {
			IMerchantDao dao = session.getMapper(IMerchantDao.class);
			map.put("merchantNamePre", "10");
			map.put("num", "8");
			dao.getMerchantNumber(map);
			return map;
		}catch (Exception e) {//订单号重复无法入库
        	logger.info("订单号重复无法入库 error：{}",e.getMessage());
        	IMerchantDao dao = session.getMapper(IMerchantDao.class);
			map.put("merchantNamePre", "10");
			map.put("num", "8");
			dao.getMerchantNumber(map);
			return map;
		}  finally {
			session.close();
		}
	}

	@Override
	public MerchantBean getMerchantByNo(Properties properties, String merNo) {
		// 获得会话对象
		SqlSession session = MyBatisSubUtil.getSqlSession(properties);
		try {
			IMerchantDao dao = session.getMapper(IMerchantDao.class);
			return dao.getMerchantByNo(properties, merNo);
		} finally {
			session.close();
		}
	}

	@Override
	public MerchantBean getByPlatformMerNo(Properties properties, String platformMerNo) {
		// 获得会话对象
		SqlSession session = MyBatisSubUtil.getSqlSession(properties);
		try {
			IMerchantDao dao = session.getMapper(IMerchantDao.class);
			return dao.getByPlatformMerNo(properties, platformMerNo);
		} finally {
			session.close();
		}
	}

	@Override
	public int insertMerchant(Properties agentProperties, MerchantBean bean) {
		int id = 0;
		// 获得会话对象
		SqlSession session = MyBatisSubUtil.getSqlSession(agentProperties);
		try {
			logger.info("【merchant表插入数据】{}", bean);
			IMerchantDao dao = session.getMapper(IMerchantDao.class);
			dao.insertMerchant(agentProperties, bean);
			session.commit();
			id = bean.getId();
		} catch (Exception e) {
			logger.info("【merchant表插入数据异常】error：{}", e.getMessage());
		} finally {
			session.close();
		}
		return id;
	}

	@Override
	public int deleteMerchant(Properties agentProperties, MerchantBean bean) {
		int count = 0;
		// 获得会话对象
		SqlSession session = MyBatisSubUtil.getSqlSession(agentProperties);
		try {
			logger.info("删除{}", bean);
			IMerchantDao dao = session.getMapper(IMerchantDao.class);
			dao.deleteMerchant(agentProperties, bean);
			session.commit();
			count = 1;
		} catch (Exception e) {
			logger.info("error：{}", e.getMessage());
		} finally {
			session.close();
		}
		return count;
	}

	@Override
	public int updateFee(Properties agentProperties, MerchantBean bean) {
		int count = 0;
		// 获得会话对象
		SqlSession session = MyBatisSubUtil.getSqlSession(agentProperties);
		try {
			logger.info("【更新商户信息表】{}", bean);
			IMerchantDao dao = session.getMapper(IMerchantDao.class);
			dao.updateFee(agentProperties, bean);
			session.commit();
			count = 1;
		} catch (Exception e) {
			logger.info("error：{}", e.getMessage());
		} finally {
			session.close();
		}
		return count;
	}

	@Override
	public int updateSettle(Properties agentProperties, MerchantBean bean) {
		int count = 0;
		// 获得会话对象
		SqlSession session = MyBatisSubUtil.getSqlSession(agentProperties);
		try {
			logger.info("更新{}", bean);
			IMerchantDao dao = session.getMapper(IMerchantDao.class);
			dao.updateSettle(agentProperties, bean);
			session.commit();
			count = 1;
		} catch (Exception e) {
			logger.info("error：{}", e.getMessage());
		} finally {
			session.close();
		}
		return count;
	}

	@Override
	public int updateOpenQuick(Properties agentProperties, MerchantBean bean) {
		int count = 0;
		// 获得会话对象
		SqlSession session = MyBatisSubUtil.getSqlSession(agentProperties);
		try {
			logger.info("更新{}", bean);
			IMerchantDao dao = session.getMapper(IMerchantDao.class);
			dao.updateOpenQuick(agentProperties, bean);
			session.commit();
			count = 1;
		} catch (Exception e) {
			logger.info("error：{}", e.getMessage());
		} finally {
			session.close();
		}
		return count;
	}

	@Override
	public int updateMerchant(Properties agentProperties, MerchantBean bean) {
		int count = 0;
		// 获得会话对象
		SqlSession session = MyBatisSubUtil.getSqlSession(agentProperties);
		try {
			logger.info("更新{}", bean);
			IMerchantDao dao = session.getMapper(IMerchantDao.class);
			dao.updateMerchant(agentProperties, bean);
			session.commit();
			count = 1;
		} catch (Exception e) {
			logger.info("error：{}", e.getMessage());
		} finally {
			session.close();
		}
		return count;
	}
}

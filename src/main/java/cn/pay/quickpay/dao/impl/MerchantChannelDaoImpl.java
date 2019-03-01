package cn.pay.quickpay.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.pay.quickpay.bean.MerchantChannelBean;
import cn.pay.quickpay.dao.IMerchantChannelDao;
import cn.pay.quickpay.utils.platform.MyBatisCoreUtil;
import cn.pay.quickpay.utils.platform.MyBatisSubUtil;
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
public class MerchantChannelDaoImpl implements IMerchantChannelDao {

	private static Logger logger = LoggerFactory.getLogger(MerchantChannelDaoImpl.class);
	
	
	@Override
	public int insertMerchantChannel(Properties agentProperties,MerchantChannelBean bean) {
		int id = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("【merchantChannel表插入数据】{}",bean.toString());
        	IMerchantChannelDao dao = session.getMapper(IMerchantChannelDao.class);
        	dao.insertMerchantChannel(agentProperties,bean);
        	session.commit();
            id = bean.getId();
        }catch (Exception e) {
        	logger.info("【merchantChannel表插入数据异常】：{}",e.getMessage());
		} finally {
            session.close();
        }
        return id;
	}
	
	@Override
	public int updateMerchantChannel(Properties agentProperties,MerchantChannelBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("【更新商户入驻通道信息表】{}",bean);
        	IMerchantChannelDao dao = session.getMapper(IMerchantChannelDao.class);
        	dao.updateMerchantChannel(agentProperties,bean);
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
	public int updateOpenQuickStatus(Properties agentProperties,MerchantChannelBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新{}",bean);
        	IMerchantChannelDao dao = session.getMapper(IMerchantChannelDao.class);
        	count=dao.updateOpenQuickStatus(agentProperties,bean);
        	session.commit();
        }catch (Exception e) {
        	logger.info("error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return count;
	}
	
	@Override
	public int updateSettleCardAndBindId(Properties agentProperties,MerchantChannelBean bean) {
		int count = 0;
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {     
        	logger.info("更新结算卡卡号和绑卡id{}",bean);
        	IMerchantChannelDao dao = session.getMapper(IMerchantChannelDao.class);
        	count=dao.updateSettleCardAndBindId(agentProperties,bean);
        	session.commit();
        }catch (Exception e) {
        	logger.info("error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return count;
	}
	
	@Override
	public MerchantChannelBean getById(Properties agentProperties, int id) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	IMerchantChannelDao dao = session.getMapper(IMerchantChannelDao.class);
            return dao.getById(agentProperties,id);
        } finally {
            session.close();
        }
	}
	
	@Override
	public List<MerchantChannelBean> getByType(Properties agentProperties,
			int merchantId, String type) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	IMerchantChannelDao dao = session.getMapper(IMerchantChannelDao.class);
            return dao.getByType(agentProperties,merchantId,type);
        } finally {
            session.close();
        }
	}
	
	@Override
	public List<MerchantChannelBean> getByMerchantId(
			Properties agentProperties, int merchantId) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	IMerchantChannelDao dao = session.getMapper(IMerchantChannelDao.class);
            return dao.getByMerchantId(agentProperties,merchantId);
        } finally {
            session.close();
        }
	}
	
	@Override
	public MerchantChannelBean getByVirtualChannelId(
			Properties agentProperties, int merchantId, int virtualChannelId) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	IMerchantChannelDao dao = session.getMapper(IMerchantChannelDao.class);
            return dao.getByVirtualChannelId(agentProperties,merchantId,virtualChannelId);
        } finally {
            session.close();
        }
	}
	
	@Override
	public MerchantChannelBean getByChannelId(Properties agentProperties,
			int merchantId, String type, int channelId) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	IMerchantChannelDao dao = session.getMapper(IMerchantChannelDao.class);
            return dao.getByChannelId(agentProperties,merchantId,type,channelId);
        } finally {
            session.close();
        }
	}
	
	@Override
	public MerchantChannelBean getByChannelIdThirdMerNo(Properties agentProperties,String thirdMerNo, int channelId) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	IMerchantChannelDao dao = session.getMapper(IMerchantChannelDao.class);
            return dao.getByChannelIdThirdMerNo(agentProperties,thirdMerNo,channelId);
        } finally {
            session.close();
        }
	}
	
	@Override
	public MerchantChannelBean getByExt(Properties agentProperties,String ext) {
		//获得会话对象
        SqlSession session= MyBatisSubUtil.getSqlSession(agentProperties);
        try {          
        	IMerchantChannelDao dao = session.getMapper(IMerchantChannelDao.class);
            return dao.getByExt(agentProperties, ext);
        } finally {
            session.close();
        }
	}
	
	
}

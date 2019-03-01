package cn.pay.quickpay.dao.impl;



import java.util.Properties;

import cn.pay.quickpay.bean.AgentBean;
import cn.pay.quickpay.dao.IAgentDao;
import cn.pay.quickpay.utils.platform.MyBatisCoreUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;


/**
 * 
 * 创建日期 2018-1-29 下午3:29:09   
 * @author 闪付时代 zll
 *
 */
@Slf4j
@Component
public class AgentDaoImpl implements IAgentDao {

	
	@Override
	public AgentBean getById(int id) {
		//获得会话对象
        SqlSession session= MyBatisCoreUtil.getSqlSession(true);
        try {          
        	IAgentDao dao = session.getMapper(IAgentDao.class);
            return dao.getById(id);
        } finally {
            session.close();
        }
	}
	
	@Override
	public int updateAgent(AgentBean bean) {
		int count = 0;
		//获得会话对象
		 SqlSession session= MyBatisCoreUtil.getSqlSession(true);
        try {     
        	log.info("【更新代理商数据】{}",bean);
        	IAgentDao dao = session.getMapper(IAgentDao.class);
        	count=dao.updateAgent(bean);
        }catch (Exception e) {
        	log.info("【更新代理商数据】error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return count;
	}
	
	@Override
	public int updateAuthBalance(AgentBean bean) {
		int count = 0;
		//获得会话对象
		 SqlSession session= MyBatisCoreUtil.getSqlSession(true);
        try {     
        	log.info("【更新代理商鉴权费用或者实名认证费用余额】{}",bean);
        	IAgentDao dao = session.getMapper(IAgentDao.class);
        	count=dao.updateAuthBalance(bean);
        }catch (Exception e) {
        	log.info("【更新代理商鉴权费用或者实名认证费用余额】error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return count;
	}
	
	@Override
	public int updateContractBalance(AgentBean bean) {
		int count = 0;
		//获得会话对象
		 SqlSession session= MyBatisCoreUtil.getSqlSession(true);
        try {     
        	log.info("【更新代理商电子合同余额】{}",bean);
        	IAgentDao dao = session.getMapper(IAgentDao.class);
			count = dao.updateContractBalance(bean);
        }catch (Exception e) {
        	log.info("【更新代理商电子合同余额】error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return count;
	}
	
	@Override
	public int updateAgentBalance(AgentBean bean) {
		int count = 0;
		//获得会话对象
		 SqlSession session= MyBatisCoreUtil.getSqlSession(true);
        try {     
        	log.info("【更新代理商代付余额】{}",bean);
        	IAgentDao dao = session.getMapper(IAgentDao.class);
        	count=dao.updateAgentBalance(bean);
        }catch (Exception e) {
        	log.info("【更新代理商代付余额】error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return count;
	}
	
	@Override
	public int updateAgentBalanceAdd(AgentBean bean) {
		int count = 0;
		//获得会话对象
		 SqlSession session= MyBatisCoreUtil.getSqlSession(true);
        try {     
        	log.info("【代理商代付余额充值】{}",bean);
        	IAgentDao dao = session.getMapper(IAgentDao.class);
        	count=dao.updateAgentBalanceAdd(bean);
        }catch (Exception e) {
        	log.info("【代理商代付余额充值】error：{}",e.getMessage());
		} finally {
            session.close();
        }
        return count;
	}
	
}

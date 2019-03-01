package cn.pay.quickpay.dao.impl;


import cn.pay.quickpay.bean.BankCardBinBean;
import cn.pay.quickpay.dao.IBankCardBinDao;
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
public class BankCardbinDaoImpl implements IBankCardBinDao {

	private static Logger logger = LoggerFactory.getLogger(BankCardbinDaoImpl.class);
	
	@Override
	public BankCardBinBean getByCardBin(String cardBin, String cardLen) {
		//获得会话对象
        SqlSession session= MyBatisCoreUtil.getSqlSession(true);
        try {          
        	IBankCardBinDao dao = session.getMapper(IBankCardBinDao.class);
            return dao.getByCardBin(cardBin,cardLen);
        } finally {
            session.close();
        }
	}
}

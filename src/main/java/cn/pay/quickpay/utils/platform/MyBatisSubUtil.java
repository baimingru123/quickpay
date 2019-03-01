package cn.pay.quickpay.utils.platform;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

 
 
public class MyBatisSubUtil {
	
	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;

 
    /**
     * 获取SqlSessionFactory
     * @return SqlSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory(Properties properties) {
	    	try {
				//通过配置文件初始化sqlSessionFactory
	    		reader = Resources.getResourceAsReader("mybatis-config.xml");
//	    		Properties properties = new Properties();
//	    		properties.setProperty("driver", "com.mysql.jdbc.Driver");
//	    		properties.setProperty("url", "jdbc:mysql://47.104.184.194:3306/paydb?useSSL=false&characterEncoding=utf8");
//	    		properties.setProperty("username", "root");
//	    		properties.setProperty("password", "Mab2d30co");
	    		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader,properties);
//				sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	return sqlSessionFactory;
    }
 
    /**
     * 获取SqlSession
     * @param properties 数据库属性文件
     * @return SqlSession
     */
    public static SqlSession getSqlSession(Properties properties) {
        return getSqlSessionFactory(properties).openSession();
    }
 
    /**
     * 获取SqlSession
     * @param isAutoCommit 
     *         true 表示创建的SqlSession对象在执行完SQL之后会自动提交事务
     *         false 表示创建的SqlSession对象在执行完SQL之后不会自动提交事务，这时就需要我们手动调用sqlSession.commit()提交事务
     * @return SqlSession
     */
    public static SqlSession getSqlSession(Properties properties,boolean isAutoCommit) {
        return getSqlSessionFactory(properties).openSession(isAutoCommit);
    }
    
    public static void main(String[] args) {

	}
}

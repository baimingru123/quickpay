package cn.pay.quickpay.utils.platform;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * redis工具类
 * 创建日期 2018-3-7 上午9:14:56   
 * @author 闪付时代 zll
 *
 */
public class JedisUtil {
	private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);
    private static String JEDIS_IP;
    private static int JEDIS_PORT;
    private static String JEDIS_PASSWORD;
    private static JedisPool jedisPool;
    /**
     * 初始化线程池
     */
    static {
    	Properties pro = new Properties();
    
		try {
			pro.load(JedisUtil.class.getResourceAsStream("/redis.properties"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
    	
    	
        JEDIS_IP=pro.getProperty("ip");
        JEDIS_PORT=Integer.valueOf(pro.getProperty("port"));
        JEDIS_PASSWORD=pro.getProperty("password");
        
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(pro.getProperty("maxActive")));
        config.setMaxIdle(Integer.valueOf(pro.getProperty("maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(pro.getProperty("maxWait")));
        config.setTestOnBorrow(Boolean.valueOf(pro.getProperty("testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(pro.getProperty("testOnReturn")));
        config.setTestWhileIdle(Boolean.valueOf(pro.getProperty("testWhileIdle")));
        config.setMinEvictableIdleTimeMillis(Long.valueOf(pro.getProperty("minEvictableIdleTimeMillis")));
        config.setTimeBetweenEvictionRunsMillis(Long.valueOf(pro.getProperty("timeBetweenEvictionRunsMillis")));
        config.setNumTestsPerEvictionRun(Integer.valueOf(pro.getProperty("numTestsPerEvictionRun")));
//        jedisPool=new JedisPool(config,JEDIS_IP,JEDIS_PORT,60000);
        jedisPool = new JedisPool(config, JEDIS_IP, JEDIS_PORT, Integer.valueOf(pro.getProperty("timeout")), JEDIS_PASSWORD);
    }
    
    /**
     * 获取连接
     */
    public static synchronized  Jedis getJedis()
    {
        try
        {
            if(jedisPool != null)
            {
                return jedisPool.getResource();
            }
            else
            {
                return null;
            }
        }
        catch (Exception e) {
            System.out.println("连接池连接异常");
            return null;
        }
        
    }
    
    public static void main(String[] args) throws JsonProcessingException {
    	set("test1".getBytes(), "hahha".getBytes());
    	
	}
    
    /**
     * 获取数据
     * @param key
     * @return
     */
    public static String get(String key){
        String value=null;
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            value=jedis.get(key);
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        }finally {
            close(jedis);
        }
        return value;
    }

    private static void close(Jedis jedis) {
        try{
            jedisPool.returnResource(jedis);
        }catch (Exception e){
            if(jedis.isConnected()){
                jedis.quit();
                jedis.disconnect();
            }
        }
    }
    public static byte[] get(byte[] key){
        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }

        return value;
    }
    
    /**
     * 
     * @param key	键
     * @param value	值
     * @param seconds	失效时间，单位秒
     */
    public static void set(String key, String value,int seconds) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }

    public static void set(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }

    public static void set(byte[] key, byte[] value, int time) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, time);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }

    public static void hset(byte[] key, byte[] field, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }

    public static void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static String hget(String key, String field) {

        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }

        return value;
    }
    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static byte[] hget(byte[] key, byte[] field) {

        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }

        return value;
    }
    public static void hdel(byte[] key, byte[] field) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hdel(key, field);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }
    /**
     * 存储REDIS队列 顺序存储
     * @param  key reids键名
     * @param  value 键值
     */
    public static void lpush(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lpush(key, value);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }

    /**
     * 存储REDIS队列 反向存储
     * @param  key reids键名
     * @param  value 键值
     */
    public static void rpush(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {

            jedis = jedisPool.getResource();
            jedis.rpush(key, value);

        } catch (Exception e) {

            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {

            //返还到连接池
            close(jedis);

        }
    }
    
    
    /**
     * 获取队列中的部分值并且对取出的值进行移除
     * @param key  要取出的key
     * @param start	开始索引(取出的值包含此索引的值)
     * @param end	结束索引(取出的值包含此索引的值)
     * @return
     */
    public static List<String> getListMultValueAfterDel(String key,int start, int end){
		List<Object> list = null;
		List<String> listStr = new ArrayList<String>();
		Jedis jedis = null;
		try {		
			jedis = jedisPool.getResource();
			Transaction ts = jedis.multi();
			ts.lrange(key, start, end);
			
			if(start ==0 && end == -1){
				ts.del(key);
			}else if(start !=0 && end == -1){
				ts.ltrim(key, 0, start-1);
			}else{
				ts.ltrim(key, end+1, -1);
			}
			list = ts.exec();
		} catch (Exception e) {
			System.out.println(e);
		}finally{
			//释放资源
			jedis.close();
		}
		
		if(list != null && !list.isEmpty()){
			try {
				//获得命令lrange(key, start, end)的返回结果
				listStr =  (ArrayList<String>)list.get(0);	
			} catch (Exception e) {
				System.out.println(e);
			}			
		} else {
			System.out.println("list为空");
			return Collections.emptyList();
		}
		return listStr;
	}

    /**
     * 获取所有的key列表
     * @param pattern
     */
    public static Set<String> keysPattern(String pattern){
    	Jedis jedis = null;
    	Set<String> result=null;
    	try{
    		jedis=jedisPool.getResource();
    		result=jedis.keys(pattern);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if(jedis != null)
    			jedis.close();
    	}
    	return result;
    	
    } 

    /**
     * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
     * @param  key reids键名
     * @param  destination 键值
     */
    public static void rpoplpush(byte[] key, byte[] destination) {

        Jedis jedis = null;
        try {

            jedis = jedisPool.getResource();
            jedis.rpoplpush(key, destination);

        } catch (Exception e) {

            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {

            //返还到连接池
            close(jedis);

        }
    }

    /**
     * 获取队列数据
     * @param  key 键名
     * @return
     */
    public static List lpopList(byte[] key) {

        List list = null;
        Jedis jedis = null;
        try {

            jedis = jedisPool.getResource();
            list = jedis.lrange(key, 0, -1);

        } catch (Exception e) {

            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {

            //返还到连接池
            close(jedis);

        }
        return list;
    }
    /**
     * 获取队列数据
     * @param  key 键名
     * @return
     */
    public static byte[] rpop(byte[] key) {

        byte[] bytes = null;
        Jedis jedis = null;
        try {

            jedis = jedisPool.getResource();
            bytes = jedis.rpop(key);

        } catch (Exception e) {

            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {

            //返还到连接池
            close(jedis);

        }
        return bytes;
    }
    public static void hmset(Object key, Map hash) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hmset(key.toString(), hash);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            //返还到连接池
            close(jedis);

        }
    }
    public static void hmset(Object key, Map hash, int time) {
        Jedis jedis = null;
        try {

            jedis = jedisPool.getResource();
            jedis.hmset(key.toString(), hash);
            jedis.expire(key.toString(), time);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            //返还到连接池
            close(jedis);

        }
    }
    public static List hmget(Object key, String... fields) {
        List result = null;
        Jedis jedis = null;
        try {

            jedis = jedisPool.getResource();
            result = jedis.hmget(key.toString(), fields);

        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            //返还到连接池
            close(jedis);

        }
        return result;
    }

    public static Set hkeys(String key) {
        Set result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hkeys(key);

        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            //返还到连接池
            close(jedis);

        }
        return result;
    }
    public static List lrange(byte[] key, int from, int to) {
        List result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lrange(key, from, to);

        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            //返还到连接池
            close(jedis);

        }
        return result;
    }
    public static Map hgetAll(byte[] key) {
        Map result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hgetAll(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            //返还到连接池
            close(jedis);
        }
        return result;
    }

    public static void del(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }

    public static long llen(byte[] key) {

        long len = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.llen(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
        return len;
    }
}

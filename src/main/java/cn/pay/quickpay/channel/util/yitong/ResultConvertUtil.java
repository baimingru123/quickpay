package cn.pay.quickpay.channel.util.yitong;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author bmr
 * 创建时间：2019-1-15 上午10:36:0151
 * 对返回来的key=value&key=value类型的参数进行转换
 */

public class ResultConvertUtil {
	private static Logger logger = LoggerFactory.getLogger(ResultConvertUtil.class);
	
	/**
	 * 将key=value&key=value类型的参数转换为map
	 * @return
	 */
	public static Map<String, String> convert2map(String str){
		
		if(str==null || "".equals(str)){
			return null;
		}
		
		Map<String, String> resMap=new HashMap<String, String>();
		String[] data  = str.split("&");
		 for (int i = 0; i < data.length; i++) {
	            String tmp[] = data[i].split("=", 2);
	            resMap.put(tmp[0],tmp[1]);
	        }
		 return resMap;


	}
}

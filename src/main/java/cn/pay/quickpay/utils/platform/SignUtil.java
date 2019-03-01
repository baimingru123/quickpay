package cn.pay.quickpay.utils.platform;

import java.net.URLEncoder;
import java.security.Key;
import java.security.SecureRandom;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import cn.pay.quickpay.channel.util.yitong.RSAUtil;
import cn.pay.quickpay.utils.platform.security.MD5Tools;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SignUtil {
	
	private static Logger logger = LoggerFactory.getLogger(SignUtil.class);


	/**
	 *
	 * 方法用途: 对所有传入参数按照字段名的Unicode码从小到大排序（字典序），并且生成url参数串<br>
	 * 实现步骤: <br>
	 *
	 * @param paraMap   要排序的Map对象
	 * @param urlEncode   是否需要URLENCODE
	 * @param keyToLower    是否需要将Key转换为全小写
	 *            true:key转化成小写，false:不转化
	 * @param valueEmpty    参数值为空是否参与排名 true 是、false否
	 * @return
	 */
	public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower,boolean valueEmpty)
	{
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		try
		{
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()
			{

				@Override
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)
				{
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});
			// 构造URL 键值对的格式
			StringBuilder buf = new StringBuilder();
			for (Map.Entry<String, String> item : infoIds)
			{
				if (StringUtils.isNotBlank(item.getKey()))
				{
					String key = item.getKey();
					String val = item.getValue();
					if(valueEmpty){
						if (urlEncode)
						{
							val = URLEncoder.encode(val, "utf-8");
						}
						if (keyToLower)
						{
							buf.append(key.toLowerCase() + "=" + val);
						} else
						{
							buf.append(key + "=" + val);
						}
						buf.append("&");
					}else{
						if(!val.equals("")){
							if (urlEncode)
							{
								val = URLEncoder.encode(val, "utf-8");
							}
							if (keyToLower)
							{
								buf.append(key.toLowerCase() + "=" + val);
							} else
							{
								buf.append(key + "=" + val);
							}
							buf.append("&");
						}
					}

				}

			}
			buff = buf.toString();
			if (buff.isEmpty() == false)
			{
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e)
		{
			return null;
		}
		return buff;
	}

	public static String getSign(Map<String,String> params,String key){

		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		buildPrePayParams(buf,params,false);
		String preStr = buf.toString();
		return MD5Tools.MD5(preStr+"&key=" + key);
	}
	

	
	/**
	 * 传递LinkedHashMap  按照特定顺序将value值用"|"拼接  最后拼接上key
	 * @param map
	 * @param key
	 * @return
	 */
	public static String getFuYouSign(Map<String, Object> map,String key){
	  
		Collection<String> keyset= map.keySet();   		  
		List list=new ArrayList<String>(keyset);  		  
		StringBuffer data=new StringBuffer();
		for(int i=0;i<list.size();i++){  
		System.out.println(list.get(i)+"="+map.get(list.get(i)));	
			if(!"md5".equals(list.get(i))){
				data=data.append(map.get(list.get(i))+"|");
			}
			
		}	
		data.append(key);
		logger.info("待签名字符串："+data);		
		String sign=MD5Tools.MD5(data.toString());
		return sign;
	}
	

	
	public static String getSort(Map map){
		//字典序排序   	  
		Collection<String> keyset= map.keySet();   		  
		List list=new ArrayList<String>(keyset);  		  
		Collections.sort(list);  
		StringBuffer data=new StringBuffer();
		//这种打印出的字符串顺序和微信官网提供的字典序顺序是一致的  
		for(int i=0;i<list.size();i++){  
//		System.out.println(list.get(i)+"="+map.get(list.get(i)));	
			data=data.append(list.get(i)+"="+map.get(list.get(i))+"&");

		}
		data.deleteCharAt(data.length()-1);
		return data.toString();
	}
	
	public static void buildPrePayParams(StringBuilder sb,Map<String, String> payParams,boolean encoding){
		List<String> keys = new ArrayList<String>(payParams.keySet());
		Collections.sort(keys);
		for(String key : keys){
            String str = payParams.get(key);
            if (str == null || str.length() == 0)
            {
                //空串不参与sign计算
                continue;
            }
			sb.append(key).append("=");
			if(encoding){
				sb.append(urlEncode(str));
			}else{
				sb.append(str);
			}
			sb.append("&");
		}
		sb.setLength(sb.length() - 1);
		logger.info(sb.toString());
	}
	
	public static String urlEncode(String str){
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (Throwable e) {
			return str;
		}
	}
	
	public static String encode(String data, String key) {
		try {
			DESedeKeySpec dks = new DESedeKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new SecureRandom());
			return Base64.encodeBase64String(cipher.doFinal(data.getBytes("utf-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * 上海亿通银联扫码BC通道中用的签名方式
	 * 将所有的参数名按照字典序升序排序,以(key=value)的形式用&拼接起来得到新串,再使用颁发给商户的商户RSA私钥证书对摘要做签名操作（签名时算法选择SHA-1）。最后，对签名做Base64编码，再将+号替换为%2B。
	 * 例如：sign=md5(a=a&b=b&c=ckey)
	 * @param map
	 * @param private_key
	 * @return
	 */
	public static String getShanghaiYitongSign(Map<String, Object> map,String private_key){
		//字典序排序
		Collection<String> keyset= map.keySet();
		List list=new ArrayList<String>(keyset);
		Collections.sort(list);
		StringBuffer data=new StringBuffer();
		//这种打印出的字符串顺序和微信官网提供的字典序顺序是一致的
		for(int i=0;i<list.size();i++){
			//System.out.println(list.get(i)+"="+map.get(list.get(i)));
			if(!"sign".equals(list.get(i)) && !("".equals(map.get(list.get(i))) || map.get(list.get(i))==null)){
				data=data.append("&"+list.get(i)+"="+map.get(list.get(i)));
			}

		}
		data.delete(0, 1);
//		logger.info("待签名字符串："+data);
		String signData = "";
		try {
			signData = RSAUtil.signByPrivate(data.toString(), RSAUtil.readFile(private_key, "UTF-8"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
//        System.out.println("待签名字符串：" + data + "，rsa加密后的密文：" + signData);
		return signData;
	}
	
	public static void main(String[] args) {
//		LinkedHashMap<String, Object> paramsMap=new LinkedHashMap<>();
//		paramsMap.put("trxType", "UNIONPAY_QRCODE_DH");//交易类型
//		paramsMap.put("merchantNo", "9535");
//		paramsMap.put("orderNum","hxy11");
//		paramsMap.put("amount", "5000");//以元为单位
//		paramsMap.put("goodsName", "冰箱");
//		paramsMap.put("callbackUrl", "");
//		paramsMap.put("serverCallbackUrl", "https");
//		paramsMap.put("orderIp", "10.10.20.187");
//		paramsMap.put("phoneNumber", "18754190709");
//		paramsMap.put("province", "山东");	
//		paramsMap.put("city", "济南");	
//		String sign=getSignTrxRonghuijinfu(paramsMap,"zoldjsjadd");
		
		String str="a=1&b=2&c=34=";
		 String data[] = str.split("&");
	        StringBuffer buf = new StringBuffer();
	        String signature = "";
	        for (int i = 0; i < data.length; i++) {
	            String tmp[] = data[i].split("=", 2);
	            if ("signature".equals(tmp[0])) {
	                signature = tmp[1];
	            } else {
	                buf.append(tmp[0]).append("=").append(tmp[1]).append("&");
	            }
	        }
	        String signatureStr = buf.substring(0, buf.length() - 1);
	        System.out.println("验签数据：" + signatureStr);
	}
}

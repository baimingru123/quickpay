
package cn.pay.quickpay.utils.platform.security;
import java.security.MessageDigest;  
	  
	/** 
	 * MD5加密工具类 
	 * <功能详细描述> 
	 *  
	 * @author  chenlujun 
	 * @version  [版本号, 2014年10月1日] 
	 * @see  [相关类/方法] 
	 * @since  [产品/模块版本] 
	 */  
	public abstract class MD5Tools  {  
	    public final static String MD5(String pwd) {  
	        //用于加密的字符  
	        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
	                'a', 'b', 'c', 'd', 'e', 'f' };  
	        try {  
	            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中  
	            byte[] btInput = pwd.getBytes();  
	               
	            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。  
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");  
	               
	            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要  
	            mdInst.update(btInput);  
	               
	            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文  
	            byte[] md = mdInst.digest();  
	               
	            // 把密文转换成十六进制的字符串形式  
	            int j = md.length;  
	            char str[] = new char[j * 2];  
	            int k = 0;  
	            for (int i = 0; i < j; i++) {   //  i = 0  
	                byte byte0 = md[i];  //95  
	                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5   
	                str[k++] = md5String[byte0 & 0xf];   //   F  
	            }  
	               
	            //返回经过加密后的字符串  
	            return new String(str);  
	               
	        } catch (Exception e) {  
	            return null;  
	        }  
	    }  
	    
	    
	    
	    public static void main(String[] args) {
//	    System.out.println(MD5("{\"accountName\":\"\u67CF\u660E\u5112\",\"accountNo\":\"EqEdU/y6tg51BajLYhG5qOj9ZYfcAUk1\",\"bankCode\":\"308584000013\",\"bankType\":\"TOPRIVATE\",\"channelName\":\"\u5C71\u4E1C\u95EA\u4ED8\u65F6\u4EE3\u4FE1\u606F\u79D1\u6280\u6709\u9650\u516C\u53F8\",\"channelNo\":\"8934938057\",\"installCity\":\"\u6D4E\u5357\u5E02\",\"installCounty\":\"\u7AE0\u4E18\u533A\",\"installProvince\":\"\u5C71\u4E1C\u7701\",\"legalPersonID\":\"37018119940104031X\",\"legalPersonName\":\"\u67CF\u660E\u5112\",\"merchantBillName\":\"\u67CF\u660E\u5112\",\"merchantName\":\"\u67CF\u660E\u5112\",\"merchantPersonName\":\"\u67CF\u660E\u5112\",\"merchantPersonPhone\":\"18754190709\",\"merchantType\":\"PERSON\",\"operateAddress\":\"\u5C71\u4E1C\u6D4E\u5357\"}9SH2QxgYM54i13s0cz7783Wn15D35763").toUpperCase());	
//	    	c9e46aa1469a94bdeb6f06c3f01aeae9
//	    	trxType=UNIONPAY_QRCODE_DH
//	    	retCode=0000
//	    	r1_merchantNo=B100713499
//	    	r2_orderNumber=102018080814380500001
//	    	r3_amount=110.00
//	    	r4_bankId=UNIONPAY_QRCODE_DH
//	    	r5_business=UNIONPAY_QRCODE_DH
//	    	r6_timestamp=2018-08-08 14:38:55
//	    	r7_completeDate=2018-08-08 14:38:57
//	    	r8_orderStatus=SUCCESS
//	    	r9_serialNumber=ZM1808081438551PS
//	    	r10_t0PayResult=1
//	    	sign=663ead649cc89f79887bed42d5674523
	    String str1="#UNIONPAY_QRCODE_DH#0000#B100713499#102018080814380500001#110.00#UNIONPAY_QRCODE_DH#UNIONPAY_QRCODE_DH#2018-08-08 14:38:55#2018-08-08 14:38:55#ZM1808081438551PS#1#hMXQqwLNXar3PTlkI0fzxwb33ukO18OQ";
	    String str="#UNIONPAY_QRCODE_DH#0000#B100713499#102018080814380500001#110.00#UNIONPAY_QRCODE_DH#UNIONPAY_QRCODE_DH#2018-08-08 14:38:55#2018-08-08 14:38:57#SUCCESS#ZM1808081438551PS#1#hMXQqwLNXar3PTlkI0fzxwb33ukO18OQ";
	    	System.out.println(MD5(str));
	    }
	}  
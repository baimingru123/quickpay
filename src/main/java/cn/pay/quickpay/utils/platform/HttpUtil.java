package cn.pay.quickpay.utils.platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * HttpClient工具类（可解决jdk1.7 https connection reset异常，指定TLS协议版本【TLSv1、TLSv1.1、TLSv1.2】）
 * 创建日期 2018-9-15 下午2:40:11   
 * @author 闪付时代 zll
 *
 */
public class HttpUtil {
	
	private static Logger logger = Logger.getLogger(HttpUtil.class);
	
	public static void main(String[] args) {
		String url = "https://plat.baiyintongbao.com/api/member_back/notify?attach=upgrade";
//		url = "https://testapi.shanfushidai.cn/";
		if(true){
			String response = HttpUtil.doGet(url);
			System.out.println(response);
		}
		
		if(true){
			Map<String, String> params = new HashMap<String, String>();
			params.put("agentNo", "123213");
			String response = HttpUtil.doPost(url, params);
			System.out.println(response);
		}
	}
 
	/**
	 * get请求
	 * @return
	 */
	public static String doGet(String url) {
        try {
//        	HttpClient client = new DefaultHttpClient();
        	HttpClient client = wrapClient(new DefaultHttpClient());
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
 
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                
                return strResult;
            }
        }catch (Exception e) {
        	e.printStackTrace();
        }
        
        return null;
	}
	
	/**
	 * post请求(用于key-value格式的参数)
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, Map params){
		
		BufferedReader in = null;  
        try {  
            // 定义HttpClient  
//            HttpClient client = new DefaultHttpClient();  
        	HttpClient client = wrapClient(new DefaultHttpClient());
            // 实例化HTTP方法  
            HttpPost request = new HttpPost();  
            request.setURI(new URI(url));
            
            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
            for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
    			String name = (String) iter.next();
    			String value = String.valueOf(params.get(name));
    			nvps.add(new BasicNameValuePair(name, value));
    			//System.out.println(name +"-"+value);
    		}
            request.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
            
            HttpResponse response = client.execute(request);  
            int code = response.getStatusLine().getStatusCode();
            if(code == 200){	//请求成功
            	in = new BufferedReader(new InputStreamReader(response.getEntity()  
                        .getContent(),"utf-8"));
                StringBuffer sb = new StringBuffer("");  
                String line = "";  
                String NL = System.getProperty("line.separator");  
                while ((line = in.readLine()) != null) {  
                    sb.append(line + NL);  
                }
                
                in.close();  
                
                return sb.toString();
            }
            else{	//
            	System.out.println("状态码：" + code);
            	return null;
            }
        }
        catch(Exception e){
        	e.printStackTrace();
        	
        	return null;
        }
	}
	
	/**
	 * post请求（用于请求json格式的参数）
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, String params) throws Exception {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);// 创建httpPost   
    	httpPost.setHeader("Accept", "application/json"); 
    	httpPost.setHeader("Content-Type", "application/json");
    	String charSet = "UTF-8";
    	StringEntity entity = new StringEntity(params, charSet);
    	httpPost.setEntity(entity);        
        CloseableHttpResponse response = null;
        
        try {
        	
        	response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
            	HttpEntity responseEntity = response.getEntity();
            	String jsonString = EntityUtils.toString(responseEntity);
            	return jsonString;
            }
            else{
				 logger.error("请求返回:"+state+"("+url+")");
			}
        }
        finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return null;
	}
	
	public static HttpClient wrapClient(HttpClient base) throws Exception{
//		  SSLContext ctx = SSLContext.getInstance("TLSv1");//华为云不支持
		  SSLContext ctx = SSLContext.getInstance("TLSv1.1");
//		  SSLContext ctx = SSLContext.getInstance("TLSv1.2");
	      X509TrustManager tm = new X509TrustManager(){
	    	  public X509Certificate[] getAcceptedIssuers()
	    	  {
	    	    return null;
	    	  }
	    	  public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException  {}
	    	  public void checkServerTrusted(X509Certificate[] arg0, String arg1)throws CertificateException{}
	      };

	      ctx.init(null, new TrustManager[] { tm }, null);
	      SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	      ClientConnectionManager ccm = base.getConnectionManager();
	      SchemeRegistry registry = ccm.getSchemeRegistry();
	      registry.register(new Scheme("https", 443, ssf));
	      return new DefaultHttpClient(ccm, base.getParams());
	}
}
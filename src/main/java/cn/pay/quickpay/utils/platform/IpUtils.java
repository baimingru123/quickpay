package cn.pay.quickpay.utils.platform;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public static String getIpAddr1(HttpServletRequest req){
		String remoteIP = req.getHeader("HTTP_X_FORWARDED_FOR");
		if (remoteIP == null || remoteIP.length() == 0) {
			remoteIP = req.getRemoteAddr();
		}
		return remoteIP;
	}
}

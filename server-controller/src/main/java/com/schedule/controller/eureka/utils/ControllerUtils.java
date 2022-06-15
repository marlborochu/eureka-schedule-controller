package com.schedule.controller.eureka.utils;

import java.net.URL;

public class ControllerUtils {
	
	private static ControllerUtils instance;
	private ControllerUtils() {}
	public static synchronized ControllerUtils getInstance() {
		if(instance == null) instance = new ControllerUtils();
		return instance;
	}
	
	public String filterURLPath(String url) {
		
		try {
			String[] urlSplit = url.split("//");
			if(urlSplit.length >= 2) {
				URL u = new URL(url);
				StringBuffer sb = new StringBuffer();
				sb.append(u.getProtocol()).append("://");
				sb.append(u.getHost());
				if(u.getPort() != -1) {
					sb.append(":").append(u.getPort());
				}
				String[] paths = u.getPath().split("/");
				for(String p : paths) {
					if(!p.equals(""))
						sb.append("/").append(p);
				}
				if(u.getQuery() != null) {
					sb.append("?").append(u.getQuery());
				}
				return sb.toString();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	
}

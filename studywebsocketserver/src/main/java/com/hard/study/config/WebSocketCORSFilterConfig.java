package com.hard.study.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

@Component
public class WebSocketCORSFilterConfig implements Filter {
	
	@Autowired
	private WebSocketMessageBrokerStats webSocketMessageBrokerStats;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletResponse res = (HttpServletResponse) response;
		
		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("Access-Control-Allow-Origin", "http://localhost:8081");
		res.setHeader("Access-Control-Allow-Headers", "Origin,Accept,Content-Type,Authorization");
		res.setHeader("Access-Control-Allow-Method", "GET,POST");
		res.setHeader("Access-Control-Max-Age", "3600");
		
		webSocketMessageBrokerStats.setLoggingPeriod(1 * 60 * 1000); // messageBroker stats 정보 출력 시간 = 1분
		
		chain.doFilter(request, response);
		
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	@Override
	public void destroy() {
		
	}
	
}

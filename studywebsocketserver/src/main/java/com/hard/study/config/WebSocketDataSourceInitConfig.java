package com.hard.study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
public class WebSocketDataSourceInitConfig extends AbstractHttpSessionApplicationInitializer {
	
	public WebSocketDataSourceInitConfig() {
		super(WebSocketDataSourceConfig.class);
	}
	
}

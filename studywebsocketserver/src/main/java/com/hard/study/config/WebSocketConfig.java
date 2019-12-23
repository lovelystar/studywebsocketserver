package com.hard.study.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.Authentication;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.hard.study.service.AuthenticationService;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<Session> {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Override
	protected void configureStompEndpoints(StompEndpointRegistry registry) {
		
		registry
			.addEndpoint("/ws-stomp")
				.addInterceptors(new WebSocketHandshakeInterceptor())
					.setAllowedOrigins("http://localhost:8081")
						.withSockJS();
		
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		
		config.enableSimpleBroker("/topic"); // 구독 처리 된 client에게 메세지 전달 ( sub )
		config.setApplicationDestinationPrefixes("/app"); // client에서 server로 보낼 prefix설정 ( put )
		
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		
		registration
			.interceptors(new ChannelInterceptor(){
				
				@Override
				public Message<?> preSend(Message<?> message, MessageChannel channel) {
					
					StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
					System.out.println("configureClientInboundChannel : preSend : " + accessor);
					if(StompCommand.CONNECT.equals(accessor.getCommand())) {
						
						Authentication authentication = authenticationService.getAuthentication();
						accessor.setUser(authentication);
						
					}
					
					return message;
					
				}
				
			});
		
	}
	
}

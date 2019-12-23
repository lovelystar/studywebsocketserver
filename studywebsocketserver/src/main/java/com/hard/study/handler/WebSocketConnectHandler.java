package com.hard.study.handler;

import java.security.Principal;
import java.util.Arrays;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

public class WebSocketConnectHandler<S> implements ApplicationListener<SessionConnectEvent> {
	
	private SimpMessageSendingOperations template;
	
	public WebSocketConnectHandler(SimpMessageSendingOperations template) {
		
		super();
		this.template = template;
		
	}
	
	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		Principal user = accessor.getUser();
		
		if(user == null) {
			return;
		}
		
		this.template.convertAndSend("/topic/message", Arrays.asList(user.getName() + " : " + accessor.getCommand()));
		
	}

}

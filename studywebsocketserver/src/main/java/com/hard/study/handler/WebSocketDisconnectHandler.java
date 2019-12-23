package com.hard.study.handler;

import java.security.Principal;
import java.util.Arrays;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class WebSocketDisconnectHandler<S> implements ApplicationListener<SessionDisconnectEvent> {
	
	private SimpMessageSendingOperations template;
	
	public WebSocketDisconnectHandler(SimpMessageSendingOperations template) {
		
		super();
		this.template = template;
		
	}
	
	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		Principal user = accessor.getUser();
		
		if(user == null) {
			return;
		}
		
		this.template.convertAndSend("/topic/message", Arrays.asList(user.getName() + " : " + accessor.getCommand()));
		
	}
	
}

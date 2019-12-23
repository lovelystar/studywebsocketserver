package com.hard.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import com.hard.study.dto.WebSocketMessageDto;

@RestController
public class WebSocketController {
	
	private SimpMessageSendingOperations template;
	
	@Autowired
	public WebSocketController(SimpMessageSendingOperations template) {
		this.template = template;
	}
	
	// @Parameter = 말 그대로 parameter (ex) url/param?name=test
	// @Payload = body에 담기는 data
	// @SendTo = 1:n으로 메세지를 뿌릴 때 사용하는 구조이며 보통 경로가 "/topic"으로 시작
	// @SendToUser = 1:1으로 메세지를 보낼 때 사용하는 구조이며 보통 경로가 "/queue"로 시작
	@MessageMapping("/message")
	@SendTo("/topic/message")
	public WebSocketMessageDto message(WebSocketMessageDto dto, @Payload String bodyMessage, SimpMessageHeaderAccessor headerAccessor) throws Exception {
		
		// String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
		String username = headerAccessor.getUser().getName();
		
		dto.setUsername(username);
		dto.setFrom(username);
		dto.setMessage(dto.getEventList().get(0).getEvent());
		
		System.out.println("dto.getUsername = " + dto.getUsername());
		System.out.println("dto.getFrom = " + dto.getFrom());
		System.out.println("dto.getMessage = " + dto.getMessage());
		
		return new WebSocketMessageDto(
			dto.getUsername(),
			dto.getTo(),
			dto.getFrom(),
			dto.getMessage(),
			dto.getCreated(),
			dto.getEventList()
		);
		
	}
	
}

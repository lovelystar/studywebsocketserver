package com.hard.study.dao;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository("webSocketEventDao")
@RequiredArgsConstructor
public class WebSocketEventDaoImpl implements WebSocketEventDao {
	/*
	private final SimpMessageSendingOperations template;
	*/
	@Override
	public void sendMessage(String category, String name, String description) {
		/*
		WebSocketEventDto dto = new WebSocketEventDto();
		dto.setCategory(category);
		dto.setName(name);
		dto.setDescription(description);
		
		template.convertAndSend("/topic/event", dto);
		*/
	}
	
}

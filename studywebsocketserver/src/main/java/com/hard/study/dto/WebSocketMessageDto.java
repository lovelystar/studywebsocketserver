package com.hard.study.dto;

import java.util.Calendar;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebSocketMessageDto {

	private String username;
	private String to;
	private String from;
	private String message;
	private Calendar created = Calendar.getInstance();
	private List<WebSocketEventDto> eventList;
	
	public WebSocketMessageDto() {	
	}
	
	public WebSocketMessageDto(String username, String to, String from, String message, Calendar created, List<WebSocketEventDto> eventList) {
		this.username = username;
		this.to = to;
		this.from = from;
		this.message = message;
		this.created = created;
		this.eventList = eventList;
	}
	
}

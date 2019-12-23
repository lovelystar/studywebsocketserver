package com.hard.study.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebSocketEventDto {
	
	private String category;
	private String event;
	private String description;
	
	public WebSocketEventDto() {
	}
	
	public WebSocketEventDto(String category, String event, String description) {
		this.category = category;
		this.event = event;
		this.description = description;
	}
	
}

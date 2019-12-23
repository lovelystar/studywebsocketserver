package com.hard.study.dao;

public interface WebSocketEventDao {
	
	public void sendMessage(String category, String name, String desciption) throws Exception;
	
}

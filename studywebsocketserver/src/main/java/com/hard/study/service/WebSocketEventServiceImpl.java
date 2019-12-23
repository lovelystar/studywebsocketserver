package com.hard.study.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hard.study.dao.WebSocketEventDao;

@Service("webSocketEventService")
public class WebSocketEventServiceImpl implements WebSocketEventService {
	
	@Resource(name="webSocketEventDao")
	private WebSocketEventDao webSocketEventDao;
	
	@Override
	public void sendMessage(String category, String name, String description) throws Exception {
		
		webSocketEventDao.sendMessage(category, name, description);
		
	}
	
}

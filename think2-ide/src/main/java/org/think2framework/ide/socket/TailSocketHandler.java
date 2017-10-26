package org.think2framework.ide.socket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

@Component
public class TailSocketHandler implements WebSocketHandler {

	@Override
	public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
		webSocketSession.sendMessage(new TextMessage("afterConnectionEstablished"));
	}

	@Override
	public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage)
			throws Exception {
		webSocketSession.sendMessage(new TextMessage("handleMessage"));
	}

	@Override
	public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
		webSocketSession.sendMessage(new TextMessage("handleTransportError"));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
		webSocketSession.sendMessage(new TextMessage("afterConnectionClosed"));
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}

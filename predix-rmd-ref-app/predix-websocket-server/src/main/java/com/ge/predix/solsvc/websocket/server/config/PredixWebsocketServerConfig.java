package com.ge.predix.solsvc.websocket.server.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.ge.predix.solsvc.websocket.server.endpoint.PredixWebSocketServerEndPoint;

@Configuration
public class PredixWebsocketServerConfig {

	
	@Bean
	public PredixWebSocketServerEndPoint predixWebSocketServerEndPoint() {
		return new PredixWebSocketServerEndPoint();
	}
	
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}

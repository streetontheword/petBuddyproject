package vttp.server.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket

public class WebSocket implements WebSocketConfigurer{

    @Autowired
    private ThreadReplyWebSocketHandler threadReplyWebSocketHandler;
  

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(threadReplyWebSocketHandler, "/notif-websocket").setAllowedOrigins("*");
        // client needs to connect to this endpoint:
        // e.g ws://localhost:8080/notif-websocket
    }
    
}


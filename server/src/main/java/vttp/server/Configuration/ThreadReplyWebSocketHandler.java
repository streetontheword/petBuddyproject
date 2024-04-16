package vttp.server.Configuration;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Component
public class ThreadReplyWebSocketHandler extends TextWebSocketHandler {

    Map<String, List<WebSocketSession>> sessions = new HashMap<>();
    private final String TYPE_SESSION_USERNAME = "session_username";

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Received message from client: " + message.getPayload().toString());
        JsonReader jr = Json.createReader(new StringReader(message.getPayload().toString()));
        JsonObject payload = jr.readObject();
        String type = payload.getString("type");
        switch (type) {
            case TYPE_SESSION_USERNAME: {
                String username = payload.getString("username"); // works
                addToSessions(username, session);
                break;
            }
        }

        // System.out.println("session id: " + session.getId());
        // System.out.println("session to string: " + session.toString());
        // System.out.println("principal: " + session.getPrincipal());
        // System.out.println("local address: " + session.getLocalAddress());

    }

    public void addToSessions(String username, WebSocketSession session) {
        System.out.println(username);
        if (sessions.containsKey(username)) {
            sessions.get(username).add(session);
            System.out.println(sessions);
        } else {
            List<WebSocketSession> list = new ArrayList<>();
            list.add(session);
            sessions.put(username, list);
            System.out.println("list" + sessions);
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String origin = session.getHandshakeHeaders().getFirst("Origin");
        System.out.println("id: " + session.getId());
        logger.info("WebSocket connection established from origin: {}", origin);

    }

    // private final Map<String, Set<WebSocketSession>> threadSubscriptions = new
    // HashMap<>();

    // public void subscribeToThread(String threadId, WebSocketSession session) {
    // threadSubscriptions.computeIfAbsent(threadId, key -> new
    // HashSet<>()).add(session);
    // }

    // public void unsubscribeFromThread(String threadId, WebSocketSession session)
    // {
    // Set<WebSocketSession> sessions = threadSubscriptions.get(threadId);
    // if (sessions != null) {
    // sessions.remove(session);
    // if (sessions.isEmpty()) {
    // threadSubscriptions.remove(threadId);
    // }
    // }
    // }

    public void notifyUsersAboutNewComment(String username, String notificationMessage){
        System.out.println("is this the message being broadcasted?" + notificationMessage);
        List<WebSocketSession> sessionList = sessions.get(username);
        if (sessionList != null) {
            TextMessage message = new TextMessage(notificationMessage);
            sessionList.forEach(session -> {
                try {
                    System.out.println("what message is this" + message);
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalStateException Ie) {
                    System.out.println("we reached the IE block");
                }
            });
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

    }
    

    public void updateUnreadNotifsCount(String username, Integer count) throws IOException {
        System.out.println("username" + username);
        System.out.println("count " + count);
        List<WebSocketSession> sessionsList = sessions.get(username);
        List<WebSocketSession> newList = new ArrayList<>();
        for (WebSocketSession ws : sessionsList) {
            TextMessage textMessage = new TextMessage(count.toString());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(textMessage);
            try {
                ws.sendMessage(new TextMessage(jsonString));
                newList.add(ws);
            } catch (IllegalStateException ex) {
                ws.close();
            }
        }
        sessions.replace(username, newList);
    }
}

package pt.hugo.LusApp.model.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pt.hugo.LusApp.model.data.Message;
import pt.hugo.LusApp.model.services.MessageService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessageWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<Integer, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    private final MessageService messageService;

    public MessageWebSocketHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Aqui assumo que passas o token no query string: ws://.../ws/messages?userId=123
        String userIdStr = session.getUri().getQuery().split("=")[1];
        int userId = Integer.parseInt(userIdStr);
        activeSessions.put(userId, session);
        //System.out.println(activeSessions);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        activeSessions.values().remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        Message message = objectMapper.readValue(textMessage.getPayload(), Message.class);


        messageService.saveMessage(message);
        //System.out.println(textMessage);

        //System.out.println(textMessage.getPayload());
        //System.out.println(message.getRecipientID());


        WebSocketSession recipientSession = activeSessions.get(message.getRecipientID());
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        }


        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
    }
}

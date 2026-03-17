package pt.hugo.LusApp.model.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pt.hugo.LusApp.model.NewChatRequest;
import pt.hugo.LusApp.model.data.Chat;
import pt.hugo.LusApp.model.data.Message;
import pt.hugo.LusApp.model.services.ChatService;
import pt.hugo.LusApp.model.services.MessageService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class ChatController extends TextWebSocketHandler {

    private final ChatService chatService;
/*    private final MessageService messageService;
    private final ObjectMapper objectMapper;*/

    //private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Autowired
    public ChatController(ChatService chatService/*, MessageService messageService, ObjectMapper objectMapper*/) {
        this.chatService = chatService;
/*        this.messageService = messageService;
        this.objectMapper = objectMapper;*/
    }

    //to create a new chat
    @PostMapping("/chat")
    public ResponseEntity<String> createNewChat(
            Authentication authentication,
            @RequestBody NewChatRequest newChatRequest
    ){
        if (chatService.createNewChat(authentication.getName(),newChatRequest.contactID()))
            return ResponseEntity.ok("Chat created successfully");
        return ResponseEntity.badRequest().build();
    }
    //to get the user for the sidebar
    @GetMapping("/availableChat")
    public ResponseEntity<List<Chat>> getChatList(
            Authentication authentication
    ){
        String username = authentication.getName();
        List<Chat> chatList = chatService.getChatList(username);
        return ResponseEntity.ok(chatList);
    }
    //get chat details
    @GetMapping("/chat/{chatID}")
    public ResponseEntity<String> getChatDetails(){
        return ResponseEntity.ok("test3");
    }
    //add new member to chat (group)
    @PostMapping("/chat/{chatID}/members")
    public ResponseEntity<String> addMembersToGroup(){
        return ResponseEntity.ok("test4");
    }
    //remove user from chat
    @DeleteMapping("/chat/{chatID}/members/{userId}")
    public ResponseEntity<String> removeMembersFromGroup(){
        return ResponseEntity.ok("test5");
    }

    //--------------WEBSOCKET------------

/*    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        // Converte JSON recebido em DTO
        Message message = objectMapper.readValue(textMessage.getPayload(), Message.class);

        // Grava mensagem na base de dados
        //messageService.saveMessage(messageDTO);

        // Envia para todos os participantes desse chat
        TextMessage broadcast = new TextMessage(objectMapper.writeValueAsString(message));
        for (WebSocketSession s : sessions) {
            if (s.isOpen() *//*&& messageService.isParticipant(message.getchatID(), s)*//*) {
                s.sendMessage(broadcast);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }*/

}

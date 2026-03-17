package pt.hugo.LusApp.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.hugo.LusApp.model.MessageRequest;
import pt.hugo.LusApp.model.data.Message;
import pt.hugo.LusApp.model.services.MessageService;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private final MessageService messageService;


    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages(
            Authentication authentication,
            @RequestParam("chatID") int chatID,
            @RequestParam("userID") int userID
            ){
        List<Message>messageList;
        messageList = messageService.getMessages(chatID,userID);
        if (messageList != null){
            return ResponseEntity.ok(messageList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

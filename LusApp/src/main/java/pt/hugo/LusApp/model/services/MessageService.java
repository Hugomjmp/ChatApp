package pt.hugo.LusApp.model.services;

import org.springframework.stereotype.Service;
import pt.hugo.LusApp.model.data.Message;
import pt.hugo.LusApp.model.data.database.DB;

import java.util.List;

@Service
public class MessageService {

    public void saveMessage(Message message){
        DB.saveMessage(message);
    }
    public List<Message> getMessages(int chatID, int userID){
        return DB.getChatMessages(chatID, userID);
    }
/*
    public boolean isParticipant(int chatID){
        return true;
    }*/
}

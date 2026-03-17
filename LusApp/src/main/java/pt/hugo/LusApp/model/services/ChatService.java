package pt.hugo.LusApp.model.services;

import org.springframework.stereotype.Service;
import pt.hugo.LusApp.model.data.Chat;
import pt.hugo.LusApp.model.data.database.DB;

import java.util.List;

@Service
public class ChatService {

    public boolean createNewChat(String username, int contactID){
        return DB.createNewChat(username,contactID);
    }
    public List<Chat> getChatList(String username){
        return DB.getChatList(username);
    }

}

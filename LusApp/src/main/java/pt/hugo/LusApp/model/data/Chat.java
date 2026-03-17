package pt.hugo.LusApp.model.data;

import pt.hugo.LusApp.model.data.Enum.ConversationType;

import java.sql.Timestamp;
import java.util.List;

public class Chat {
    private int ID;
    private String type;
    private String name;
    private String lastMessage;
    private Timestamp lastMessageTime;
    private List<User>chatParticipantsList;

    public List<User> getChatParticipantsList() {
        return chatParticipantsList;
    }

    public void setChatParticipantsList(List<User> chatParticipantsList) {
        this.chatParticipantsList = chatParticipantsList;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Timestamp getlastMessageTime() {
        return lastMessageTime;
    }

    public void setlastMessageTime(Timestamp lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}

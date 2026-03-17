package pt.hugo.LusApp.model.data;

import java.sql.Timestamp;

public class Message {
    private int ID;
    private int chatID;
    private int senderID;
    private int recipientID;
    private String content;
    private Timestamp sentAt;

    public Message() {
    }

    public Message(int ID, int chatID, int senderID, int recipientID, String content, Timestamp sentAt) {
        this.ID = ID;
        this.chatID = chatID;
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.content = content;
        this.sentAt = sentAt;
    }

    public int getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(int recipientID) {
        this.recipientID = recipientID;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "ID=" + ID +
                ", chatID=" + chatID +
                ", senderID=" + senderID +
                ", recipientID=" + recipientID +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}

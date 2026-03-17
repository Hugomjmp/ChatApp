package pt.hugo.LusApp.model.data.database;

import pt.hugo.LusApp.model.data.*;
import pt.hugo.LusApp.model.data.Enum.InviteStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DB {
    private static Connection connection = null;
    private static final String dataBase = "database/DataBase.db";

    public static boolean DBConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataBase);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean DBDisconnect(){
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void createTable(){
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE USERS (\n" +
                    "ID            INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                    "USERNAME      VARCHAR (50) UNIQUE,\n" +
                    "PASSWORD      TEXT,\n" +
                    "PROFILE_NAME  TEXT,\n" +
                    "PROFILE_IMAGE TEXT,\n" +
                    "DESCRIPTION   TEXT,\n" +
                    "CREATED_AT              DEFAULT (DATETIME('now', 'localtime') )\n" +
                    ");"
            );
            statement.executeUpdate(
                    "CREATE TABLE MESSAGES (" +
                    "ID          INTEGER   PRIMARY KEY AUTOINCREMENT,\n" +
                    "SENDER_ID   INTEGER   REFERENCES USERS (ID),\n" +
                    "RECEIVER_ID INTEGER   REFERENCES USERS (ID),\n" +
                    "CONTENT     TEXT,\n" +
                    "SENT_AT     TIMESTAMP DEFAULT (DATETIME('now', 'localtime') )\n" +
                    ");"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateUniqueInviteCode(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        String inviteCode;
        boolean unique = false;

        do {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                stringBuilder.append(chars.charAt(random.nextInt(chars.length())));
            }
            inviteCode = stringBuilder.toString();


            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT COUNT(*) FROM USERS WHERE INVITE_CODE = ?")){
                ps.setString(1, inviteCode);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 0){
                    unique = true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }while (!unique);
        return inviteCode;
    }
    public static boolean createUser(String username, String password, String profileName){
        String inviteCode = generateUniqueInviteCode();

        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(
                    "INSERT INTO USERS" +
                    "(USERNAME, PASSWORD, PROFILE_NAME, INVITE_CODE)" +
                    "VALUES" +
                    "('" + username + "','" + password + "','" + profileName + "','" + inviteCode + "');"
            );
            statement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static boolean getUser(String username, String password){
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM USERS\n" +
                    "WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "';"
            );

            if (rs.next()){
                rs.close();
                statement.close();
                return true;
            }

        } catch (SQLException e) {

            return false;
        }
        return false;
    }
    public static int getUserID(String username){
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(
                    "SELECT ID FROM USERS\n" +
                            "WHERE USERNAME = '" + username + "';"
            );

            if (rs.next()){
                int ID = rs.getInt("ID");
                rs.close();
                statement.close();
                return ID;
            }

        } catch (SQLException e) {
            return -1;
        }
        return -1;
    }
    public static User getUserData(String username){
        User user = null;
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM USERS\n" +
                            "WHERE USERNAME = '" + username + "';"
            );

            if (rs.next()){
                user = new User();
                //user.setUsername(rs.getString("USERNAME"));
                user.setId(rs.getInt("ID"));
                user.setName(rs.getString("PROFILE_NAME"));
                user.setDescription(rs.getString("DESCRIPTION"));
                user.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                user.setImagePath(rs.getString("PROFILE_IMAGE"));
                user.setInviteCode(rs.getString("INVITE_CODE"));
                user.setLanguage(rs.getString("LANGUAGE"));
                rs.close();
                statement.close();
                return user;
            }

        } catch (SQLException e) {

            return user;
        }
        return user;
    }
    public static boolean updateProfileName(String username, String profileName){
        String sql = "UPDATE USERS\n" +
                "SET PROFILE_NAME = '"+ profileName + "'\n" +
                "WHERE USERNAME = '" + username + "';";
        try {
            Statement statement = connection.createStatement();


            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()){
                rs.close();
                statement.close();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }
    public static boolean updateProfileImage(String username, String profileImage){
        String sql = "UPDATE USERS\n" +
                "SET PROFILE_IMAGE = '"+ profileImage + "'\n" +
                "WHERE USERNAME = '" + username + "';";
        try {
            Statement statement = connection.createStatement();


            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()){
                rs.close();
                statement.close();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }
    public static boolean updateUserDescription(String username, String description){
        String sql = "UPDATE USERS\n" +
                "SET DESCRIPTION = '"+ description + "'\n" +
                "WHERE USERNAME = '" + username + "';";
        try {
            Statement statement = connection.createStatement();


            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()){
                rs.close();
                statement.close();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }
    public static boolean updateUserLanguage(String username, String language){
        String sql = "UPDATE USERS\n" +
                "SET LANGUAGE = '"+ language + "'\n" +
                "WHERE USERNAME = '" + username + "';";
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()){
                rs.close();
                statement.close();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public static boolean createInvite(int senderID, String inviteCode){
        if(verifyIfInviteIsPresent(senderID, inviteCode)){
            System.out.println("found");
            return false;
        }

        String sql = "INSERT INTO INVITES (INVITED_USER, CREATED_BY)\n" +
                "VALUES (\n" +
                "(SELECT ID FROM USERS WHERE INVITE_CODE = '" + inviteCode + "')," + senderID + ");";
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);

            statement.close();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    private static boolean verifyIfInviteIsPresent(int senderID, String inviteCode){

        String sql = "SELECT COUNT(*) AS TOTAL\n" +
                "FROM INVITES\n" +
                "WHERE CREATED_BY = " + senderID + " \n" +
                "AND INVITED_USER = (SELECT ID FROM USERS WHERE INVITE_CODE = '" + inviteCode + "');";
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);

            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()){
                rs.close();
                statement.close();
                return rs.getInt("TOTAL") > 0;
            }

        } catch (SQLException e) {
            return false;
        }
        return false;
    }
    public static List<Invite> getReceivedPendingInvites(String USERNAME){
        List<Invite>inviteList = new ArrayList<>();
        String sql = "SELECT \n" +
                "    I.ID AS INVITE_ID,\n" +
                "    SENDER.PROFILE_NAME AS PROFILE_NAME,\n" +
                "    SENDER.PROFILE_IMAGE AS PROFILE_IMAGE,\n" +
                "    I.STATUS\n" +
                "FROM INVITES I\n" +
                "JOIN USERS SENDER ON I.CREATED_BY = SENDER.ID\n" +
                "JOIN USERS RECEIVER ON I.INVITED_USER = RECEIVER.ID\n" +
                "WHERE RECEIVER.USERNAME = '" + USERNAME + "'\n" +
                "  AND I.STATUS = 'PENDING';";

        Invite invite = null;
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                invite = new Invite();
                invite.setInviteID(rs.getString("INVITE_ID"));
                invite.setUsername(rs.getString("PROFILE_NAME"));
                invite.setProfileImage(rs.getString("PROFILE_IMAGE"));
                invite.setStatus(rs.getString("STATUS"));
                inviteList.add(invite);
            }
            rs.close();
            statement.close();
            return inviteList;
        } catch (SQLException e) {
            return inviteList;
        }

    }


    public static boolean acceptInvite(String username, int inviteID) {

        if (!verifyInvite(username,inviteID)){
            return false;
        } else if (!changeInviteStatus(InviteStatus.ACCEPT.name(), inviteID)) {
            return false;
        }
        String sql = "INSERT INTO CONTACTS (USER_ID, CONTACT_ID)\n" +
                "VALUES\n" +
                "(\n" +
                "    (SELECT ID FROM USERS WHERE USERNAME = '" + username + "'),\n" +
                "    (SELECT CREATED_BY FROM INVITES WHERE ID = " + inviteID + ")\n" +
                "),\n" +
                "(\n" +
                "    (SELECT CREATED_BY FROM INVITES WHERE ID = " + inviteID + "),\n" +
                "    (SELECT ID FROM USERS WHERE USERNAME = '" + username + "')\n" +
                ")";
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);

            statement.close();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static boolean rejectInvite(String username, int inviteID){
        if (!verifyInvite(username,inviteID)){
            return false;
        } else if (changeInviteStatus(InviteStatus.REJECTED.name(), inviteID)) {
            return true;
        }
        return false;
    }
    private static boolean verifyInvite(String username, int inviteID) {
        String sql = "SELECT 1\n" +
                "FROM INVITES\n" +
                "WHERE ID = "+ inviteID +"\n" +
                "  AND INVITED_USER = (SELECT ID FROM USERS " +
                "WHERE USERNAME ='" + username + "')\n" +
                "  AND STATUS = 'PENDING';";
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next()){
                resultSet.close();
                statement.close();
                return false;
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    private static boolean changeInviteStatus(String Status, int inviteID){
        String sql = "UPDATE INVITES\n" +
                "SET STATUS = '" + Status + "', USED_AT = CURRENT_TIMESTAMP\n" +
                "WHERE ID = " + inviteID + ";";
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);

            statement.close();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Contact> getUserContacts(String username){
        List<Contact>contactList = new ArrayList<>();
        String sql = "SELECT \n" +
                "    u.ID AS ID,\n" +
                "    u.PROFILE_NAME AS PROFILE_NAME,\n" +
                "    u.PROFILE_IMAGE AS PROFILE_IMAGE\n" +
                "FROM CONTACTS c\n" +
                "JOIN USERS u ON c.CONTACT_ID = u.ID\n" +
                "WHERE c.USER_ID = (SELECT ID FROM USERS WHERE USERNAME = '" + username + "');";

        Contact contact = null;
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                contact = new Contact();
                contact.setId(rs.getInt("ID"));
                contact.setProfileName(rs.getString("PROFILE_NAME"));
                contact.setProfileImage(rs.getString("PROFILE_IMAGE"));
                contactList.add(contact);
            }
            rs.close();
            statement.close();
            return contactList;
        } catch (SQLException e) {
            return contactList;
        }
    }

    public static boolean createNewChat(String username, int contactID){
        int userID = getUserID(username);
        int chatID = 0;
        if(verifyIfChatExists(userID,contactID))
            return false;

        String sql = "INSERT INTO CHAT (TYPE) VALUES ('PRIVATE');";
        String sqlChatID = "SELECT last_insert_rowid() AS CHAT_ID;";


        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);
            ResultSet rs = statement.executeQuery(sqlChatID);
            if (rs.next()){
                chatID = rs.getInt("CHAT_ID");
                String sqlCreateChatParticipants = "INSERT INTO CHAT_PARTICIPANTS (CHAT_ID, USER_ID, ROLE)\n" +
                        "VALUES ("+ chatID +", "+ userID +", 'MEMBER'),\n" +
                        "       ("+ chatID +", "+ contactID +", 'MEMBER');";
                statement.executeUpdate(sqlCreateChatParticipants);
                rs.close();
                statement.close();
                return true;
            }
            statement.close();
            return false;
        } catch (SQLException e) {
            return false;
        }
    }
    public static List<Chat> getChatList(String username){
        Chat chat = null;
        List<Chat>chatList = new ArrayList<>();
        int userID = getUserID(username);
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(
                    "SELECT \n" +
                            "    c.ID AS CHAT_ID,\n" +
                            "    c.TYPE AS CHAT_TYPE,\n" +
                            "    c.NAME AS CHAT_NAME,\n" +
                            "    m.CONTENT AS lastMessage,\n" +
                            "    m.SENT_AT AS lastMessageTime\n" +
                            "FROM CHAT c\n" +
                            "JOIN CHAT_PARTICIPANTS cp ON cp.CHAT_ID = c.ID\n" +
                            "LEFT JOIN MESSAGES m ON m.CONVERSATION_ID = c.ID\n" +
                            "    AND m.SENT_AT = (\n" +
                            "        SELECT MAX(SENT_AT) \n" +
                            "        FROM MESSAGES \n" +
                            "        WHERE CONVERSATION_ID = c.ID\n" +
                            "    )\n" +
                            "WHERE cp.USER_ID = "+userID+";"
            );

            while (rs.next()){
                chat = new Chat();
                chat.setID(rs.getInt("CHAT_ID"));
                chat.setType(rs.getString("CHAT_TYPE"));
                chat.setName(rs.getString("CHAT_NAME"));
                chat.setLastMessage(rs.getString("lastMessage"));
                chat.setlastMessageTime(rs.getTimestamp("lastMessageTime"));
                chat.setChatParticipantsList(getParticipants(chat.getID()));
                chatList.add(chat);


            }
            rs.close();
            statement.close();
            return chatList;
        } catch (SQLException e) {

            return chatList;
        }
    }
    public static List<User> getParticipants(int chatID){
        List<User> participants = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT u.ID, u.PROFILE_NAME, u.PROFILE_IMAGE, u.DESCRIPTION " +
                            "FROM USERS u " +
                            "JOIN CHAT_PARTICIPANTS cp ON cp.USER_ID = u.ID " +
                            "WHERE cp.CHAT_ID = " + chatID + ";"
            );

            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("ID"));
                user.setName(rs.getString("PROFILE_NAME"));
                user.setImagePath(rs.getString("PROFILE_IMAGE"));
                user.setDescription(rs.getString("DESCRIPTION"));
                participants.add(user);
            }
            rs.close();
            stmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return participants;
    }
    private static boolean verifyIfChatExists(int userID, int contactID){
        int chatID = 0;
        String sql = "SELECT c.id\n" +
                "FROM chat c\n" +
                "JOIN chat_participants cp1 ON c.id = cp1.chat_id\n" +
                "JOIN chat_participants cp2 ON c.id = cp2.chat_id\n" +
                "WHERE c.type = 'PRIVATE'\n" +
                "AND cp1.user_id = "+ userID +"\n" +
                "AND cp2.user_id = "+ contactID +";";
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()){
                rs.close();
                statement.close();
                return true;
            }

        } catch (SQLException e) {
            return false;
        }
        return false;
    }
    public static void saveMessage(Message message){
        String sql = "INSERT INTO MESSAGES (CONVERSATION_ID, SENDER_ID, CONTENT)\n" +
                "VALUES (" + message.getChatID() + ", "
                + message.getSenderID() + ", '"
                + message.getContent() + "');";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
/*    Contact contact = null;
        try {
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()){
            contact = new Contact();
            contact.setId(rs.getInt("ID"));
            contact.setProfileName(rs.getString("PROFILE_NAME"));
            contact.setProfileImage(rs.getString("PROFILE_IMAGE"));
            contactList.add(contact);
        }
        rs.close();
        statement.close();
        return contactList;
    } catch (SQLException e) {
        return contactList;
    }*/
    public static List<Message> getChatMessages(int chatID, int userID){

        if (!verifyIfUserIsParticipant(chatID,userID)){
            return null;
        }
        Message message = null;
        List<Message>messageList = new ArrayList<>();
        String sql = "SELECT * FROM MESSAGES WHERE CONVERSATION_ID = " + chatID +";";
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                message = new Message();
                message.setChatID(rs.getInt("CONVERSATION_ID"));
                message.setSenderID(rs.getInt("SENDER_ID"));
                message.setContent(rs.getString("CONTENT"));
                message.setSentAt(rs.getTimestamp("SENT_AT"));
                messageList.add(message);
            }
            return messageList;
        } catch (SQLException e) {
            return null;
        }
    }
    private static boolean verifyIfUserIsParticipant(int chatID, int userID){
        String sql = "SELECT 1 FROM CHAT_PARTICIPANTS WHERE CHAT_ID = " + chatID + " AND USER_ID = " + userID + " ;";

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()){
                statement.close();
                resultSet.close();
                return true;
            }

        } catch (SQLException e) {
            return false;
        }
        return false;
    }
}

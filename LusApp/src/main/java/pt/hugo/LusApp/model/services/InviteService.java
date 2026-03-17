package pt.hugo.LusApp.model.services;

import org.springframework.stereotype.Service;
import pt.hugo.LusApp.model.data.Invite;
import pt.hugo.LusApp.model.data.database.DB;

import java.util.ArrayList;
import java.util.List;

@Service
public class InviteService {

    public boolean invite(String senderUsername, String inviteCode){
        return DB.createInvite(DB.getUserID(senderUsername), inviteCode);
    }

    //listar convites recebidos
    public List<Invite> getReceivedInvites(String username){
        return DB.getReceivedPendingInvites(username);
    }
    //aceitar convite

    public boolean acceptInvites(String username, int invite_ID){
        return DB.acceptInvite(username,invite_ID);
    }


    //recusar convite

    public boolean rejectInvites(String username, int invite_ID){
        return DB.rejectInvite(username,invite_ID);
    }
}

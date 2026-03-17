package pt.hugo.LusApp.model.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.hugo.LusApp.model.InviteRequest;
import pt.hugo.LusApp.model.data.Invite;
import pt.hugo.LusApp.model.services.InviteService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class InviteController {

    @Autowired
    private final InviteService inviteService;

    public InviteController(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    @PostMapping("/invite")
    public ResponseEntity<String> sendInvite(
            Authentication authentication,
            @RequestBody InviteRequest inviteRequest
    ){
        String sender = authentication.getName();
        String inviteCode = inviteRequest.inviteCode();
        if (inviteService.invite(sender,inviteCode)){
            return ResponseEntity.ok("Invite created successfully.");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body("Invite already exists.");
        //inviteService.invite(sender,inviteCode);
    }
    @GetMapping("/invite/received")
    public ResponseEntity<List<Invite>> receivedInvite(
            Authentication authentication
    ){
        List<Invite>inviteList;
        String username = authentication.getName();
        inviteList = inviteService.getReceivedInvites(username);
        if (inviteList != null){
            for (Invite invite : inviteList){
                String imageName = invite.getProfileImage();
                if (invite.getProfileImage() == null){
                    invite.setProfileImage("http://localhost:8080/assets/images/personW.png");
                }else{
                    invite.setProfileImage("http://localhost:8080/assets/images/" + imageName);
                }
            }
            return ResponseEntity.ok(inviteList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null); //ver isto depois
    }

    /*colocar uma resposta de jeito depois*/
    @PostMapping("/invite/accept")
    public void acceptInvite(
            Authentication authentication,
            @RequestParam int invite_ID
    ){
        String username = authentication.getName();
        inviteService.acceptInvites(username, invite_ID);

    }
    /*colocar uma resposta de jeito depois*/
    @PostMapping("/invite/decline")
    public void rejectInvite(
            Authentication authentication,
            @RequestParam int invite_ID
    ){
        String username = authentication.getName();
        inviteService.rejectInvites(username, invite_ID);
    }




/*@GetMapping("/invite/sent")      // listar convites enviados*/
}

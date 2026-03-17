package pt.hugo.LusApp.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.hugo.LusApp.model.data.Contact;
import pt.hugo.LusApp.model.data.Enum.IPAddress;
import pt.hugo.LusApp.model.services.ContactService;

import java.util.List;

@RestController
public class ContactController {

    @Autowired
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<Contact>> getContacts(
            Authentication authentication
    ){
        List<Contact>contactList;
        String username = authentication.getName();
        contactList= contactService.getContacts(username);
        if (contactList != null){
            for (Contact contact : contactList){
                String imageName = contact.getProfileImage();
                if (contact.getProfileImage() == null){
                    //contact.setProfileImage("http://localhost:8080/assets/images/personW.png");
                    contact.setProfileImage(IPAddress.IP_ADDRESS.getUrl()+"/assets/images/personW.png");
                }else{
                    //contact.setProfileImage("http://localhost:8080/userpicture/" + imageName);
                    contact.setProfileImage(IPAddress.IP_ADDRESS.getUrl()+"/userpicture/" + imageName);
                }
            }
            return ResponseEntity.ok(contactList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}

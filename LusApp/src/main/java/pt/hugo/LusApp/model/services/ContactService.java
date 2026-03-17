package pt.hugo.LusApp.model.services;

import org.springframework.stereotype.Service;
import pt.hugo.LusApp.model.data.Contact;
import pt.hugo.LusApp.model.data.database.DB;

import java.util.List;

@Service
public class ContactService {


    public List<Contact> getContacts(String username){
        return DB.getUserContacts(username);
    }

}

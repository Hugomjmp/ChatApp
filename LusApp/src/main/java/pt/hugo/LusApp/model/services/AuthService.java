package pt.hugo.LusApp.model.services;

import org.springframework.stereotype.Service;
import pt.hugo.LusApp.model.data.User;
import pt.hugo.LusApp.model.data.database.DB;

@Service
public class AuthService {

    public boolean validateCredentials(String username, String password){
        return DB.getUser(username,password);
    }

    public boolean validateRegister(String username, String password, String profileName){
        return DB.createUser(username, password, profileName);
    }
}

package pt.hugo.LusApp.model.services;

import org.springframework.stereotype.Service;
import pt.hugo.LusApp.model.data.User;
import pt.hugo.LusApp.model.data.database.DB;

@Service
public class UserService {

    public User getUserData(String username){
        return DB.getUserData(username);
    }
    public boolean updateUserData(String username, String profileName, String profileImage, String language, String description){
        if (!profileName.isBlank() && profileImage.isBlank() && language.isEmpty() && description.isBlank()){
            return DB.updateProfileName(username, profileName);
        } else if (profileName.isBlank() && !profileImage.isBlank() && language.isEmpty() && description.isBlank()) {
            return DB.updateProfileImage(username, profileImage);
        } else if (profileName.isBlank() && profileImage.isBlank() && language.isEmpty() && !description.isBlank()) {
            return DB.updateUserDescription(username, description);
        } else if (profileName.isBlank() && profileImage.isBlank() && !language.isEmpty() && description.isBlank()) {
            return DB.updateUserLanguage(username, language);
        }
        return false;
    }
}

package pt.hugo.LusApp.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.hugo.LusApp.model.UpdateRequest;
import pt.hugo.LusApp.model.data.Enum.IPAddress;
import pt.hugo.LusApp.model.data.User;
import pt.hugo.LusApp.model.services.UserService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/profile")
    public ResponseEntity<User> profile(
            Authentication authentication
    ) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserData(username);
        if (user.getImagePath() == null){
            //user.setImagePath("http://localhost:8080/assets/images/personW.png");
            user.setImagePath(IPAddress.IP_ADDRESS.getUrl() + "/assets/images/personW.png");
        }else{
            //user.setImagePath("http://localhost:8080/uploads/assets/userpictures/" + user.getImagePath());
            //user.setImagePath("http://localhost:8080/userpicture/" + user.getImagePath());
            user.setImagePath(IPAddress.IP_ADDRESS.getUrl()+"/userpicture/" + user.getImagePath());
        }
        return ResponseEntity.ok(user);
    }


    @PutMapping("/update")
    public void updateProfile(
            Authentication authentication,
            @RequestBody UpdateRequest updateRequest
    ){
        String username = authentication.getName();

        String profileName = updateRequest.profileName();
        String profileImage = updateRequest.profileImage();
        String language = updateRequest.language();
        String description = updateRequest.description();
        userService.updateUserData(username, profileName, profileImage, language, description);
    }

    @GetMapping("/userpicture/{filename}")
    public ResponseEntity<Resource> getUserPicture(
            @PathVariable String filename
    ){

        try {
            Path file = Paths.get("uploads/assets/userpictures").resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()){
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(file);
            if (contentType == null){
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload-profile-picture")
    public ResponseEntity<String> uploadPicture(
           Authentication authentication,
            @RequestParam("picture") MultipartFile file
            ) throws IOException {

        if (file.isEmpty())
            return ResponseEntity.badRequest().body("file empty");
        String username = authentication.getName();
        String profileImage = file.getOriginalFilename();

        if (profileImage != null){
            System.out.println(profileImage);
            //create folder
            Path uploadDir = Paths.get("uploads/assets/userpictures").toAbsolutePath().normalize();
            Files.createDirectories(uploadDir);
            //-------------
            Path filePath = uploadDir.resolve(profileImage);
            file.transferTo(filePath.toFile());
            userService.updateUserData(username,"",profileImage,"","");
        }




/*        String username = authentication.getName();
        User user = userService.getUserData(username);

        String uploadFolder = "uploads/assets/userpictures";
        String filename = file.getOriginalFilename();
        Path path = Paths.get(uploadFolder).resolve(filename);
        Files.createDirectories(path.getParent()); // garante que a pasta existe
        file.transferTo(path.toFile());*/


        return ResponseEntity.ok("Upload successufly");
    }
}

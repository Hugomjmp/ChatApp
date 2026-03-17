package pt.hugo.LusApp.model.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.hugo.LusApp.model.LoginRequest;
import pt.hugo.LusApp.model.RegisterRequest;
import pt.hugo.LusApp.model.services.AuthService;
import pt.hugo.LusApp.model.services.TokenService;

@RestController
public class AuthController {
    private final TokenService tokenService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(TokenService tokenService, AuthService authService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest){

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(),loginRequest.password());

        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        return tokenService.generateToken(authenticationResponse);
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){
        String username = registerRequest.username();
        String password = registerRequest.password();
        String profileName = registerRequest.profileName();
        if (username == null || password == null || username.equalsIgnoreCase("") || password.equalsIgnoreCase("") || profileName.equalsIgnoreCase("")) {
            return ResponseEntity.badRequest().body("Username and Password are required.");
        }
        if (authService.validateRegister(username,password, profileName)){
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body("Account already exists.");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        return ResponseEntity.ok().build();
    }
}

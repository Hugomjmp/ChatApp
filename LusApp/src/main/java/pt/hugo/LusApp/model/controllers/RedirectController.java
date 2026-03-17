package pt.hugo.LusApp.model.controllers;

import org.springframework.stereotype.Controller;

@Controller
public class RedirectController {

/*    @GetMapping("/")
    public String redirectBasedOnAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/chat.html";  // Já autenticado? Vai para o chat
        } else {
            return "redirect:/login";     // Não autenticado? Vai para o login
        }
    }*/
}

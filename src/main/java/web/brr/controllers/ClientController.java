package web.brr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.brr.domains.User;
import web.brr.repositories.ClienteRep;

@Controller
@RequestMapping("/cliente")
public class ClientController {

    @Autowired
    private ClienteRep clienteRep;

    @GetMapping("/")
    public String loginPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = clienteRep.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("currentUser", user);
        }
        return "clientePage/index";
    }

}

package web.brr.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.brr.domains.Locacao;
import web.brr.domains.Locadora;
import web.brr.domains.User;
import web.brr.service.impl.LocadoraService;

@Controller
@RequestMapping("/locadora")
public class LocadoraController {

    @Autowired
    private LocadoraService locadoraService;

    @GetMapping("/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = locadoraService.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("currentUser", user);
        }
        return "locadoraPage/index";
    }
    
    @GetMapping("/locacao")
    public String getLocacoes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Locadora logged = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = locadoraService.findByEmail(userDetails.getUsername()).orElse(null);
            logged =(Locadora) user;
            model.addAttribute("currentUser", user);
        }
        List<Locacao> locacao = locadoraService.findRegistrations(logged.getId().toString());
        model.addAttribute("locacao", locacao);
        return "locadoraPage/locacao";
    }

    @GetMapping("/perfil")
    public String editarDados(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Locadora locadora = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = locadoraService.findByEmail(userDetails.getUsername()).orElse(null);
            locadora = (Locadora) user;
            model.addAttribute("currentUser", user);
        }
        model.addAttribute("locadora", locadora);
        return "locadoraPage/perfil";
    }

    @PostMapping("/perfil")
    public String editarDados(Locadora locadora) {
        System.out.println(locadora.getCidade());
        System.out.println(locadora.getCnpj());
        System.out.println(locadora.getSenha());
        locadora.setRole("ROLE_LOCADORA");
        locadoraService.save(locadora,true);
        return "redirect:/locadora/perfil";
    }


}

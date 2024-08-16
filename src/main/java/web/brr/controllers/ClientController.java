package web.brr.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import web.brr.domains.Cliente;
import web.brr.domains.Locacao;
import web.brr.domains.Locadora;
import web.brr.domains.User;
import web.brr.service.impl.ClienteService;
import web.brr.service.impl.LocacaoService;
import web.brr.service.impl.LocadoraService;

@Controller
@RequestMapping("/cliente")
public class ClientController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private LocadoraService locadoraService;

    @Autowired 
    private LocacaoService locacaoService;



    @GetMapping("/")
    public String loginPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = clienteService.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("currentUser", user);
        }
        return "clientePage/index";
    }

    @GetMapping("/locacao")
    public String getLocacoes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Cliente logged = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = clienteService.findByEmail(userDetails.getUsername()).orElse(null);
            logged =(Cliente) user;
            model.addAttribute("currentUser", user);
        }
        List<Locacao> locacao = clienteService.findRegistrations(logged.getId().toString());
        model.addAttribute("locacao", locacao);
        return "clientePage/locacao";
    }

    @GetMapping("/registrar")
    public String registrarLocacao(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Cliente logged = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = clienteService.findByEmail(userDetails.getUsername()).orElse(null);
            //logged =(Cliente) user;
            model.addAttribute("currentUser", user);
        }  


        List<Locadora> locadoras = locadoraService.findAll();
        model.addAttribute("locadoras", locadoras);

        return "clientePage/registrarLocacao";
    }

    @PostMapping("/registrar")
    public String registrarLoc(@RequestParam("locadoraId") Long locadoraId, @RequestParam("locacaoDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime  locacaoDate){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Cliente logged = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = clienteService.findByEmail(userDetails.getUsername()).orElse(null);
            logged =(Cliente) user;
        }
        
        if(logged == null) throw new RuntimeException("Usuário não encontrado");
        Optional<Locadora> locadora = locadoraService.findById(locadoraId);
        if(!locadora.isPresent()) throw new RuntimeException("Locadora não encontrada");

        // TODO : LOGICA PARA CHECAR LOCACAO
        Locacao locacao = new Locacao();
        locacao.setCliente(logged);
        locacao.setLocadora(locadora.get());
        locacao.setRegisteredAt(locacaoDate);
        
        locacaoService.save(locacao);
        return "clientePage/locacao";
    }

}

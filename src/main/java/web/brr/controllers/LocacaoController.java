package web.brr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import web.brr.domains.Locacao;
import web.brr.domains.User;
import web.brr.service.impl.LocacaoService;
import web.brr.service.impl.UserService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
@RequestMapping("/locacao")
public class LocacaoController {

    @Autowired
    private LocacaoService locacaoService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public String locacaoPage(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User logged = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
            logged = user;

        }

        Locacao loc = locacaoService.findById(id).orElse(null);
        if (loc == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Locação não encontrada");
        }
        model.addAttribute("locacao", loc);
        return "locacaoPage/index";
    }

    @PostMapping("/atualizar")
    public String atualizaLocacao(@ModelAttribute("locacao") Locacao newLocacao) {
        // Preciso saber daonde veio a requisao ?

        Locacao loc = locacaoService.findById(newLocacao.getId()).orElse(null);
        if (loc == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Locação não encontrada");
        }
        newLocacao.setCliente(loc.getCliente());
        newLocacao.setLocadora(loc.getLocadora());
        // Preciso do ID de quem fez a requisição(logado)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User logged = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
            logged = user;

        }
        if (logged == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Você não tem permissão para atualizar essa locação");
        }

        // Se a pessoa logada for um admin, atualizar
        if (logged.getRole().equals("ROLE_ADMIN")) {
            // TODO: VERIFICAR LOGICA DE HORARIO
            locacaoService.save(newLocacao);
        }

        // Se a pessoa logada for um cliente, verificar se o ID da locacao é dele
        else if (newLocacao.getCliente().getId() == logged.getId()) {
            // TODO: VERIFICAR LOGICA DE HORARIO
            locacaoService.save(newLocacao);
        }

        else if (newLocacao.getLocadora().getId() == logged.getId()) {
            // TODO: VERIFICAR LOGICA DE HORARIO
            locacaoService.save(newLocacao);
        }

        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Você não tem permissão para atualizar essa locação");
        }

        return "redirect:/locacao/" + newLocacao.getId();
    }

}

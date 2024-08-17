package web.brr.controllers;

import java.time.LocalDateTime;
import java.util.List;

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
import web.brr.service.impl.ObjectValidatorService;
import web.brr.service.impl.UserService;
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

    private ObjectValidatorService objectValidatorService = new ObjectValidatorService();

    @GetMapping("/{id}")
    public String locacaoPage(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
            if (user != null) {
                model.addAttribute("logged", user);
            }
        }

        Locacao loc = locacaoService.findById(id).orElse(null);
        if (loc == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Locação não encontrada");
        }
        LocalDateTime dataAtual = LocalDateTime.now();
        String data = dataAtual.toString();
        model.addAttribute("dataAtual", data.substring(0, data.lastIndexOf(":")));
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

        List<String> errors = objectValidatorService.validate(newLocacao);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
        }

        // Verificar se alguem ja tem essa locacao
        List<Locacao> locacoes = locacaoService.findLocacaoClienteByDate(newLocacao.getRegisteredAt(),
                newLocacao.getCliente().getId().toString());
        locacoes.addAll(locacaoService.findLocacaoLocadoraByDate(newLocacao.getRegisteredAt(),
                newLocacao.getLocadora().getId().toString()));
        if (!locacoes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe uma locação para essa data");
        }
        // Se a pessoa logada for um admin, atualizar
        if (logged.getRole().equals("ROLE_ADMIN")) {
            locacaoService.save(newLocacao);
        }

        // Se a pessoa logada for um cliente, verificar se o ID da locacao é dele
        else if (newLocacao.getCliente().getId() == logged.getId()
                || newLocacao.getLocadora().getId() == logged.getId()) {
            locacaoService.save(newLocacao);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Você não tem permissão para atualizar essa locação");
        }

        return "redirect:/locacao/" + newLocacao.getId();
    }

    @GetMapping("/deletar/{id}")
    public String deletarLoc(@ModelAttribute("locacao") Locacao locacao, @RequestParam("ref") String referrerUrl) {
        Locacao loc = locacaoService.findById(locacao.getId()).orElse(null);
        if (loc == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Locação não encontrada");
        }
        String returnString = null;
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
                    "Você não tem permissão para deletar essa locação");
        }

        // Se a pessoa logada for um admin, deletar
        if (logged.getRole().equals("ROLE_ADMIN")) {
            locacaoService.deleteById(locacao.getId());
            returnString = "redirect:/admin/";
        }

        // Se a pessoa logada for um cliente, verificar se o ID da locacao é dele
        else if (loc.getCliente().getId() == logged.getId() || loc.getLocadora().getId() == logged.getId()) {
            locacaoService.deleteById(locacao.getId());
            returnString = "redirect:/locadora/";
            if (loc.getCliente().getId() == logged.getId())
                returnString = "redirect:/cliente/";

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Você não tem permissão para deletar essa locação");
        }
        if (referrerUrl != null && !referrerUrl.isEmpty()) {
            returnString = "redirect:" + referrerUrl;
        }
        return returnString;
    }

}

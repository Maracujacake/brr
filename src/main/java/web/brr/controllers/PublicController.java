package web.brr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.brr.domains.Cliente;
import web.brr.service.impl.UserService;

@Controller
@RequestMapping("/publicos")
public class PublicController {
    @Autowired
    private UserService userService;

    @GetMapping("/cadastro")
    public String showCadastro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "publicos/cadastroCliente";
    }

    @PostMapping("/cliente/cadastrar")
    public String saveCliente(Cliente cliente) {
        System.out.println(cliente.getNome());
        cliente.setRole("CLIENTE");

        userService.save(cliente);
        return "index";
    }
}

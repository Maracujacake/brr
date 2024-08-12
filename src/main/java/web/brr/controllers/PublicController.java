package web.brr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.brr.domains.Cliente;
import web.brr.service.impl.ClienteService;

@Controller
@RequestMapping("/publicos")
public class PublicController {

    @Autowired
    private ClienteService clienteService;
    @GetMapping("/cliente/cadastro")
    public String showCadastro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "publicos/cadastroCliente";
    }

    @PostMapping("/cliente/cadastrar")
    public String saveCliente(Cliente cliente) {
        System.out.println(cliente.getNome());
        Cliente cli = clienteService.save(cliente);
        if(cli == null){
            return "redirect:/publicos/cliente/cadastro?error";
        }
        return "index";
    }
}

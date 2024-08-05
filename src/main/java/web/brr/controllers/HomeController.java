package web.brr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import web.brr.domains.Cliente;
import web.brr.repositories.ClienteRep;
import web.brr.repositories.LocadoraRep;

@Controller
public class HomeController {

    @Autowired
    private ClienteRep clienteRep;

    @Autowired
    private LocadoraRep locadoraRep;

    // @GetMapping("/")
    // public String index() {
    // return "index";
    // }

    @GetMapping("/publicos/cadastro")
    public String showCadastro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "publicos/cadastroCliente";
    }

    @PostMapping("/cadastro")
    public String saveCliente(Cliente cliente) {
        System.out.println(cliente.getNome());
        clienteRep.save(cliente);
        return "index";
    }

    @GetMapping("/")
    public String testBD(Model model) {

        model.addAttribute("clientes", clienteRep.findAll());
        model.addAttribute("loc", locadoraRep.findAll());

        return "publicos/testeBD";
    }

}

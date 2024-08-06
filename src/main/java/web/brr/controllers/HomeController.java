package web.brr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import web.brr.domains.Cliente;
import web.brr.service.impl.UserService;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/publicos/cadastro")
    public String showCadastro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "publicos/cadastroCliente";
    }

    @PostMapping("/publicos/cadastrar")
    public String saveCliente(Cliente cliente) {
        System.out.println(cliente.getNome());
        cliente.setRole("CLIENTE");

        userService.save(cliente);
        return "index";
    }

    // Apenas para debug
    // @GetMapping("/")
    // public String testBD(Model model) {
    // List<Cliente> liCli = clienteRep.findAll();
    // for (Cliente cliente : liCli) {
    // System.out.println("smt " + cliente.getNome());
    // }
    // model.addAttribute("clientes", clienteRep.findAll());
    // model.addAttribute("loc", locadoraRep.findAll());

    // return "publicos/testeBD";
    // }

}

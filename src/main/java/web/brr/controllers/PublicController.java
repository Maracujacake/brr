package web.brr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.brr.domains.Cliente;
import web.brr.domains.Locadora;
import web.brr.service.impl.ClienteService;
import web.brr.service.impl.LocadoraService;

@Controller
@RequestMapping("/publicos")
public class PublicController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private LocadoraService locadoraService;

    @GetMapping("/cliente/cadastro")
    public String showCadastro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "publicos/cadastroCliente";
    }

    @PostMapping("/cliente/cadastrar")
    public String saveCliente(Cliente cliente) {
        System.out.println(cliente.getNome());
        Cliente cli = clienteService.save(cliente);
        if (cli == null) {
            return "redirect:/publicos/cliente/cadastro?error";
        }
        return "index";
    }

    @GetMapping("/locadora/cadastro")
    public String showCadastroLocadora(Model model) {
        model.addAttribute("loc", new Locadora());
        return "publicos/cadastroLocadora";
    }

    @PostMapping("/locadora/cadastrar")
    public String saveLocadora(Locadora locadora) {
        System.out.println(locadora.getNome() + " ----- " + locadora.getCidade() + " -- " + locadora.getEmail());
        Locadora cli = locadoraService.save(locadora);
        if (cli == null) {
            return "redirect:/publicos/locadora/cadastro?error";
        }
        return "index";
    }
}

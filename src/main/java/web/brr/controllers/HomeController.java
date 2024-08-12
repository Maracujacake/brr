package web.brr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "loginPages/loginPage";
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

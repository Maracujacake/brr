package web.brr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import web.brr.repositories.ClienteRep;

@Controller
public class ClientController {

    @Autowired
    private ClienteRep clienteRep;

    @GetMapping("/cliente")
    public String loginPage(Model model) {

        return "clientePage/index";
    }

}

package web.brr.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import web.brr.domains.Cliente;
import web.brr.domains.Locadora;
import web.brr.service.impl.ClienteService;
import web.brr.service.impl.LocadoraService;
import web.brr.service.impl.ObjectValidatorService;
@Controller
@RequestMapping("/publicos")
public class PublicController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private LocadoraService locadoraService;

    private ObjectValidatorService objectValidatorService = new ObjectValidatorService();


    @GetMapping("/cliente/cadastro")
    public String showCadastro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "publicos/cadastroCliente";
    }

    @PostMapping("/cliente/cadastrar")
    public String saveCliente(Cliente cliente) {
        cliente.setRole("ROLE_CLIENTE");
        List<String> errors = objectValidatorService.validate(cliente);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
        }
        Cliente cli = clienteService.save(cliente);
        if (cli == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Dados Invalidos");
        }
        return "index";
    }

    @GetMapping("/locadora")
    public String showLocadora(Model model) {
        List<Locadora> listaLocadora = locadoraService.findAll();
        model.addAttribute("listaLocadora", listaLocadora);

        return "publicos/listAllLocadora";
    }

    @GetMapping("/locadora/byName")
    public String showLocadoraByName() {
   
        return "publicos/locadoraByName";
    }

    @GetMapping("/locadora/LocbyName")
    public String showLocadoraByName(Model model, @RequestParam("name") String name) {
        List<Locadora> listaLocadora = locadoraService.findByNomeContaining(name);
        model.addAttribute("listaLocadora", listaLocadora);
        return "publicos/listByNameLocadora";
    }
    
    @GetMapping("/locadora/ByCidade")
    public String showLocadoraByCidade() {
   
        return "publicos/locadoraByCidade";
    }

    @GetMapping("/locadora/LocbyCidade")
    public String showLocadoraByCidade(Model model, @RequestParam("cidade") String cidade) {
        List<Locadora> listaLocadora = locadoraService.findByCidade(cidade);
        model.addAttribute("listaLocadora", listaLocadora);
        return "publicos/listByCidade";
    }

    @GetMapping("/locadora/cadastro")
    public String showCadastroLocadora(Model model) {
        model.addAttribute("loc", new Locadora());
        return "publicos/cadastroLocadora";
    }
    

    @PostMapping("/locadora/cadastrar")
    public String saveLocadora(Locadora locadora) {
        Locadora cli = locadoraService.save(locadora,false);
        if (cli == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Dados Invalidos");
        }
        return "index";
    }
}

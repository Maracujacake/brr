package web.brr.controllers;

import java.util.List;
import java.util.Optional;

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
import web.brr.domains.User;
import web.brr.service.impl.ClienteService;
import web.brr.service.impl.LocadoraService;
import web.brr.service.impl.ObjectValidatorService;
import web.brr.service.impl.UserService;

@Controller
@RequestMapping("/publicos")
public class PublicController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private LocadoraService locadoraService;

    @Autowired
    private UserService userService;

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
        Optional<User> user = userService.findByEmail(cliente.getEmail());
        Optional<Cliente> existCli = clienteService.findByCpf(cliente.getCpf());
        Optional<Cliente> existTel = clienteService.findByTelefone(cliente.getTelefone());
        if (user.isPresent() || existCli.isPresent() || existTel.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Dados Invalidos");

        clienteService.save(cliente, false);

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
        locadora.setRole("ROLE_LOCADORA");
        List<String> errors = objectValidatorService.validate(locadora);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
        }
        Optional<User> user = userService.findByEmail(locadora.getEmail());
        Optional<Locadora> existCli = locadoraService.findByCnpj(locadora.getCnpj());

        if (user.isPresent() || existCli.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Dados Invalidos");
        }

        locadoraService.save(locadora, false);

        return "index";
    }

    @GetMapping("/sobre")
    public String showSobre() {
        return "publicos/sobre";
    }

    @GetMapping("/painelLocadora")
    public String showPainelLocadora() {
        return "publicos/telaInicialLocadora";
    }
}

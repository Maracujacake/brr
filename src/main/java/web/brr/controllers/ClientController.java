package web.brr.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import web.brr.domains.Cliente;
import web.brr.domains.Locacao;
import web.brr.domains.Locadora;
import web.brr.domains.User;
import web.brr.encrypt.EncryptPassword;
import web.brr.service.impl.ClienteService;
import web.brr.service.impl.LocacaoService;
import web.brr.service.impl.LocadoraService;
import web.brr.service.impl.ObjectValidatorService;
import web.brr.service.impl.SendGridEmailService;
import web.brr.service.impl.UserService;

@Controller
@RequestMapping("/cliente")
public class ClientController {

    private ClienteService clienteService;
    private final LocadoraService locadoraService;
    private final LocacaoService locacaoService;
    private final UserService userService;
    private final SendGridEmailService sendGridEmailService;

    @Autowired
    public ClientController(LocadoraService locadoraService,
            LocacaoService locacaoService,
            UserService userService,
            SendGridEmailService sendGridEmailService, ClienteService clienteService) {
        this.locadoraService = locadoraService;
        this.locacaoService = locacaoService;
        this.userService = userService;
        this.sendGridEmailService = sendGridEmailService;
        this.clienteService = clienteService;
    }

    private ObjectValidatorService objectValidatorService = new ObjectValidatorService();

    @GetMapping("/")
    public String loginPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = clienteService.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("currentUser", user);
        }
        return "clientePage/index";
    }

    @GetMapping("/locacao")
    public String getLocacoes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Cliente logged = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = clienteService.findByEmail(userDetails.getUsername()).orElse(null);
            logged = (Cliente) user;
            model.addAttribute("currentUser", user);
        }
        List<Locacao> locacao = clienteService.findRegistrations(logged.getId().toString());
        model.addAttribute("locacao", locacao);
        return "clientePage/locacao";
    }

    @GetMapping("/registrar")
    public String registrarLocacao(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Cliente logged = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = clienteService.findByEmail(userDetails.getUsername()).orElse(null);
            // logged =(Cliente) user;
            model.addAttribute("currentUser", user);
        }

        List<Locadora> locadoras = locadoraService.findAll();
        model.addAttribute("locadoras", locadoras);

        return "clientePage/registrarLocacao";
    }

    @PostMapping("/registrar")
    public String registrarLoc(@RequestParam("locadoraId") Long locadoraId,
            @RequestParam("locacaoDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime locacaoDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Cliente logged = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = clienteService.findByEmail(userDetails.getUsername()).orElse(null);
            logged = (Cliente) user;
        }

        if (logged == null)
            throw new RuntimeException("Usuário não encontrado");
        Optional<Locadora> locadora = locadoraService.findById(locadoraId);
        if (!locadora.isPresent())
            throw new RuntimeException("Locadora não encontrada");

        Locacao locacao = new Locacao();
        locacao.setCliente(logged);
        locacao.setLocadora(locadora.get());
        locacao.setRegisteredAt(locacaoDate);

        List<String> errors = objectValidatorService.validate(locacao);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
        }

        // Verificar se alguem ja tem essa locacao
        List<Locacao> locacoes = locacaoService.findLocacaoClienteByDate(locacao.getRegisteredAt(),
                locacao.getCliente().getId().toString());
        locacoes.addAll(locacaoService.findLocacaoLocadoraByDate(locacao.getRegisteredAt(),
                locacao.getLocadora().getId().toString()));
        if (!locacoes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe uma locação para essa data");
        }

        Boolean result = this.sendGridEmailService.sendEmail(logged.getEmail(), "Locação Registrada",
                "Locação registrada com sucesso para a data " + locacao.getRegisteredAt() + " na locadora "
                        + locacao.getLocadora().getNome());
        if (!result)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao enviar email");
        result = this.sendGridEmailService.sendEmail(locacao.getLocadora().getEmail(), "Locação Registrada",
                "Locação registrada com sucesso para a data " + locacao.getRegisteredAt() + " com o cliente "
                        + logged.getNome());
        if (!result)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao enviar email");
        locacaoService.save(locacao);
        return "redirect:registrar";
    }

    @GetMapping("/perfil")
    public String editarDados(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Cliente cli = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = clienteService.findByEmail(userDetails.getUsername()).orElse(null);
            cli = (Cliente) user;
            model.addAttribute("currentUser", user);
        }
        model.addAttribute("cliente", cli);
        return "clientePage/perfil";
    }

    @PostMapping("/perfil")
    public String editarDados(Cliente cliente) {
        cliente.setRole("ROLE_CLIENTE");
        String newPasswd = cliente.getSenha();
        cliente.setSenha(clienteService.findById(cliente.getId()).get().getSenha());
        if (newPasswd != null && !newPasswd.isEmpty()) {
            newPasswd = EncryptPassword.encrypt(newPasswd);
            cliente.setSenha(newPasswd);
        }
        List<String> errors = objectValidatorService.validate(cliente);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
        }

        Long id = cliente.getId();
        Optional<User> user = userService.findByEmail(cliente.getEmail());
        Optional<Cliente> existCli = clienteService.findByCpf(cliente.getCpf());
        Optional<Cliente> existTel = clienteService.findByTelefone(cliente.getTelefone());
        if ((user.isPresent() && user.get().getId() != id) ||
                (existCli.isPresent() && existCli.get().getId() != id) ||
                (existTel.isPresent() && existTel.get().getId() != id))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Dados Invalidos");

        clienteService.save(cliente, true);
        return "redirect:/cliente/perfil";
    }

}

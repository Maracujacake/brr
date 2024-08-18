package web.brr.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import web.brr.service.impl.ObjectValidatorService;
import web.brr.service.impl.UserService;
import web.brr.domains.Admin;
import web.brr.domains.Cliente;
import web.brr.domains.Locadora;
import web.brr.domains.User;
import web.brr.encrypt.EncryptPassword;
import web.brr.service.impl.AdminService;
import web.brr.service.impl.ClienteService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UserService userService;

    private ObjectValidatorService objectValidatorService = new ObjectValidatorService();


    // **** Admin
    @GetMapping("/")
    public String loginPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = adminService.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("currentUser", user);
        }
        return "adminPage/index";
    }

    @GetMapping("/editar")
    public String editarPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin adm = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = adminService.findByEmail(userDetails.getUsername()).orElse(null);
            adm = (Admin) user;
            model.addAttribute("currentUser", user);
            System.out.println(user.getEmail());
        }
        model.addAttribute("admin", adm);
        return "adminPage/edicao";
    }

    @PostMapping("/editar")
    public String editarAdmin(Admin admin) {
        admin.setRole("ROLE_ADMIN");
        String newPasswd = admin.getSenha();
        admin.setSenha(adminService.findById(admin.getId()).get().getSenha());
        if (newPasswd != null && !newPasswd.isEmpty()) {
            newPasswd = EncryptPassword.encrypt(newPasswd);
            admin.setSenha(newPasswd);
        }
        List<String> errors = objectValidatorService.validate(admin);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
        }
        Admin adm = adminService.findByEmail(admin.getEmail()).orElse(null);

        if (adm != null && adm.getId() != admin.getId()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Dados Invalidos");
        }


        adminService.save(admin, true);
        return "redirect:/admin/";
    }

    // **** Clientes
    @GetMapping("/clientes")
    public String clientesPage(Model model) {
        List<Cliente> clientes = adminService.findAllClients();
        model.addAttribute("clientes", clientes);
        return "adminPage/clientes";
    }

    @GetMapping("/excluirCliente")
    public String excluirCliente(Model model, @RequestParam("clienteId") Long clienteId) {
        adminService.deleteClienteById(clienteId);
        return "redirect:/admin/";
    }

    @GetMapping("atualizarCliente")
    public String atualizarCliente(Model model, @RequestParam("clienteId") Long clienteId) {
        Cliente cliente = adminService.findClienteById(clienteId).orElse(null);
        model.addAttribute("cliente", cliente);
        return "adminPage/atualizarCliente";
    }
    

    @PostMapping("atualizarCliente")
    public String atualizarCliente(Cliente cliente) {
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

        adminService.save(cliente, true);
        return "redirect:/admin/clientes";
    }
    
    // **** Locadoras
    @GetMapping("atualizarLocadora")
    public String atualizarLocadora(Model model, @RequestParam("locadoraId") Long locadoraId) {
        Locadora locadora = adminService.findLocadoraById(locadoraId).orElse(null);
        model.addAttribute("locadora", locadora);
        return "adminPage/atualizarLocadora";
    }

    @PostMapping("atualizarLocadora")
    public String atualizarLocadora(Locadora locadora) {
        locadora.setRole("ROLE_LOCADORA");
        String newPasswd = locadora.getSenha();
        locadora.setSenha(adminService.findLocadoraById(locadora.getId()).get().getSenha());
        if (newPasswd != null && !newPasswd.isEmpty()) {
            newPasswd = EncryptPassword.encrypt(newPasswd);
            locadora.setSenha(newPasswd);
        }
        List<String> errors = objectValidatorService.validate(locadora);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
        }

        Locadora loc = adminService.findLocadoraByEmail(locadora.getEmail()).orElse(null);
        if (loc != null && loc.getId() != locadora.getId()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Dados Invalidos");
        }
        adminService.save(locadora, true);
        return "redirect:/admin/";
    }

    @GetMapping("/excluirLocadora")
    public String excluirLocadora(Model model, @RequestParam("locadoraId") Long locadoraId) {
    
        adminService.deleteLocadoraById(locadoraId);
        return "redirect:/admin/";
    }


}

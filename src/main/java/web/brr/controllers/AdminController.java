package web.brr.controllers;

import java.util.List;

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
import org.springframework.web.server.ResponseStatusException;

import web.brr.service.impl.ObjectValidatorService;
import web.brr.domains.Admin;
import web.brr.domains.User;
import web.brr.encrypt.EncryptPassword;
import web.brr.service.impl.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    private ObjectValidatorService objectValidatorService = new ObjectValidatorService();

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
}

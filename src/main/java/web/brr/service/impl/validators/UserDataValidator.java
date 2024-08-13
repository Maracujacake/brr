package web.brr.service.impl.validators;

import web.brr.service.spec.DataValidatorServiceSpec;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import web.brr.domains.User;

enum Role {
    ROLE_ADMIN, ROLE_CLIENTE, ROLE_LOCADORA
};
@Component
public class UserDataValidator implements DataValidatorServiceSpec {
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }
    
    public boolean supports(Class<?> clazz, String attrName) {
        if(attrName.equals("user")) {
            return supports(clazz);
        }
        return false;
    }

    @Override
    public List<String> validate(Object target) {
       
        User user = (User) target;
        List<String> errors = new ArrayList<>();


        if (user.getNome() == null || user.getNome().isEmpty()) {
            errors.add("Nome é obrigatorio");
        }

        if(user.getNome().length() > 100){
            errors.add("Nome deve ter no maximo 100 caracteres");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            errors.add("Email é obrigatorio");
        }

        if(user.getEmail().length() > 100){
            errors.add("Email deve ter no maximo 100 caracteres");
        }

        if (user.getSenha() == null || user.getSenha().isEmpty()) {
            errors.add("Senha é obrigatorio");
        }

        if(user.getRole() == null || user.getRole().isEmpty()){
            errors.add("Role é obrigatorio");
        }else{
            if(Role.valueOf(user.getRole()) == null){
                errors.add("Role invalido");
            }
        }

        

        return errors;
    }   
    
}

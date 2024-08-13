package web.brr.service.impl.validators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import web.brr.service.spec.DataValidatorServiceSpec;

@Component
public class EmailDataValidator implements DataValidatorServiceSpec {

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);    
    }
    public boolean supports(Class<?> clazz, String attrName) {
        if(attrName.equals("email")) {
            return supports(clazz);
        }
        return false;
    }
    @Override
    public List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();
  
        String email = (String) target;
        if (email == null || email.isEmpty()) {
            errors.add("Email é obrigatorio");
        }
        if(email.contains("@") == false){
            errors.add("Email invalido");
        }

        if(email.contains(".")==false){
            errors.add("Email invalido");
        }
        
        if(email.length() < 3 ){
            errors.add("Email invalido");
        }

        return errors;
    }
    
}

package web.brr.service.impl.validators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import web.brr.service.spec.DataValidatorServiceSpec;

enum Sexo {
    M, F, O, N
}
@Component
public class SexoDataValidator implements DataValidatorServiceSpec{
    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);    
    }
    public boolean supports(Class<?> clazz, String attrName) {
        if(attrName.equals("sexo")) {
            return supports(clazz);
        }
        return false;
    }
    @Override
    public List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();
        
        String sexo = (String) target;
        if(sexo == null || sexo.isEmpty()){
            errors.add("Sexo é obrigatorio");
        }
        if(sexo.length() != 1){
            errors.add("Sexo invalido");
        }

        if(Sexo.valueOf(sexo) == null){
            errors.add("Sexo invalido");
        }

        return errors;
    }
}

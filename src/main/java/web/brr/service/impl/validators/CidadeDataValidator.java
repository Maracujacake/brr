package web.brr.service.impl.validators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import web.brr.service.spec.DataValidatorServiceSpec;
@Component
public class CidadeDataValidator implements DataValidatorServiceSpec{
    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    public boolean supports(Class<?> clazz, String attrName) {
        if(attrName.equals("cidade")) {
            return supports(clazz);
        }
        return false;
    }

    @Override
    public List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();
 

        String Cidade = (String) target;
        if(Cidade == null || Cidade.isEmpty()){
            errors.add("Cidade é obrigatorio");
        }
        if(Cidade.length() > 50){
            errors.add("Nome da cidade muito grande.");
        }

        Cidade = Cidade.replaceAll("[^0-9]", "");
        if(Cidade.length() > 0){
            errors.add("Cidade invalida");
        }

        return errors;
    }
}


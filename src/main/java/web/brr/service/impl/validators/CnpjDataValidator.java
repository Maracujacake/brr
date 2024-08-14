package web.brr.service.impl.validators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import web.brr.service.spec.DataValidatorServiceSpec;
@Component
public class CnpjDataValidator implements DataValidatorServiceSpec{
    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    public boolean supports(Class<?> clazz, String attrName) {
        if(attrName.equals("cnpj")) {
            return supports(clazz);
        }
        return false;
    }

    @Override
    public List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();
 

        String cnpj = (String) target;
        if(cnpj == null || cnpj.isEmpty()){
            errors.add("Cnpj é obrigatorio");
        }

        // TODO: Não faz sentido verificar a formatação, apenas se é um número válido. Mas se precisar, fazer aqui.

        cnpj = cnpj.replaceAll("[^0-9]", "");
        // cnpj xxx xxx xxx xx
        if(cnpj.length() != 14){
            errors.add("Cnpj invalido");
        }

        return errors;
    }
}


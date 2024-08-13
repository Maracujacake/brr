package web.brr.service.impl.validators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import web.brr.service.spec.DataValidatorServiceSpec;
@Component
public class CpfDataValidator implements DataValidatorServiceSpec{
    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    public boolean supports(Class<?> clazz, String attrName) {
        if(attrName.equals("cpf")) {
            return supports(clazz);
        }
        return false;
    }

    @Override
    public List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();
 

        String cpf = (String) target;
        if(cpf == null || cpf.isEmpty()){
            errors.add("cpf é obrigatorio");
        }

        // TODO: Não faz sentido verificar a formatação, apenas se é um número válido. Mas se precisar, fazer aqui.

        cpf = cpf.replaceAll("[^0-9]", "");
        // CPF xxx xxx xxx xx
        if(cpf.length() != 11){
            errors.add("CPF invalido");
        }

        return errors;
    }
}

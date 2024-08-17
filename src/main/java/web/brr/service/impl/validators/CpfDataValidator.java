package web.brr.service.impl.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import web.brr.service.spec.DataValidatorServiceSpec;

@Component
public class CpfDataValidator implements DataValidatorServiceSpec {
    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    public boolean supports(Class<?> clazz, String attrName) {
        if (attrName.equals("cpf")) {
            return supports(clazz);
        }
        return false;
    }

    @Override
    public List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();

        String cpf = (String) target;
        if (cpf == null || cpf.isEmpty()) {
            errors.add("cpf é obrigatorio");
        }

        String pattern = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$";

        if (!Pattern.matches(pattern, cpf)) {
            errors.add("CPF invalido");
            return errors;
        }

        cpf = cpf.replaceAll("[^0-9]", "");
        // CPF xxx xxx xxx xx
        if (cpf.length() != 11) {
            errors.add("CPF invalido");
        }

        return errors;
    }
}

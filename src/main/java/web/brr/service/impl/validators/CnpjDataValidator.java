package web.brr.service.impl.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import web.brr.service.spec.DataValidatorServiceSpec;

@Component
public class CnpjDataValidator implements DataValidatorServiceSpec {
    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    public boolean supports(Class<?> clazz, String attrName) {
        if (attrName.equals("cnpj")) {
            return supports(clazz);
        }
        return false;
    }

    @Override
    public List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();

        String cnpj = (String) target;
        if (cnpj == null || cnpj.isEmpty()) {
            errors.add("Cnpj é obrigatorio");
        }

        String pattern = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$";

        if (!Pattern.matches(pattern, cnpj)) {
            errors.add("Cnpj invalido");
            return errors;
        }

        cnpj = cnpj.replaceAll("[^0-9]", "");
        if (cnpj.length() != 14) {
            errors.add("Cnpj invalido");
        }

        return errors;
    }
}

package web.brr.service.impl.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import web.brr.service.spec.DataValidatorServiceSpec;

@Component
public class TelefoneDataValidator implements DataValidatorServiceSpec {
    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    public boolean supports(Class<?> clazz, String attrName) {
        if (attrName.equals("telefone")) {
            return supports(clazz);
        }
        return false;
    }

    @Override
    public List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();

        String telefone = (String) target;

        if (telefone == null || telefone.isEmpty()) {
            errors.add("Telefone é obrigatorio");
        }
        String pattern = "^\\(\\d{2}\\) \\d{5}-\\d{4}$";

        if (!Pattern.matches(pattern, telefone)) {
            errors.add("Telefone invalido");
            return errors;
        }
        telefone = telefone.replaceAll("[^0-9]", "");

        // xx x xxxx xxxx telefone pode ter 8, 9 ou 11 digitos
        int telLength = telefone.length();
        if (telLength > 11 || telLength < 8 || telLength == 10) {
            errors.add("Telefone invalido");
        }

        return errors;
    }

}

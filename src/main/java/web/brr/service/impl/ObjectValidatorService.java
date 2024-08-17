package web.brr.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.brr.service.impl.validators.*;
import web.brr.service.spec.ObjectValidatorSpec;

@Service
public class ObjectValidatorService implements ObjectValidatorSpec {
    @Autowired
    private ValidatorService validatorService = new ValidatorService();

    public ObjectValidatorService() {

        validatorService.addValidator(new CpfDataValidator());
        validatorService.addValidator(new EmailDataValidator());
        validatorService.addValidator(new SexoDataValidator());
        validatorService.addValidator(new TelefoneDataValidator());
        validatorService.addValidator(new UserDataValidator());
        validatorService.addValidator(new DateDataValidator());
        validatorService.addValidator(new CnpjDataValidator());
        validatorService.addValidator(new CidadeDataValidator());
    }

    /**
     * Valida uma classe e seus campos. Portanto é esperado receber objetos como
     * Cliente, User, Locadora, Admin .
     * 
     * @param target The object to be validated.
     * @return A list of validation errors, if any.
     */
    @Override
    public List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();
        errors.addAll(validatorService.validate(target)); // valida objeto inteiro
        if (!errors.isEmpty()) {
            return errors;
        }
        Field[] fields = target.getClass().getDeclaredFields();
        fields = Stream
                .concat(Arrays.stream(fields), Arrays.stream(target.getClass().getSuperclass().getDeclaredFields()))
                .toArray(Field[]::new);

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(target);
                if (value != null) {
                    errors.addAll(validatorService.validate(value, field.getName()));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return errors;
    }
}
package web.brr.service.impl.validators;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import web.brr.service.spec.DataValidatorServiceSpec;

@Component
public class DateDataValidator implements DataValidatorServiceSpec {
    private boolean isLocalTime = false;

    @Override
    public boolean supports(Class<?> clazz) {
        isLocalTime = LocalDateTime.class.isAssignableFrom(clazz);
        return isLocalTime || LocalDate.class.isAssignableFrom(clazz);
    }

    public boolean supports(Class<?> clazz, String attrName) {
        if (attrName.equals("dataNascimento") || attrName.equals("registeredAt")) {
            return supports(clazz);
        }
        return false;
    }

    @Override
    public List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();

        if (isLocalTime) {
            LocalDateTime date = (LocalDateTime) target;
            errors = validateLocalTime((LocalDateTime) date);
            return errors;
        }
        errors = validateLocalTime((LocalDate) target);

        return errors;
    }

    private List<String> validateLocalTime(LocalDate data) {
        List<String> errors = new ArrayList<>();
        ChronoLocalDate currentDate = LocalDate.now();
        if (data.isAfter(currentDate)) {
            errors.add("Data não pode ser maior que a data atual");
        }
        LocalDate today = LocalDate.now();
        LocalDate minDate = today.minusYears(100);
        LocalDate maxDate = today.plusYears(100);

        if (data.isBefore(minDate) || data.isAfter(maxDate)) {
            errors.add("Data deve estar no intervalo de -100 anos a +100 anos a partir da data atual");
        }

        return errors;
    }

    private List<String> validateLocalTime(LocalDateTime data) {
        List<String> errors = new ArrayList<>();
        LocalDateTime currentDate = LocalDateTime.now();
        if (data.isBefore(currentDate)) {
            errors.add("Data não pode ser menor que a data atual");
        }
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime maxDate = today.plusYears(100);

        if (data.isAfter(maxDate)) {
            errors.add("Data deve estar no intervalo +100 anos a partir da data atual");
        }

        if (data.getMinute() != 0) {
            errors.add("A data deve ser exata, sem minutos");
        }

        return errors;
    }
}

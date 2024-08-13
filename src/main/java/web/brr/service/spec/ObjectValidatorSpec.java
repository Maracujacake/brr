package web.brr.service.spec;

import java.util.List;

public interface ObjectValidatorSpec {
    /**
     * Validates the given object.
     *
     * @param target the object to be validated
     * @return true if the object is valid, false otherwise
     */
    List<String> validate(Object target);
}

package web.brr.service.spec;

import java.util.List;

public interface DataValidatorServiceSpec {

    /**
     * Determines if the specified class is supported by this DataValidatorServiceSpec.
     *
     * @param clazz the class to check for support
     * @return true if the class is supported, false otherwise
     */
    boolean supports(Class<?> clazz);

    /**
     * Determines if the specified class and attribute name are supported by this DataValidatorService.
     *
     * @param clazz The class to check if it is supported.
     * @param attrName The attribute name to check if it is supported.
     * @return true if the class and attribute name are supported, false otherwise.
     */
    boolean supports(Class<?> clazz, String attrName);

    /**
     * Validates the given object.
     *
     * @param target the object to be validated
     * @return true if the object is valid, false otherwise
     */
    List<String> validate(Object target);
}

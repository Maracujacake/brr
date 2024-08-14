package web.brr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import web.brr.service.spec.DataValidatorServiceSpec;

@Service
public class ValidatorService {
    private final List<DataValidatorServiceSpec> validators;
    private String attrName = null;

    public String getAttrName(){
        return attrName;
    }


    public ValidatorService() {
        this.validators = new ArrayList<DataValidatorServiceSpec>();
    }

    public void addValidator(DataValidatorServiceSpec validator) {
        validators.add(validator);
    }

    public List<String> validate(Object target) {
        List<String> errors = new ArrayList<>();
        for (DataValidatorServiceSpec validator : validators) {
            if (validator.supports(target.getClass())) {
                errors.addAll(validator.validate(target));
            }
        }
        if (errors.isEmpty()) {
            System.out.println(target.getClass().getName());
        }
        return errors;
    }

    public List<String> validate(Object target, String attrName) {
        this.attrName = attrName;
        List<String> errors = new ArrayList<>();
        for (DataValidatorServiceSpec validator : validators) {
            if (validator.supports(target.getClass(), attrName)) {
                errors.addAll(validator.validate(target));
            }
        }
        if (!errors.isEmpty()) {
            System.out.println(target.getClass().getName());
        }
        return errors;
       
    }
}
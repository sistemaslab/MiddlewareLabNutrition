package blacksip.vtex.client.exception;

import blacksip.vtex.client.apiStatus.ApiStatus;
import blacksip.vtex.client.errors.ValidationFieldError;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alejandro Cadena
 */
public class FieldValidationException extends ServiceException {
    
    private ApiStatus appStatus;
    private List<ValidationFieldError> validationFieldErrors;

    public FieldValidationException(String message) {
        super(message);
        this.validationFieldErrors = new ArrayList();
        this.appStatus = ApiStatus.VALIDATION_ERROR;
    }
    
    public FieldValidationException(FieldValidationException ex) {
        super(ex);
        this.validationFieldErrors = ex.getValidationFieldErrors();
        this.appStatus = ApiStatus.VALIDATION_ERROR;
    }
    
    public FieldValidationException(ValidationFieldError validationError) {
        this.validationFieldErrors = new ArrayList();
        this.validationFieldErrors.add(validationError);
        this.appStatus = ApiStatus.VALIDATION_ERROR;
    }
    
    public FieldValidationException(List<ValidationFieldError> validationErrors) {
        this.validationFieldErrors = validationErrors;
        this.appStatus = ApiStatus.VALIDATION_ERROR;
    }
    
    @Override
    public ApiStatus getAppStatus() {
        return appStatus;
    }

    public List<ValidationFieldError> getValidationFieldErrors() {
        return validationFieldErrors;
    }

    public void setValidationFieldErrors(List<ValidationFieldError> validationFieldErrors) {
        this.validationFieldErrors = validationFieldErrors;
    }
    
    public void addValitationError(ValidationFieldError validationError) {
        this.validationFieldErrors.add(validationError);
    }
    
    public void addValitationErrors(FieldValidationException ex) {
        this.validationFieldErrors.addAll(ex.getValidationFieldErrors());
    }
    
    public String getSummaryErrors(){
        String summaryErrors = "";
        for(ValidationFieldError validationFieldError : validationFieldErrors){
            summaryErrors += validationFieldError.getMessage()+", ";
        }
        if(summaryErrors.length()>2){
            summaryErrors = summaryErrors.substring(0, summaryErrors.length()-2);
        }
        return summaryErrors;
    }
    
}

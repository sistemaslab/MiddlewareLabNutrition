package blacksip.vtex.client.errors;

import blacksip.vtex.client.apiStatus.ApiStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Alejandro Cadena
 */
public class ApiValidationError implements InterfaceError{
    
    private ApiStatus code;
    private String codeId;
    private String message;
    private List<ValidationFieldError> errors;
    
    public ApiValidationError(ApiStatus code, ValidationFieldError error) {
        super();
        this.code = code;
        this.codeId = code.getAppCode();
        this.message = code.getDescription();
        this.errors = Arrays.asList(error);
    }
    
    public ApiValidationError(ApiStatus code, List<ValidationFieldError> errors) {
        super();
        this.code = code;
        this.codeId = code.getAppCode();
        this.message = code.getDescription();
        this.errors = errors;
    }

    public ApiStatus getCode() {
        return code;
    }

    public String getCodeId() {
        return codeId;
    }
    
    public HttpStatus getHttpStatus() {
        return code.getHttpStatus();
    }

    public String getMessage() {
        return message;
    }

    public List<ValidationFieldError> getErrors() {
        return errors;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrors(List<ValidationFieldError> errors) {
        this.errors = errors;
    }
    
    public void putError(ValidationFieldError error) {
        if(this.errors == null){
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }
    
}

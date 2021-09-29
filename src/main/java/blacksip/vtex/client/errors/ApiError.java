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
public class ApiError implements InterfaceError{
    
    private ApiStatus code;
    private String codeId;
    private String message;
    private List<String> errors;

    public ApiError(ApiStatus code, String message, String error) {
        super();
        this.code = code;
        this.codeId = code.getAppCode();
        this.message = message;
        this.errors = Arrays.asList(error);
    }
    
    public ApiError(ApiStatus code, String message, List<String> errors) {
        super();
        this.code = code;
        this.codeId = code.getAppCode();
        this.message = message;
        this.errors = errors;
    }

    public ApiError(ApiStatus code, String error) {
        super();
        this.code = code;
        this.codeId = code.getAppCode();
        this.message = code.getDescription();
        this.errors = Arrays.asList(error);
    }
    
    public ApiError(ApiStatus code, List<String> errors) {
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

    public List<String> getErrors() {
        return errors;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(List<String> errors) {
        this.errors = errors;
    }
    
    public void putError(String error) {
        if(this.errors == null){
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }
    
}

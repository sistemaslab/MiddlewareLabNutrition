package blacksip.vtex.client.errors;

import blacksip.vtex.client.apiStatus.ApiStatus;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alejandro Cadena
 */
public class ValidationFieldError implements InterfaceError{
    
    private ApiStatus code;
    private String codeId;
    private String field;
    private String message;
    
    /**
     * Método constructor para crear error de validación
     * @param code codigo del error de aplicación
     * @param field nombre del campo o atributo a ser validado
     * @param message mensaje que describe el error de validación presentado
     */
    public ValidationFieldError(ApiStatus code, String field, String message) {
        this.code = code;
        this.codeId = code.getAppCode();
        this.field = field;
        this.message = message;
    }

    /**
     * Método constructor para crear error de validación
     * El mensaje que describe el error se inicializa por defecto con el valor
     * que describe el código de error de aplicación.
     * @param code código del error de aplicación
     * @param field nombre del campo o atributo a ser validado
     */
    public ValidationFieldError(ApiStatus code, String field) {
        this.code = code;
        this.codeId = code.getAppCode();
        this.message = code.getDescription();
    }
    
    /**
     * Metodo que retorna un error de validación en el caso de que el campo 
     * requerido sea nulo o esté vacío
     * 
     * @param fieldValue valor del campo
     * @param fieldName nombre del campo
     * @return error de validación si el campo requerido es nulo o está vacío,
     * nulo en caso contrario
     */
    public static ValidationFieldError getValidateRequiredFieldError(Object fieldValue, String fieldName){
        ApiStatus appStatus;
        String validateMessage;
        if (ValidateField.requiredFieldIsNull(fieldValue)){
            appStatus = ApiStatus.FIELD_IS_REQUIRED;
            validateMessage = ValidateFieldMessage.getRequiredMessage(fieldName);
        }else if(ValidateField.requiredFieldIsEmpty(fieldValue)){
            appStatus = ApiStatus.FIELD_CAN_NOT_BE_EMPTY;
            validateMessage = ValidateFieldMessage.getEmptyMessage(fieldName);
        }else{
            return null;
        }
        return new ValidationFieldError(appStatus, fieldName, validateMessage);
    }
    
    /**
     * Metodo que agrega un error de validación a la lista de errores en el 
     * caso de que el campo requerido sea nulo o esté vacío
     * @param fieldValue valor del campo
     * @param fieldName nombre del campo
     * @param errors lista de errores actualizada
     */
    public static void addValidateRequiredFieldError(Object fieldValue, String fieldName, List<InterfaceError> errors){
        if(errors == null){
            errors = new ArrayList();
        }
        InterfaceError interfaceError;
        interfaceError = ValidationFieldError.getValidateRequiredFieldError(fieldValue, fieldName);
        if(interfaceError != null){
            errors.add(interfaceError);
        }
    }
    
    /**
     * Metodo que retorna un error de validación en el caso de que el campo 
     * requerido sea no sea de tipo entero
     * @param fieldValue valor del campo
     * @param fieldName nombre del campo
     * @return error de validación si el campo requerido no es entero,
     * nulo en caso contrario
     */
    public static ValidationFieldError getValidateIntegerFieldError(Object fieldValue, String fieldName){
        if (ValidateField.isNotInteger(fieldValue)){
            ApiStatus appStatus = ApiStatus.FIELD_MUST_BE_INTEGER;
            String validateMessage = ValidateFieldMessage.getRequiredMessage(fieldName);
            return new ValidationFieldError(appStatus, fieldName, validateMessage);
        }else{
            return null;
        }
    }
    
    /**
     * Metodo que agrega un error de validación a la lista de errores en el 
     * caso de que el campo no sea de tipo entero
     * @param fieldValue valor del campo
     * @param fieldName nombre del campo
     * @param errors lista de errores actualizada
     */
    public static void addValidateIntegerFieldError(Object fieldValue, String fieldName, List<InterfaceError> errors){
        if(errors == null){
            errors = new ArrayList();
        }
        InterfaceError interfaceError;
        interfaceError = ValidationFieldError.getValidateIntegerFieldError(fieldValue, fieldName);
        if(interfaceError != null){
            errors.add(interfaceError);
        }
    }

    /**
     * retorna el código del error de aplicación
     * @return código del error de aplicación
     */
    public ApiStatus getCode() {
        return code;
    }

    /**
     * Retorna el identificador del error de aplicación
     * @return identificador del error de aplicación
     */
    public String getCodeId() {
        return codeId;
    }

    /**
     * retorna el nombre del campo o atributo
     * @return nombre del campo o atributo
     */
    public String getField() {
        return field;
    }

    /**
     * Retorna mensaje que describe el error de validación
     * @return mensaje que describe error de validación
     */
    public String getMessage() {
        return message;
    }

    /**
     * Asigna un valor nuevo al mensaje que describe el error de validación
     * @param message nuevo valor a asignar al mensade que describe el error de
     * validación
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
}

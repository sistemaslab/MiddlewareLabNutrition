package blacksip.vtex.client.errors;

import org.json.JSONObject;

/**
 *
 * @author Alejandro Cadena
 */
public class ValidateFieldMessage {
    
    /**
     * Valida que el campo tipo Objecto no sea nulo ni esté en en blanco
     * @param fieldValue valor del campo
     * @param fieldName nombre del campo requerido
     * @return String vacío si no hubo error, o String con descripción del error
     * en caso contrario
     */
    public static String requiredField(Object fieldValue, String fieldName){
        String error = "";
        if(ValidateField.requiredFieldIsNull(fieldValue)){
            error = getRequiredMessage(fieldName);
        }else if(ValidateField.requiredFieldIsEmpty(fieldValue)){
            error = getEmptyMessage(fieldName);
        }
        return error;
    }
    
    /**
     * Valida que el campo sea de tipo entero
     * @param fieldValue valor del campo
     * @param fieldName nombre del campo requerido
     * @return String vacío si no hubo error, o String con descripción del error
     * en caso contrario
     */
    public static String integerField(Object fieldValue, String fieldName){
        String error = "";
        if(ValidateField.isNotInteger(fieldValue)){
            error = getMustBeIntegerMessage(fieldName);
        }
        return error;
    }
    
    /**
     * Valida que el campo tipo Objecto no sea nulo
     * @param jsonData json a validar
     * @param fieldName nombre del campo requerido
     * @return String vacío si no hubo error, o String con descripción del error
     * en caso contrario
     */
    public static String requiredField(JSONObject jsonData, String fieldName){
        String error = "";
        if(ValidateField.requiredFieldIsNull(jsonData, fieldName)){
            error = getRequiredMessage(fieldName);
        }else if(ValidateField.requiredFieldIsEmpty(jsonData, fieldName)){
            error = getEmptyMessage(fieldName);
        }
        return error;
    }
    
    /**
     * Valida que el campo sea de tipo entero
     * @param jsonData json a validar
     * @param fieldName nombre del campo requerido
     * @return String vacío si no hubo error, o String con descripción del error
     * en caso contrario
     */
    public static String integerField(JSONObject jsonData, String fieldName){
        String error = "";
        if(ValidateField.isNotInteger(jsonData, fieldName)){
            error = getMustBeIntegerMessage(fieldName);
        }
        return error;
    }
    
    /**
     *
     * @param fieldName
     * @return
     */
    public static String getRequiredMessage(String fieldName){
        return "The field << "+fieldName+" >> is required. ";
    }
    
    /**
     *
     * @param fieldName
     * @return
     */
    public static String getEmptyMessage(String fieldName){
        return "The field << "+fieldName+" >> is required. ";
    }
    
    /**
     *
     * @param fieldName
     * @return
     */
    public static String getMustBeIntegerMessage(String fieldName){
        return "The field << "+fieldName+" >> is empty. ";
    }
    
    
    
    
}

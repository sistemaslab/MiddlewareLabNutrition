package blacksip.vtex.client.errors;

import blacksip.vtex.client.apiStatus.ApiStatus;
import java.math.BigInteger;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author Alejandro Cadena
 */
public class ValidateField {
    
    /**
     * 
     * @param fieldValue
     * @return 
     */
    public static boolean isEmptyOrNull(Object fieldValue){
        if(requiredFieldIsNull(fieldValue)){
            return true;
        } else if(requiredFieldIsEmpty(fieldValue)){
            return true;
        }
        return false;
    }
    
    /**
     * Valida que el campo tipo Objecto no sea nulo
     * @param fieldValue valor del campo
     * @return String vacío si no hubo error, o String con descripción del error
     * en caso contrario
     */
    public static boolean requiredFieldIsNull(Object fieldValue){
        return fieldValue == null;
    }
    
    /**
     * Valida que el campo no esté en blanco o esté vacío
     * @param fieldValue valor del campo
     * @return true si el campo está en blanco o vacío, false en caso contrario
     */
    public static boolean requiredFieldIsEmpty(Object fieldValue){
        if(fieldValue instanceof String){
            String fieldString = String.valueOf(fieldValue);
            if(fieldString.equals("")){
                return true;
            }
        }else if(fieldValue instanceof List){
            List fieldList = (List)fieldValue;
            if (fieldList.isEmpty()){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Valida que el campo sea de tipo entero
     * @param fieldValue valor del campo
     * @return true si el campo no es de tipo entero, false en caso contrario
     */
    public static boolean isNotInteger(Object fieldValue){
        String fieldString = String.valueOf(fieldValue);
        try{
            Integer.parseInt(fieldString);
        }catch(NumberFormatException ex){
            return true;
        }catch(Exception ex){
            try{
                new BigInteger(fieldString);
            }catch(Exception ex2){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Valida que el campo del json exista y no tenga valor nulo
     * @param jsonData json a validar
     * @param fieldName nombre del campo requerido
     * @return true si el campo no existe o tiene valor nulo, false en caso
     * contrario
     */
    public static boolean requiredFieldIsNull(JSONObject jsonData, String fieldName){
        if( ! jsonData.has(fieldName) ){
            return true;
        }else{
            Object fieldValue = jsonData.get(fieldName);
            if(fieldValue == null){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Valida que el campo del json no esté en blanco o esté vacío
     * @param jsonData json a validar
     * @param fieldName nombre del campo requerido
     * @return true si el campo está en blanco o vacío, false en caso
     * contrario
     */
    public static boolean requiredFieldIsEmpty(JSONObject jsonData, String fieldName){
        if( ! requiredFieldIsNull(jsonData, fieldName) ){
            Object fieldValue = jsonData.get(fieldName);
            if(fieldValue instanceof String){
                String fieldString = String.valueOf(fieldValue);
                if(fieldString.equals("")){
                    return true;
                }
            }else if(fieldValue instanceof List){
                List fieldList = (List)fieldValue;
                if (fieldList.isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Valida que el campo del json sea de tipo entero
     * @param jsonData valor del campo
     * @param fieldName nombre del campo a validar
     * @return true si el campo no es de tipo entero, false en caso contrario
     */
    public static boolean isNotInteger(JSONObject jsonData, String fieldName){
        if( ! jsonData.has(fieldName) ){
            return true;
        }else{
            Object fieldValue = jsonData.get(fieldName);
            return isNotInteger(fieldValue);
        }
    }
    
    /**
     * 
     * @param appStatus
     * @param fieldName
     * @return 
     */
    public static String getValidationMessage(ApiStatus appStatus, String fieldName){
        String message;
        if(null == appStatus){
            String errorMessage = "FieldValidationException(AppStatus appStatus, "
                    + "String field) - constructor: appStatus can't be null";
            throw new RuntimeException(errorMessage);
        }else switch (appStatus) {
            case FIELD_CAN_NOT_BE_EMPTY:
                message = "The field << "+fieldName+" >> can't be empty";
                break;
            case FIELD_IS_REQUIRED:
                message = "The field << "+fieldName+" >> is required";
                break;
            case FIELD_MUST_BE_INTEGER:
                message = "The field << "+fieldName+" >> must be integer";
                break;
            case FIELD_MUST_BE_UNIQUE:
                message = "The field << "+fieldName+" >> must be unique";
                break;
            case VALIDATION_ERROR:
                message = "Default validation field error";
                break;
            default:
                message = "Default validation field error";
                break;
        }
        return message;
    }
    
}

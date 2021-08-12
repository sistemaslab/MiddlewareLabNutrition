package blacksip.vtex.client.errors;

import blacksip.vtex.client.apiStatus.ApiStatus;
import blacksip.vtex.client.exception.FieldValidationException;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro Cadena
 * @param <Model> model
 */
@Component
public class FieldValidator<Model> {
    
    protected final Class<Model> classModel;
    
    public FieldValidator() {
        this.classModel = (Class<Model>) GenericTypeResolver.resolveTypeArgument(getClass(), FieldValidator.class);
    }
    
    /**
     * Valida que el campo tipo Objecto no sea nulo
     * @param fieldValue valor del campo
     * @return String vacío si no hubo error, o String con descripción del error
     * en caso contrario
     */
    private static boolean requiredFieldIsNull(Object fieldValue){
        return fieldValue == null;
    }
    
    /**
     * Valida que el campo no esté en blanco o esté vacío
     * @param fieldValue valor del campo
     * @return true si el campo está en blanco o vacío, false en caso contrario
     */
    private static boolean requiredFieldIsEmpty(Object fieldValue){
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
     *
     * @param fieldValue
     * @return
     */
    private static boolean isEmptyOrNull(Object fieldValue){
        if(requiredFieldIsNull(fieldValue)){
            return true;
        } else if(requiredFieldIsEmpty(fieldValue)){
            return true;
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
     *
     * @param appStatus
     * @param fieldName
     * @return
     */
    private static String getValidationMessage(ApiStatus appStatus, String fieldName){
        return ValidateField.getValidationMessage(appStatus, fieldName);
    }
    
    /**
     * 
     * @param modelObject
     * @param field
     * @return
     * @throws FieldValidationException 
     */
    private Object getFieldValue(Model modelObject, Field field) throws FieldValidationException{
        try {
            Object fieldValue = field.get(modelObject);
            return fieldValue;
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(FieldValidator.class.getName()).log(Level.SEVERE, null, ex);
            String message = modelObject.getClass().getSimpleName();
            message += ": no se pudo extraer valor del atributo \""+field.getName()+"\"";
            ValidationFieldError validationFieldError = new ValidationFieldError(
                    ApiStatus.VALIDATION_ERROR, field.getName(),  message);
            throw new FieldValidationException(validationFieldError);
        }
    }
    
    /**
     * 
     * @param modelObject
     * @param name
     * @return
     * @throws FieldValidationException 
     */
    public Field getFieldByName(Object modelObject, String name) throws FieldValidationException{
        try{
            Field field = modelObject.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (SecurityException | NoSuchFieldException ex) {
            Logger.getLogger(FieldValidator.class.getName()).log(Level.SEVERE, null, ex);
            String message = modelObject.getClass().getSimpleName();
            message += ": no se pudo extraer el atributo \""+name
                    +"\". Asegurese de que la clase <"+modelObject.getClass().getSimpleName()
                    +"> tiene un atributo con ese nombre. ";
            ValidationFieldError validationFieldError = new ValidationFieldError(
                    ApiStatus.VALIDATION_ERROR, name,  message);
            throw new FieldValidationException(validationFieldError);
        }
    }
    
}

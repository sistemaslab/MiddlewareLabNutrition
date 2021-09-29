package blacksip.vtex.client.exception;

import blacksip.vtex.client.apiStatus.ApiStatus;

/**
 *
 * @author Alejandro Cadena
 */
public class ModelMappingException extends ServiceException {

    public ModelMappingException() {
        super("Could not mapping Model");
    }

    public ModelMappingException(Throwable cause) {
        super("Could not mapping Model", cause);
    }
    
    public ModelMappingException(Object obj) {
        super("Could not mapping "+obj.getClass().getSimpleName()+"");
    }
    
    public ModelMappingException(Throwable cause, Object obj) {
        super("Could not mapping "+obj.getClass().getSimpleName()+"", cause);
    }

    @Override
    public ApiStatus getAppStatus() {
        return ApiStatus.UNPROCESSABLE_REQUEST_ENTITY;
    }
    
}

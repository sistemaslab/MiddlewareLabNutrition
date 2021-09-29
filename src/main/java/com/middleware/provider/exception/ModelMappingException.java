package com.middleware.provider.exception;

import com.middleware.provider.utils.ApiStatus;

/**
*
* @author Natalia Manrique
*/
public class ModelMappingException extends ServiceException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

package com.middleware.provider.exception;

import com.middleware.provider.utils.ApiStatus;

public abstract class ServiceException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException() {
    }

    public ServiceException(String message) {
        super("ServiceException: "+message);
    }

    public ServiceException(String message, Throwable cause) {
        super("ServiceException: "+message, cause);
    }

    public ServiceException(Throwable cause) {
        super("ServiceException: "+cause.getMessage(), cause);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("ServiceException: "+message, cause, enableSuppression, writableStackTrace);
    }
    
    public abstract ApiStatus getAppStatus();

}
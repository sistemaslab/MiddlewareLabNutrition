package com.middleware.provider.exception;

import com.middleware.provider.utils.ApiStatus;

/**
 *
 * @author Natalia Manrique
 */
public class HeaderNotFoundException extends ServiceException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public ApiStatus getAppStatus() {
        return ApiStatus.BAD_REQUEST;
    }

    public HeaderNotFoundException() {
        super("Missing Headers");
    }

    public HeaderNotFoundException(String message) {
        super("Missing Headers " + message);
    }

    public HeaderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public HeaderNotFoundException(Throwable cause) {
        super(cause);
    }

    public HeaderNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

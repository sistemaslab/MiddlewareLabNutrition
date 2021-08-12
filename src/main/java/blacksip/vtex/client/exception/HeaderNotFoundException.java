/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blacksip.vtex.client.exception;

import blacksip.vtex.client.apiStatus.ApiStatus;

/**
 *
 * @author Jonathan Briceno
 */
public class HeaderNotFoundException extends ServiceException {

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

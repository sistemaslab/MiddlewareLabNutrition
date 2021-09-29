package blacksip.vtex.client.exception;

import blacksip.vtex.client.apiStatus.ApiStatus;

/**
 *
 * @author Alejandro Cadena
 */
public class ServiceFileException extends ServiceException {

    public ServiceFileException(String message) {
        super(message);
    }

    public ServiceFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceFileException(Throwable cause) {
        super(cause);
    }
    
    @Override
    public ApiStatus getAppStatus() {
        return ApiStatus.UPLOAD_FAILED;
    }    
    
}

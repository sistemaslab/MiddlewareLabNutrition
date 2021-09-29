package com.middleware.provider.exception;

import java.net.URI;
import java.util.Date;

/**
*
* @author Natalia Manrique
*/
public class ServiceConnectionException extends Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uri;
    private Date exceptionTime;

    public ServiceConnectionException(String uri, String message) {
        super(message);
        this.uri = uri;
        this.exceptionTime = new Date();
    }
    
    public ServiceConnectionException(URI uri, String message) {
        super(message);
        this.uri = uri.toString();
        this.exceptionTime = new Date();
    }
    
    public ServiceConnectionException(URI uri, Throwable throwable) {
        super(throwable);
        this.uri = uri.toString();
        this.exceptionTime = new Date();
    }
    
    public ServiceConnectionException(String uri, Throwable throwable) {
        super(throwable);
        this.uri = uri;
        this.exceptionTime = new Date();
    }

    public String getUri() {
        return uri;
    }

    public Date getExceptionTime() {
        return exceptionTime;
    }

}

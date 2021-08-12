package blacksip.vtex.client.exception;

import blacksip.vtex.client.apiStatus.ApiStatus;
import java.net.URI;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.ws.soap.client.SoapFaultClientException;

/**
 *
 * @author Alejandro Cadena
 */
public class ServiceConnectionException extends Exception {
    
    private String uri;
    private Date exceptionTime;
    private HttpClientErrorException hcee;
    private SoapFaultClientException sfce;
    
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(ServiceConnectionException.class);

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
    
    public ServiceConnectionException(HttpClientErrorException hcee, URI uri) {
        super(hcee);
        this.hcee = hcee;
        this.uri = uri.toString();
        this.exceptionTime = new Date();
    }
    
    public ServiceConnectionException(HttpClientErrorException hcee, String uri) {
        super(hcee);
        this.hcee = hcee;
        this.uri = uri;
        this.exceptionTime = new Date();
    }
    
    public ServiceConnectionException(SoapFaultClientException sfce, String uri) {
        super(sfce);
        this.sfce = sfce;
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
    
    public String getResponseBody(){
        if(hcee != null){
            return hcee.getResponseBodyAsString();
        }else if(sfce != null){
            return sfce.getFaultStringOrReason();
        }
        return "No response";
    }
    
    public String getResponseStatus(){
        if(hcee != null){
            return hcee.getStatusText();
        }
        return "Internal Server Error";
    }
    
    public ApiStatus getApiStatus(){
        String standarResponseStatus = this.getResponseStatus().toUpperCase().replace(" ", "_");
        if(standarResponseStatus.contains("GATEWAY_TIMEOUT")){
            standarResponseStatus = "GATEWAY_TIMEOUT";
        }
        HttpStatus httpStatus;
        try{
            httpStatus = HttpStatus.valueOf(standarResponseStatus);
        }catch(Exception ex){
            LOGGER.error("getApiStatus - getHttpStatus from value: "+standarResponseStatus, ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ApiStatus.fromHttpStatus(httpStatus);
    }

}

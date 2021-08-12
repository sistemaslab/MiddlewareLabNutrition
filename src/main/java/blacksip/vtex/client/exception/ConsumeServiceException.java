package blacksip.vtex.client.exception;

import java.util.Date;

/**
 *
 * @author Alejandro Cadena
 */
public class ConsumeServiceException extends Exception {
    
    private String codeId;
    private Date exceptionTime;

    public ConsumeServiceException(String codeId, String message) {
        super(message);
        this.codeId = codeId;
        this.exceptionTime = new Date();
    }
    
    public ConsumeServiceException(String message) {
        super(message);
        this.codeId = "UNKNOWN";
        this.exceptionTime = new Date();
    }
    
    public ConsumeServiceException(Throwable throwable) {
        super(throwable);
        this.codeId = "UNKNOWN";
        this.exceptionTime = new Date();
    }
    
    public ConsumeServiceException(String codeId, Throwable throwable) {
        super(throwable);
        this.codeId = codeId;
        this.exceptionTime = new Date();
    }

    public String getCodeId() {
        return codeId;
    }

    public Date getExceptionTime() {
        return exceptionTime;
    }
    
}

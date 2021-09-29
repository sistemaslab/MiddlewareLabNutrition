package blacksip.vtex.client.apiStatus;

import org.springframework.http.HttpStatus;

/**
 *
 * @author Alejandro Cadena
 */
public enum ApiStatus{
    
    OK ("0200", HttpStatus.OK, "Ok"),
    CREATED ("0201", HttpStatus.CREATED, "Created"),
    BAD_REQUEST ("0400", HttpStatus.BAD_REQUEST, "Bad request"),
    UNAUTHORIZED ("0401", HttpStatus.UNAUTHORIZED, "Unauthorized"),
    NOT_FOUND ("0404", HttpStatus.NOT_FOUND, "Not found"),
    METHOD_NOT_ALLOWED ("0405", HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed"),
    UNSUPPORTED_MEDIA_TYPE ("0415", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type"),
    VALIDATION_ERROR ("0452", HttpStatus.BAD_REQUEST, "Error validating parameters"),
    FIELD_IS_REQUIRED ("0453", HttpStatus.BAD_REQUEST, "The field is required"),
    FIELD_CAN_NOT_BE_EMPTY ("0454", HttpStatus.BAD_REQUEST, "The field must not be empty"),
    FIELD_CAN_NOT_BE_EMPTY_OR_NULL ("0455", HttpStatus.BAD_REQUEST, "The field must not be empty or null"),
    FIELD_MUST_BE_INTEGER ("0456", HttpStatus.BAD_REQUEST, "The field must be numeric"),
    FIELD_MUST_BE_UNIQUE ("0457", HttpStatus.BAD_REQUEST, "The field must be unique"),
    FIELD_WITH_INVALID_SIZE ("0458", HttpStatus.BAD_REQUEST, "Field with invalid size"),
    OUT_OF_RANGE ("0459", HttpStatus.BAD_REQUEST, "Value out of range"),
    DATE_OUT_OF_RANGE ("0460", HttpStatus.BAD_REQUEST, "Date out of range"),
    INVALID_OPTION ("0461", HttpStatus.BAD_REQUEST, "The value entered does not correspond to a possible option"),
    INVALID_PARAMETER_TYPE("0462", HttpStatus.BAD_REQUEST, "Invalid parameter type"),
    UPLOAD_FAILED ("0480", HttpStatus.BAD_REQUEST, "The file could not be loaded"),
    OBJECT_NOT_FOUND ("0604", HttpStatus.NOT_FOUND, "The object was not found"),
    UNPROCESSABLE_REQUEST_ENTITY ("0422", HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable request entity: invalid json format"),
    SERVICE_UNAVAILABLE ("0522", HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable"),
    EMPTY ("0204", HttpStatus.OK, "Empty result"),
    INTERNAL_ERROR ("0500", HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    HTTP_CLIENT_ERROR ("0520", HttpStatus.INTERNAL_SERVER_ERROR, "Partner application error: http client error exception"),
    REST_CLIENT_EXCEPTION ("0521", HttpStatus.INTERNAL_SERVER_ERROR, "Partner application error: rest client exception");
    
    private String appCode;
    private HttpStatus httpStatus;
    private String description;
    
    private ApiStatus(String appCode, HttpStatus httpStatus, String description) {
        this.appCode =  appCode;
        this.httpStatus = httpStatus;
        this.description = description;
    }

    public String getAppCode() {
        return appCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setCustomDescription(String description) {
        this.description = description;
    }
    
    public static ApiStatus fromCode(String appCode){
        for (ApiStatus value : ApiStatus.values()) {
            if (value.appCode.equals(appCode)) {
                return value;
            }
        }
        return null;
    }
    
    public static ApiStatus fromHttpStatus(HttpStatus httpStatus){
        for (ApiStatus value : ApiStatus.values()) {
            if (value.httpStatus == httpStatus) {
                return value;
            }
        }
        return ApiStatus.INTERNAL_ERROR;
    }
    
    public static String getCustomNotFoundMessage(String objectName, String idValue){
        return objectName+" with <"+idValue+"> not found";
    }
    
    public static String getCustomNotFoundMessage(Class<?> aClass, String idValue){
        String objectName = aClass.getSimpleName();
        return objectName+" with <"+idValue+"> not found";
    }

    @Override
    public String toString() {
        return "ApiStatus{" + "appCode=" + appCode + ", httpStatus=" + httpStatus + ", description=" + description + '}';
    }
    
    
    
}

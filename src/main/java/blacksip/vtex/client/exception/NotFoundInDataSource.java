package blacksip.vtex.client.exception;

import blacksip.vtex.client.apiStatus.ApiStatus;

/**
 *
 * @author Alejandro Cadena
 */
public class NotFoundInDataSource extends ServiceException {

    private Object primaryKey;

    public NotFoundInDataSource(Object primaryKey) {
        super("Object with '"+primaryKey+"' not Found");
        this.primaryKey = primaryKey;
    }
    
    @Override
    public ApiStatus getAppStatus() {
        return ApiStatus.OBJECT_NOT_FOUND;
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }
    
    
    
}

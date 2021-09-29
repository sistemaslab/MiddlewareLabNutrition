package blacksip.vtex.client.log;

import blacksip.vtex.client.apiStatus.ApiStatus;
import blacksip.vtex.client.apiStatus.ApiComponent;
import blacksip.vtex.client.apiStatus.Trace;
import java.util.Date;

public interface TraceabilityLogFacadeInterface {
    
    public void addTraceabilityJson(ApiComponent component, Trace trace, String objectRefId, String urlService, Object request, Date requestDate,
            Object response, Date responseDate, ApiStatus status, String detailMessage);
    
    public void addTraceabilityXml(ApiComponent component, Trace trace, String objectRefId, String urlService, Object request, Date requestDate,
            Object response, Date responseDate, ApiStatus status, String detailMessage);

}

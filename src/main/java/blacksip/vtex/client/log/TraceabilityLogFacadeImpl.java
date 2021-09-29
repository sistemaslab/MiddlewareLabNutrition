package blacksip.vtex.client.log;

import blacksip.vtex.client.apiStatus.ApiStatus;
import blacksip.vtex.client.apiStatus.Trace;
import com.middleware.provider.utils.Parser;
import java.util.Date;

import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class TraceabilityLogFacadeImpl implements TraceabilityLogFacadeInterface{
    
    private static final Logger LOGGER = LogManager.getLogger(TraceabilityLogFacadeImpl.class);
    
    @Override
    public void addTraceabilityJson(blacksip.vtex.client.apiStatus.ApiComponent component, Trace trace, String objectRefId, String urlService, Object request, Date requestDate,
            Object response, Date responseDate, ApiStatus status, String detailMessage) {
        if(status == ApiStatus.OK || status == ApiStatus.CREATED || status == ApiStatus.EMPTY){
            status = ApiStatus.OK;
        }
        String msg = "component: "+component.getName()+", ";
        msg += "traceName: "+trace.getName()+", ";
        msg += "objectRefId: "+objectRefId+", ";
        msg += "urlService: "+objectRefId+", ";
        msg += "request: "+Parser.objToJsonString(request)+", ";
        msg += "requestDate: "+requestDate+", ";
        msg += "response: "+Parser.objToJsonString(response)+", ";
        msg += "responseDate: "+responseDate+", ";
        msg += "status: "+status+", ";
        msg += "detailMessage: "+detailMessage+" ";
        LOGGER.debug(msg);
    }
    
    @Override
    public void addTraceabilityXml(blacksip.vtex.client.apiStatus.ApiComponent component, Trace trace, String objectRefId, String urlService, Object request, Date requestDate,
            Object response, Date responseDate, ApiStatus status, String detailMessage) {
        if(status == ApiStatus.OK || status == ApiStatus.CREATED || status == ApiStatus.EMPTY){
            status = ApiStatus.OK;
        }
        String msg = "component: "+component.getName()+", ";
        msg += "traceName: "+trace.getName()+", ";
        msg += "objectRefId: "+objectRefId+", ";
        msg += "urlService: "+objectRefId+", ";
        msg += "request: "+Parser.xmlToString(request)+", ";
        msg += "requestDate: "+requestDate+", ";
        msg += "response: "+Parser.xmlToString(response)+", ";
        msg += "responseDate: "+responseDate+", ";
        msg += "status: "+status+", ";
        msg += "detailMessage: "+detailMessage+" ";
        LOGGER.debug(msg);
    }
}

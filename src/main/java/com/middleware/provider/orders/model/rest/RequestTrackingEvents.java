package com.middleware.provider.orders.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RequestTrackingEvents {
	
	private boolean isDelivered;     	
	private Events events;     

    public RequestTrackingEvents(boolean isDelivered, Events events) {
        this.isDelivered = isDelivered;
        this.events = events;
    }

    public boolean isIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

	   
}

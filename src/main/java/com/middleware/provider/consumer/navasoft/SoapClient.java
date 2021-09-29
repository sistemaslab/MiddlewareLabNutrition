package com.middleware.provider.consumer.navasoft;

import lab.navasoft.model.soap.Getitems;
import lab.navasoft.model.soap.GetitemsResponse;
import lab.navasoft.model.soap.Getnewitems;
import lab.navasoft.model.soap.GetnewitemsResponse;
import lab.navasoft.model.soap.Getpreciolista;
import lab.navasoft.model.soap.GetpreciolistaResponse;
import lab.navasoft.model.soap.Getstatuspedido;
import lab.navasoft.model.soap.GetstatuspedidoResponse;
import lab.navasoft.model.soap.Getstockalm;
import lab.navasoft.model.soap.GetstockalmResponse;
import lab.navasoft.model.soap.Loadbilldata;
import lab.navasoft.model.soap.LoadbilldataResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class SoapClient extends WebServiceGatewaySupport {

    public LoadbilldataResponse setOrder(Loadbilldata loadbilldata, String url, String callback) {

        LoadbilldataResponse response = (LoadbilldataResponse) getWebServiceTemplate()
                .marshalSendAndReceive(url, loadbilldata, new SoapActionCallback(callback));

        return response;
    }

    public GetstatuspedidoResponse getInvoice(Getstatuspedido getstatuspedido, String url, String callback) {

        GetstatuspedidoResponse response = (GetstatuspedidoResponse) getWebServiceTemplate()
                .marshalSendAndReceive(url, getstatuspedido, new SoapActionCallback(callback));

        return response;
    }

    public GetpreciolistaResponse getPrice(Getpreciolista getpreciolista, String url, String callback) {

        GetpreciolistaResponse response = (GetpreciolistaResponse) getWebServiceTemplate()
                .marshalSendAndReceive(url, getpreciolista, new SoapActionCallback(callback));

        return response;
    }

    public GetstockalmResponse getStock(Getstockalm getstockalm, String url, String callback) {

        GetstockalmResponse response = (GetstockalmResponse) getWebServiceTemplate()
                .marshalSendAndReceive(url, getstockalm, new SoapActionCallback(callback));

        return response;
    }

    public GetnewitemsResponse getProducts(Getnewitems getnewitems, String url, String callback) {

        GetnewitemsResponse response = (GetnewitemsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(url, getnewitems, new SoapActionCallback(callback));

        return response;
    }
    
      public GetitemsResponse getProductById(Getitems getitems, String url, String callback) {

        GetitemsResponse response = (GetitemsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(url, getitems, new SoapActionCallback(callback));

        return response;
    }
}

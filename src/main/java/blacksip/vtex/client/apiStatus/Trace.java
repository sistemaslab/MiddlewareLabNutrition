package blacksip.vtex.client.apiStatus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alejandro Cadena
 */
public enum Trace{
    
    INVENTORY ("inventario"),
    PRICE ("precio"),
    PRODUCT ("producto"),
    SKU ("sku"),
    SPECIFICATION ("especificacion"),
    IMAGE ("imagen"),
    CLIENT ("cliente"),
    ORDER ("pedido"),
    INVOICE ("factura"),
    REMITTANCE ("guia"),
    TRACKING ("traza de envio"),
    LOG ("log de envio");
    
    private String name;
    
    private Trace(String name) {
        this.name =  name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Trace fromName(String name){
        for (Trace value : Trace.values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    public static List<String> getAllTraceNames(){
        List<String> traceNames = new ArrayList();
        for (Trace value : Trace.values()) {
            traceNames.add(value.getName());
        }
        return traceNames;
    }
    
}

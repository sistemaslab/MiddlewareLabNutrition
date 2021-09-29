//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.3.0 
// Visite <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2021.09.29 a las 05:05:33 PM COT 
//


package lab.navasoft.model.soap;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the lab.navasoft.model.soap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _String_QNAME = new QName("http://prolineapp.pe/", "string");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: lab.navasoft.model.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Loadbilldata }
     * 
     */
    public Loadbilldata createLoadbilldata() {
        return new Loadbilldata();
    }

    /**
     * Create an instance of {@link LoadbilldataResponse }
     * 
     */
    public LoadbilldataResponse createLoadbilldataResponse() {
        return new LoadbilldataResponse();
    }

    /**
     * Create an instance of {@link Getstockalm }
     * 
     */
    public Getstockalm createGetstockalm() {
        return new Getstockalm();
    }

    /**
     * Create an instance of {@link GetstockalmResponse }
     * 
     */
    public GetstockalmResponse createGetstockalmResponse() {
        return new GetstockalmResponse();
    }

    /**
     * Create an instance of {@link Getpreciolista }
     * 
     */
    public Getpreciolista createGetpreciolista() {
        return new Getpreciolista();
    }

    /**
     * Create an instance of {@link GetpreciolistaResponse }
     * 
     */
    public GetpreciolistaResponse createGetpreciolistaResponse() {
        return new GetpreciolistaResponse();
    }

    /**
     * Create an instance of {@link Getnewitems }
     * 
     */
    public Getnewitems createGetnewitems() {
        return new Getnewitems();
    }

    /**
     * Create an instance of {@link GetnewitemsResponse }
     * 
     */
    public GetnewitemsResponse createGetnewitemsResponse() {
        return new GetnewitemsResponse();
    }

    /**
     * Create an instance of {@link Getitems }
     * 
     */
    public Getitems createGetitems() {
        return new Getitems();
    }

    /**
     * Create an instance of {@link GetitemsResponse }
     * 
     */
    public GetitemsResponse createGetitemsResponse() {
        return new GetitemsResponse();
    }

    /**
     * Create an instance of {@link Getstatuspedido }
     * 
     */
    public Getstatuspedido createGetstatuspedido() {
        return new Getstatuspedido();
    }

    /**
     * Create an instance of {@link GetstatuspedidoResponse }
     * 
     */
    public GetstatuspedidoResponse createGetstatuspedidoResponse() {
        return new GetstatuspedidoResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://prolineapp.pe/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

}

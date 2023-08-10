
package tyler.ecf.v5_0.extensions.recordservice;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the tyler.ecf.v5_0.extensions.recordservice package. 
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

    private final static QName _RecordServiceMessage_QNAME = new QName("urn:tyler:ecf:v5.0:extensions:recordservice", "RecordServiceMessage");
    private final static QName _RecordServiceMessageAugmentationPoint_QNAME = new QName("urn:tyler:ecf:v5.0:extensions:recordservice", "RecordServiceMessageAugmentationPoint");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: tyler.ecf.v5_0.extensions.recordservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RecordServiceMessageType }
     * 
     */
    public RecordServiceMessageType createRecordServiceMessageType() {
        return new RecordServiceMessageType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecordServiceMessageType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RecordServiceMessageType }{@code >}
     */
    @XmlElementDecl(namespace = "urn:tyler:ecf:v5.0:extensions:recordservice", name = "RecordServiceMessage")
    public JAXBElement<RecordServiceMessageType> createRecordServiceMessage(RecordServiceMessageType value) {
        return new JAXBElement<RecordServiceMessageType>(_RecordServiceMessage_QNAME, RecordServiceMessageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     */
    @XmlElementDecl(namespace = "urn:tyler:ecf:v5.0:extensions:recordservice", name = "RecordServiceMessageAugmentationPoint")
    public JAXBElement<Object> createRecordServiceMessageAugmentationPoint(Object value) {
        return new JAXBElement<Object>(_RecordServiceMessageAugmentationPoint_QNAME, Object.class, null, value);
    }

}

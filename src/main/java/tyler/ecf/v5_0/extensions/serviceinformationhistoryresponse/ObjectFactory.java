
package tyler.ecf.v5_0.extensions.serviceinformationhistoryresponse;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the tyler.ecf.v5_0.extensions.serviceinformationhistoryresponse package. 
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

    private final static QName _GetServiceInformationHistoryResponseMessage_QNAME = new QName("urn:tyler:ecf:v5.0:extensions:serviceinformationhistoryresponse", "GetServiceInformationHistoryResponseMessage");
    private final static QName _GetServiceInformationHistoryResponseMessageAugmentationPoint_QNAME = new QName("urn:tyler:ecf:v5.0:extensions:serviceinformationhistoryresponse", "GetServiceInformationHistoryResponseMessageAugmentationPoint");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: tyler.ecf.v5_0.extensions.serviceinformationhistoryresponse
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetServiceInformationHistoryResponseMessageType }
     * 
     */
    public GetServiceInformationHistoryResponseMessageType createGetServiceInformationHistoryResponseMessageType() {
        return new GetServiceInformationHistoryResponseMessageType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetServiceInformationHistoryResponseMessageType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetServiceInformationHistoryResponseMessageType }{@code >}
     */
    @XmlElementDecl(namespace = "urn:tyler:ecf:v5.0:extensions:serviceinformationhistoryresponse", name = "GetServiceInformationHistoryResponseMessage")
    public JAXBElement<GetServiceInformationHistoryResponseMessageType> createGetServiceInformationHistoryResponseMessage(GetServiceInformationHistoryResponseMessageType value) {
        return new JAXBElement<GetServiceInformationHistoryResponseMessageType>(_GetServiceInformationHistoryResponseMessage_QNAME, GetServiceInformationHistoryResponseMessageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     */
    @XmlElementDecl(namespace = "urn:tyler:ecf:v5.0:extensions:serviceinformationhistoryresponse", name = "GetServiceInformationHistoryResponseMessageAugmentationPoint")
    public JAXBElement<Object> createGetServiceInformationHistoryResponseMessageAugmentationPoint(Object value) {
        return new JAXBElement<Object>(_GetServiceInformationHistoryResponseMessageAugmentationPoint_QNAME, Object.class, null, value);
    }

}
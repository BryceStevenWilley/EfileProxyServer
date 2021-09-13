
package ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.documentrequest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ecfv5.gov.niem.release.niem.niem_core._4.DocumentType;
import ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.ecf.RequestMessageType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * The base information contained in any query message.
 * 
 * <p>Java class for GetDocumentRequestMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDocumentRequestMessageType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0/ecf}RequestMessageType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://release.niem.gov/niem/niem-core/4.0/}CaseTrackingID"/&gt;
 *         &lt;element ref="{http://release.niem.gov/niem/niem-core/4.0/}Document" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0/documentrequest}GetDocumentRequestMessageAugmentationPoint" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='urn:us:gov:ic:ntk urn:us:gov:ic:ism'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDocumentRequestMessageType", propOrder = {
    "caseTrackingID",
    "document",
    "getDocumentRequestMessageAugmentationPoint"
})
public class GetDocumentRequestMessageType
    extends RequestMessageType
{

    @XmlElement(name = "CaseTrackingID", namespace = "http://release.niem.gov/niem/niem-core/4.0/", required = true)
    protected ecfv5.gov.niem.release.niem.proxy.xsd._4.String caseTrackingID;
    @XmlElement(name = "Document", namespace = "http://release.niem.gov/niem/niem-core/4.0/", required = true, nillable = true)
    protected List<DocumentType> document;
    @XmlElement(name = "GetDocumentRequestMessageAugmentationPoint")
    protected List<Object> getDocumentRequestMessageAugmentationPoint;

    /**
     * Gets the value of the caseTrackingID property.
     * 
     * @return
     *     possible object is
     *     {@link ecfv5.gov.niem.release.niem.proxy.xsd._4.String }
     *     
     */
    public ecfv5.gov.niem.release.niem.proxy.xsd._4.String getCaseTrackingID() {
        return caseTrackingID;
    }

    /**
     * Sets the value of the caseTrackingID property.
     * 
     * @param value
     *     allowed object is
     *     {@link ecfv5.gov.niem.release.niem.proxy.xsd._4.String }
     *     
     */
    public void setCaseTrackingID(ecfv5.gov.niem.release.niem.proxy.xsd._4.String value) {
        this.caseTrackingID = value;
    }

    /**
     * Gets the value of the document property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the document property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentType }
     * 
     * 
     */
    public List<DocumentType> getDocument() {
        if (document == null) {
            document = new ArrayList<DocumentType>();
        }
        return this.document;
    }

    /**
     * Gets the value of the getDocumentRequestMessageAugmentationPoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the getDocumentRequestMessageAugmentationPoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGetDocumentRequestMessageAugmentationPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getGetDocumentRequestMessageAugmentationPoint() {
        if (getDocumentRequestMessageAugmentationPoint == null) {
            getDocumentRequestMessageAugmentationPoint = new ArrayList<Object>();
        }
        return this.getDocumentRequestMessageAugmentationPoint;
    }

    /**
     * Generates a String representation of the contents of this type.
     * This is an extension method, produced by the 'ts' xjc plugin
     * 
     */
    @Override
    public java.lang.String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

}

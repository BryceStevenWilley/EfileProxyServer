
package tyler.ecf.v5_0.extensions.recordreceipt;

import java.util.ArrayList;
import java.util.List;
import gov.niem.release.niem.domains.humanservices._4.ChildSupportEnforcementCaseType;
import gov.niem.release.niem.niem_core._4.CaseType;
import https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.ecf.CaseFilingType;
import https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.ecf.DocumentReviewType;
import https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.ecf.FilingStatusType;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import tyler.ecf.v5_0.extensions.common.FilingMessageAugmentationType;


/**
 * Message to record receipt for filing.
 * 
 * <p>Java class for RecordReceiptMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RecordReceiptMessageType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0/ecf}CaseFilingType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://release.niem.gov/niem/niem-core/4.0/}Case" minOccurs="0"/&gt;
 *         &lt;element ref="{https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0/ecf}FilingStatus"/&gt;
 *         &lt;element ref="{https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0/ecf}ConnectedDocumentReview" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0/ecf}LeadDocumentReview" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:recordreceipt}RecordReceiptMessageAugmentationPoint" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlType(name = "RecordReceiptMessageType", propOrder = {
    "_case",
    "filingStatus",
    "connectedDocumentReview",
    "leadDocumentReview",
    "recordReceiptMessageAugmentationPoint"
})
public class RecordReceiptMessageType
    extends CaseFilingType
{

    @XmlElementRef(name = "Case", namespace = "http://release.niem.gov/niem/niem-core/4.0/", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends CaseType> _case;
    @XmlElement(name = "FilingStatus", namespace = "https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0/ecf", required = true)
    protected FilingStatusType filingStatus;
    @XmlElement(name = "ConnectedDocumentReview", namespace = "https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0/ecf")
    protected List<DocumentReviewType> connectedDocumentReview;
    @XmlElement(name = "LeadDocumentReview", namespace = "https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0/ecf", required = true)
    protected List<DocumentReviewType> leadDocumentReview;
    @XmlElementRef(name = "RecordReceiptMessageAugmentationPoint", namespace = "urn:tyler:ecf:v5.0:extensions:recordreceipt", type = JAXBElement.class, required = false)
    protected List<JAXBElement<?>> recordReceiptMessageAugmentationPoint;

    /**
     * Gets the value of the case property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ChildSupportEnforcementCaseType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CaseType }{@code >}
     *     
     */
    public JAXBElement<? extends CaseType> getCase() {
        return _case;
    }

    /**
     * Sets the value of the case property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ChildSupportEnforcementCaseType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CaseType }{@code >}
     *     
     */
    public void setCase(JAXBElement<? extends CaseType> value) {
        this._case = value;
    }

    /**
     * Gets the value of the filingStatus property.
     * 
     * @return
     *     possible object is
     *     {@link FilingStatusType }
     *     
     */
    public FilingStatusType getFilingStatus() {
        return filingStatus;
    }

    /**
     * Sets the value of the filingStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilingStatusType }
     *     
     */
    public void setFilingStatus(FilingStatusType value) {
        this.filingStatus = value;
    }

    /**
     * Gets the value of the connectedDocumentReview property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the connectedDocumentReview property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnectedDocumentReview().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentReviewType }
     * 
     * 
     */
    public List<DocumentReviewType> getConnectedDocumentReview() {
        if (connectedDocumentReview == null) {
            connectedDocumentReview = new ArrayList<DocumentReviewType>();
        }
        return this.connectedDocumentReview;
    }

    /**
     * Gets the value of the leadDocumentReview property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the leadDocumentReview property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLeadDocumentReview().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentReviewType }
     * 
     * 
     */
    public List<DocumentReviewType> getLeadDocumentReview() {
        if (leadDocumentReview == null) {
            leadDocumentReview = new ArrayList<DocumentReviewType>();
        }
        return this.leadDocumentReview;
    }

    /**
     * Gets the value of the recordReceiptMessageAugmentationPoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the recordReceiptMessageAugmentationPoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecordReceiptMessageAugmentationPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link FilingMessageAugmentationType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getRecordReceiptMessageAugmentationPoint() {
        if (recordReceiptMessageAugmentationPoint == null) {
            recordReceiptMessageAugmentationPoint = new ArrayList<JAXBElement<?>>();
        }
        return this.recordReceiptMessageAugmentationPoint;
    }

    /**
     * Generates a String representation of the contents of this type.
     * This is an extension method, produced by the 'ts' xjc plugin
     * 
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

}
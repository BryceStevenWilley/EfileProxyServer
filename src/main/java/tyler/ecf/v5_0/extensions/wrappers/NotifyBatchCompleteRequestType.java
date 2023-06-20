
package tyler.ecf.v5_0.extensions.wrappers;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import tyler.ecf.v5_0.extensions.batchcallback.NotifyBatchCompleteMessageType;


/**
 * <p>Java class for NotifyBatchCompleteRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NotifyBatchCompleteRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:batchcallback}NotifyBatchCompleteMessage"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotifyBatchCompleteRequestType", propOrder = {
    "notifyBatchCompleteMessage"
})
public class NotifyBatchCompleteRequestType {

    @XmlElement(name = "NotifyBatchCompleteMessage", namespace = "urn:tyler:ecf:v5.0:extensions:batchcallback", required = true)
    protected NotifyBatchCompleteMessageType notifyBatchCompleteMessage;

    /**
     * Gets the value of the notifyBatchCompleteMessage property.
     * 
     * @return
     *     possible object is
     *     {@link NotifyBatchCompleteMessageType }
     *     
     */
    public NotifyBatchCompleteMessageType getNotifyBatchCompleteMessage() {
        return notifyBatchCompleteMessage;
    }

    /**
     * Sets the value of the notifyBatchCompleteMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link NotifyBatchCompleteMessageType }
     *     
     */
    public void setNotifyBatchCompleteMessage(NotifyBatchCompleteMessageType value) {
        this.notifyBatchCompleteMessage = value;
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
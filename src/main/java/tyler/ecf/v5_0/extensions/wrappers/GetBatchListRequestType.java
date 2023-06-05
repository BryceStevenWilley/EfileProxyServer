
package tyler.ecf.v5_0.extensions.wrappers;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import tyler.ecf.v5_0.extensions.batchlistrequest.GetBatchListMessageType;


/**
 * <p>Java class for GetBatchListRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetBatchListRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:batchlistrequest}GetBatchListMessage"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBatchListRequestType", propOrder = {
    "getBatchListMessage"
})
public class GetBatchListRequestType {

    @XmlElement(name = "GetBatchListMessage", namespace = "urn:tyler:ecf:v5.0:extensions:batchlistrequest", required = true)
    protected GetBatchListMessageType getBatchListMessage;

    /**
     * Gets the value of the getBatchListMessage property.
     * 
     * @return
     *     possible object is
     *     {@link GetBatchListMessageType }
     *     
     */
    public GetBatchListMessageType getGetBatchListMessage() {
        return getBatchListMessage;
    }

    /**
     * Sets the value of the getBatchListMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetBatchListMessageType }
     *     
     */
    public void setGetBatchListMessage(GetBatchListMessageType value) {
        this.getBatchListMessage = value;
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

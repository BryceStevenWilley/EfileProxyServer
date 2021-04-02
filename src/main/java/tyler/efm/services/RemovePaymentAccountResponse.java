
package tyler.efm.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import tyler.efm.services.schema.baseresponse.BaseResponseType;


/**
 * &lt;p&gt;Java class for anonymous complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="RemovePaymentAccountResponse" type="{urn:tyler:efm:services:schema:BaseResponse}BaseResponseType" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "removePaymentAccountResponse"
})
@XmlRootElement(name = "RemovePaymentAccountResponse")
public class RemovePaymentAccountResponse {

    @XmlElement(name = "RemovePaymentAccountResponse")
    protected BaseResponseType removePaymentAccountResponse;

    /**
     * Gets the value of the removePaymentAccountResponse property.
     * 
     * @return
     *     possible object is
     *     {@link BaseResponseType }
     *     
     */
    public BaseResponseType getRemovePaymentAccountResponse() {
        return removePaymentAccountResponse;
    }

    /**
     * Sets the value of the removePaymentAccountResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseResponseType }
     *     
     */
    public void setRemovePaymentAccountResponse(BaseResponseType value) {
        this.removePaymentAccountResponse = value;
    }

}

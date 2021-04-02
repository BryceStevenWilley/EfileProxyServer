
package tyler.efm.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import tyler.efm.services.schema.getpaymentaccountresponse.GetPaymentAccountResponseType;


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
 *         &amp;lt;element name="GetGlobalPaymentAccountResponse" type="{urn:tyler:efm:services:schema:GetPaymentAccountResponse}GetPaymentAccountResponseType" minOccurs="0"/&amp;gt;
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
    "getGlobalPaymentAccountResponse"
})
@XmlRootElement(name = "GetGlobalPaymentAccountResponse")
public class GetGlobalPaymentAccountResponse {

    @XmlElement(name = "GetGlobalPaymentAccountResponse")
    protected GetPaymentAccountResponseType getGlobalPaymentAccountResponse;

    /**
     * Gets the value of the getGlobalPaymentAccountResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GetPaymentAccountResponseType }
     *     
     */
    public GetPaymentAccountResponseType getGetGlobalPaymentAccountResponse() {
        return getGlobalPaymentAccountResponse;
    }

    /**
     * Sets the value of the getGlobalPaymentAccountResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetPaymentAccountResponseType }
     *     
     */
    public void setGetGlobalPaymentAccountResponse(GetPaymentAccountResponseType value) {
        this.getGlobalPaymentAccountResponse = value;
    }

}

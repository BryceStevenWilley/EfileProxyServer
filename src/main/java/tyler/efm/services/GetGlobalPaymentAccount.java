
package tyler.efm.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import tyler.efm.services.schema.getpaymentaccountrequest.GetPaymentAccountRequestType;


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
 *         &amp;lt;element name="GetGlobalPaymentAccountRequest" type="{urn:tyler:efm:services:schema:GetPaymentAccountRequest}GetPaymentAccountRequestType" minOccurs="0"/&amp;gt;
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
    "getGlobalPaymentAccountRequest"
})
@XmlRootElement(name = "GetGlobalPaymentAccount")
public class GetGlobalPaymentAccount {

    @XmlElement(name = "GetGlobalPaymentAccountRequest")
    protected GetPaymentAccountRequestType getGlobalPaymentAccountRequest;

    /**
     * Gets the value of the getGlobalPaymentAccountRequest property.
     * 
     * @return
     *     possible object is
     *     {@link GetPaymentAccountRequestType }
     *     
     */
    public GetPaymentAccountRequestType getGetGlobalPaymentAccountRequest() {
        return getGlobalPaymentAccountRequest;
    }

    /**
     * Sets the value of the getGlobalPaymentAccountRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetPaymentAccountRequestType }
     *     
     */
    public void setGetGlobalPaymentAccountRequest(GetPaymentAccountRequestType value) {
        this.getGlobalPaymentAccountRequest = value;
    }

}

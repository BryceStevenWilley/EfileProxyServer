
package tyler.efm.services.schema.updateattorneyrequest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import tyler.efm.services.schema.baserequest.BaseRequestType;
import tyler.efm.services.schema.common.AttorneyType;


/**
 * &lt;p&gt;Java class for UpdateAttorneyRequestType complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="UpdateAttorneyRequestType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;extension base="{urn:tyler:efm:services:schema:BaseRequest}BaseRequestType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element ref="{urn:tyler:efm:services:schema:Common}Attorney" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/extension&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateAttorneyRequestType", propOrder = {
    "attorney"
})
public class UpdateAttorneyRequestType
    extends BaseRequestType
{

    @XmlElement(name = "Attorney", namespace = "urn:tyler:efm:services:schema:Common")
    protected AttorneyType attorney;

    /**
     * Gets the value of the attorney property.
     * 
     * @return
     *     possible object is
     *     {@link AttorneyType }
     *     
     */
    public AttorneyType getAttorney() {
        return attorney;
    }

    /**
     * Sets the value of the attorney property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttorneyType }
     *     
     */
    public void setAttorney(AttorneyType value) {
        this.attorney = value;
    }

}

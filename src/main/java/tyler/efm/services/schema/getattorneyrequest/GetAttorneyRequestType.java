
package tyler.efm.services.schema.getattorneyrequest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import tyler.efm.services.schema.baserequest.BaseRequestType;


/**
 * &lt;p&gt;Java class for GetAttorneyRequestType complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="GetAttorneyRequestType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;extension base="{urn:tyler:efm:services:schema:BaseRequest}BaseRequestType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="AttorneyID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/extension&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAttorneyRequestType", propOrder = {
    "attorneyID"
})
public class GetAttorneyRequestType
    extends BaseRequestType
{

    @XmlElement(name = "AttorneyID")
    protected String attorneyID;

    /**
     * Gets the value of the attorneyID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttorneyID() {
        return attorneyID;
    }

    /**
     * Sets the value of the attorneyID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttorneyID(String value) {
        this.attorneyID = value;
    }

}

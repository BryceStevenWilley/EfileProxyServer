
package tyler.ecf.v5_0.extensions.common;

import gov.niem.release.niem.niem_core._4.TextType;
import gov.niem.release.niem.structures._4.AugmentationType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * <p>Java class for AddressAugmentationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressAugmentationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://release.niem.gov/niem/structures/4.0/}AugmentationType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}AddressSecondaryLine" minOccurs="0"/&gt;
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
@XmlType(name = "AddressAugmentationType", propOrder = {
    "addressSecondaryLine"
})
public class AddressAugmentationType
    extends AugmentationType
{

    @XmlElement(name = "AddressSecondaryLine")
    protected TextType addressSecondaryLine;

    /**
     * Gets the value of the addressSecondaryLine property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getAddressSecondaryLine() {
        return addressSecondaryLine;
    }

    /**
     * Sets the value of the addressSecondaryLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setAddressSecondaryLine(TextType value) {
        this.addressSecondaryLine = value;
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
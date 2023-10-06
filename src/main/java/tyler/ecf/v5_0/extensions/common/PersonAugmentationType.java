
package tyler.ecf.v5_0.extensions.common;

import java.util.ArrayList;
import java.util.List;
import gov.niem.release.niem.niem_core._4.DateType;
import gov.niem.release.niem.niem_core._4.IdentificationType;
import gov.niem.release.niem.niem_core._4.TextType;
import gov.niem.release.niem.proxy.xsd._4.Boolean;
import gov.niem.release.niem.structures._4.AugmentationType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * <p>Java class for PersonAugmentationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonAugmentationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://release.niem.gov/niem/structures/4.0/}AugmentationType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}PartyIdentification" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}AttorneyIdentification" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}FilerIdentification" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}GroupIdentification" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}DriverLicenseIdentification" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}FirmName" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}Alias" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}IAmThisUserIndicator" minOccurs="0"/&gt;
 *         &lt;element ref="{http://release.niem.gov/niem/niem-core/4.0/}PersonDeathDate" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}SSNIdentification" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}Agency" minOccurs="0"/&gt;
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
@XmlType(name = "PersonAugmentationType", propOrder = {
    "partyIdentification",
    "attorneyIdentification",
    "filerIdentification",
    "groupIdentification",
    "driverLicenseIdentification",
    "firmName",
    "alias",
    "iAmThisUserIndicator",
    "personDeathDate",
    "ssnIdentification",
    "agency"
})
public class PersonAugmentationType
    extends AugmentationType
{

    @XmlElement(name = "PartyIdentification")
    protected IdentificationType partyIdentification;
    @XmlElement(name = "AttorneyIdentification")
    protected IdentificationType attorneyIdentification;
    @XmlElement(name = "FilerIdentification")
    protected IdentificationType filerIdentification;
    @XmlElement(name = "GroupIdentification")
    protected IdentificationType groupIdentification;
    @XmlElement(name = "DriverLicenseIdentification")
    protected DriverLicenseIdentificationType driverLicenseIdentification;
    @XmlElement(name = "FirmName")
    protected TextType firmName;
    @XmlElement(name = "Alias")
    protected List<AliasType> alias;
    @XmlElement(name = "IAmThisUserIndicator")
    protected Boolean iAmThisUserIndicator;
    @XmlElement(name = "PersonDeathDate", namespace = "http://release.niem.gov/niem/niem-core/4.0/")
    protected DateType personDeathDate;
    @XmlElement(name = "SSNIdentification")
    protected IdentificationType ssnIdentification;
    @XmlElement(name = "Agency")
    protected AgencyType agency;

    /**
     * Gets the value of the partyIdentification property.
     * 
     * @return
     *     possible object is
     *     {@link IdentificationType }
     *     
     */
    public IdentificationType getPartyIdentification() {
        return partyIdentification;
    }

    /**
     * Sets the value of the partyIdentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificationType }
     *     
     */
    public void setPartyIdentification(IdentificationType value) {
        this.partyIdentification = value;
    }

    /**
     * Gets the value of the attorneyIdentification property.
     * 
     * @return
     *     possible object is
     *     {@link IdentificationType }
     *     
     */
    public IdentificationType getAttorneyIdentification() {
        return attorneyIdentification;
    }

    /**
     * Sets the value of the attorneyIdentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificationType }
     *     
     */
    public void setAttorneyIdentification(IdentificationType value) {
        this.attorneyIdentification = value;
    }

    /**
     * Gets the value of the filerIdentification property.
     * 
     * @return
     *     possible object is
     *     {@link IdentificationType }
     *     
     */
    public IdentificationType getFilerIdentification() {
        return filerIdentification;
    }

    /**
     * Sets the value of the filerIdentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificationType }
     *     
     */
    public void setFilerIdentification(IdentificationType value) {
        this.filerIdentification = value;
    }

    /**
     * Gets the value of the groupIdentification property.
     * 
     * @return
     *     possible object is
     *     {@link IdentificationType }
     *     
     */
    public IdentificationType getGroupIdentification() {
        return groupIdentification;
    }

    /**
     * Sets the value of the groupIdentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificationType }
     *     
     */
    public void setGroupIdentification(IdentificationType value) {
        this.groupIdentification = value;
    }

    /**
     * Gets the value of the driverLicenseIdentification property.
     * 
     * @return
     *     possible object is
     *     {@link DriverLicenseIdentificationType }
     *     
     */
    public DriverLicenseIdentificationType getDriverLicenseIdentification() {
        return driverLicenseIdentification;
    }

    /**
     * Sets the value of the driverLicenseIdentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link DriverLicenseIdentificationType }
     *     
     */
    public void setDriverLicenseIdentification(DriverLicenseIdentificationType value) {
        this.driverLicenseIdentification = value;
    }

    /**
     * Gets the value of the firmName property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getFirmName() {
        return firmName;
    }

    /**
     * Sets the value of the firmName property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setFirmName(TextType value) {
        this.firmName = value;
    }

    /**
     * Gets the value of the alias property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the alias property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlias().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AliasType }
     * 
     * 
     */
    public List<AliasType> getAlias() {
        if (alias == null) {
            alias = new ArrayList<AliasType>();
        }
        return this.alias;
    }

    /**
     * Gets the value of the iAmThisUserIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getIAmThisUserIndicator() {
        return iAmThisUserIndicator;
    }

    /**
     * Sets the value of the iAmThisUserIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIAmThisUserIndicator(Boolean value) {
        this.iAmThisUserIndicator = value;
    }

    /**
     * Gets the value of the personDeathDate property.
     * 
     * @return
     *     possible object is
     *     {@link DateType }
     *     
     */
    public DateType getPersonDeathDate() {
        return personDeathDate;
    }

    /**
     * Sets the value of the personDeathDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateType }
     *     
     */
    public void setPersonDeathDate(DateType value) {
        this.personDeathDate = value;
    }

    /**
     * Gets the value of the ssnIdentification property.
     * 
     * @return
     *     possible object is
     *     {@link IdentificationType }
     *     
     */
    public IdentificationType getSSNIdentification() {
        return ssnIdentification;
    }

    /**
     * Sets the value of the ssnIdentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificationType }
     *     
     */
    public void setSSNIdentification(IdentificationType value) {
        this.ssnIdentification = value;
    }

    /**
     * Gets the value of the agency property.
     * 
     * @return
     *     possible object is
     *     {@link AgencyType }
     *     
     */
    public AgencyType getAgency() {
        return agency;
    }

    /**
     * Sets the value of the agency property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgencyType }
     *     
     */
    public void setAgency(AgencyType value) {
        this.agency = value;
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

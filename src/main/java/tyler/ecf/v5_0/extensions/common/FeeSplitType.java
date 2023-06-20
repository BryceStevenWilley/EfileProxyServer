
package tyler.ecf.v5_0.extensions.common;

import gov.niem.release.niem.niem_core._4.TextType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * <p>Java class for FeeSplitType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FeeSplitType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}PaymentInstructionID"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}CourtSplitAmount"/&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:common}NonCourtSplitAmount"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FeeSplitType", propOrder = {
    "paymentInstructionID",
    "courtSplitAmount",
    "nonCourtSplitAmount"
})
public class FeeSplitType {

    @XmlElement(name = "PaymentInstructionID", required = true)
    protected TextType paymentInstructionID;
    @XmlElement(name = "CourtSplitAmount", required = true)
    protected SplitAmountType courtSplitAmount;
    @XmlElement(name = "NonCourtSplitAmount", required = true)
    protected SplitAmountType nonCourtSplitAmount;

    /**
     * Gets the value of the paymentInstructionID property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getPaymentInstructionID() {
        return paymentInstructionID;
    }

    /**
     * Sets the value of the paymentInstructionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setPaymentInstructionID(TextType value) {
        this.paymentInstructionID = value;
    }

    /**
     * Gets the value of the courtSplitAmount property.
     * 
     * @return
     *     possible object is
     *     {@link SplitAmountType }
     *     
     */
    public SplitAmountType getCourtSplitAmount() {
        return courtSplitAmount;
    }

    /**
     * Sets the value of the courtSplitAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link SplitAmountType }
     *     
     */
    public void setCourtSplitAmount(SplitAmountType value) {
        this.courtSplitAmount = value;
    }

    /**
     * Gets the value of the nonCourtSplitAmount property.
     * 
     * @return
     *     possible object is
     *     {@link SplitAmountType }
     *     
     */
    public SplitAmountType getNonCourtSplitAmount() {
        return nonCourtSplitAmount;
    }

    /**
     * Sets the value of the nonCourtSplitAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link SplitAmountType }
     *     
     */
    public void setNonCourtSplitAmount(SplitAmountType value) {
        this.nonCourtSplitAmount = value;
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
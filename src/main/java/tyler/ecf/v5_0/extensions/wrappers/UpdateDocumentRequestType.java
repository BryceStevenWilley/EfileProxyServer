
package tyler.ecf.v5_0.extensions.wrappers;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import tyler.ecf.v5_0.extensions.updatedocument.UpdateDocumentMessageType;


/**
 * <p>Java class for UpdateDocumentRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateDocumentRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:tyler:ecf:v5.0:extensions:updatedocument}UpdateDocumentMessage" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateDocumentRequestType", propOrder = {
    "updateDocumentMessage"
})
public class UpdateDocumentRequestType {

    @XmlElement(name = "UpdateDocumentMessage", namespace = "urn:tyler:ecf:v5.0:extensions:updatedocument", required = true)
    protected List<UpdateDocumentMessageType> updateDocumentMessage;

    /**
     * Gets the value of the updateDocumentMessage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the updateDocumentMessage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdateDocumentMessage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateDocumentMessageType }
     * 
     * 
     */
    public List<UpdateDocumentMessageType> getUpdateDocumentMessage() {
        if (updateDocumentMessage == null) {
            updateDocumentMessage = new ArrayList<UpdateDocumentMessageType>();
        }
        return this.updateDocumentMessage;
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

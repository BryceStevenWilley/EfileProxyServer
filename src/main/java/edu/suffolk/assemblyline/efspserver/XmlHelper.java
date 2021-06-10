package edu.suffolk.assemblyline.efspserver;

import gov.niem.niem.domains.jxdm._4.CourtType;
import gov.niem.niem.niem_core._2.DateType;
import gov.niem.niem.niem_core._2.TextType;
import java.time.LocalDate;
import java.util.Base64;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/**
 * Helper class that easily converts Java native types to Cumbersome XML Schema types,
 * particularly those from Oasis's ECF standard, and the National Information 
 * Exchange Model (NIEM).
 *
 * <p>Some information about NIEM:
 * Many of the types here all include the "SimpleObjectAttributeGroup", which,
 * "provides a collection of attributes which are appropriate for
 *    definition of object types", including an ID attribute, a Metadata attribute,
 * and a Link Metadata attribute that directly reference objects via IDs instead of
 * including the object as an element. TBH, I'm not sure what the design decisions
 * that dictate the use of them are, but they exist.</p>
 *
 * @author Bryce Willey
 *
 */
public class XmlHelper {

  static final gov.niem.niem.proxy.xsd._2.ObjectFactory niemProxyObjFac;
  static final gov.niem.niem.niem_core._2.ObjectFactory niemCoreObjFac;
  static final gov.niem.niem.domains.jxdm._4.ObjectFactory jxObjFac;

  static {
    niemProxyObjFac = new gov.niem.niem.proxy.xsd._2.ObjectFactory();
    niemCoreObjFac = new gov.niem.niem.niem_core._2.ObjectFactory();
    jxObjFac = new gov.niem.niem.domains.jxdm._4.ObjectFactory();
  }
  
  /** Creates a date from a java date. Doesn't have time associated with it. */
  public static DateType convertDate(LocalDate date) {
    DateType dt = niemCoreObjFac.createDateType();
    gov.niem.niem.proxy.xsd._2.Date d = niemProxyObjFac.createDate();
    DatatypeFactory datatypeFac;
    try {
      datatypeFac = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    // TODO(brycew): THIS TIMEZONE IS WRONG: how should LocalDate +
    // GregorianCalendar operate?
    d.setValue(datatypeFac.newXMLGregorianCalendarDate(date.getYear(), 
        date.getMonth().getValue(), date.getDayOfMonth(), 0));

    dt.setDateRepresentation(niemCoreObjFac.createDate(d));
    return dt;
  }
  
  /** Niem's Boolean type: either true or false. */
  public static gov.niem.niem.proxy.xsd._2.Boolean convertBool(boolean bool) {
    gov.niem.niem.proxy.xsd._2.Boolean val = new gov.niem.niem.proxy.xsd._2.Boolean();
    val.setValue(bool);
    return val;
  }
  
  
  /** Converts a Java string to NIEM "Text", a wrapper around the NIEM String.
   *
   * <p>"A data type for a character string"</p>
   */
  public static TextType convertText(String str) {
    TextType tt = niemCoreObjFac.createTextType();
    tt.setValue(str);
    return tt;
  }
  
  /** Converts a Java string to NIEM's XML String. "A datatype for character strings in XML."
   * Practically, just a xsd:string with the extra SimpleObjectAttributeGroup attributes.
   */
  public static gov.niem.niem.proxy.xsd._2.String convertString(String str) {
    gov.niem.niem.proxy.xsd._2.String outStr = niemProxyObjFac.createString();
    outStr.setValue(str);
    return outStr;
  }
  
  public static gov.niem.niem.niem_core._2.IdentificationType convertId(String idStr) {
    gov.niem.niem.niem_core._2.IdentificationType id = niemCoreObjFac.createIdentificationType();
    id.setIdentificationID(convertString(idStr));
    return id;
  }

  public static gov.niem.niem.niem_core._2.IdentificationType convertId(
      String idStr, String category) {
    gov.niem.niem.niem_core._2.IdentificationType id = niemCoreObjFac.createIdentificationType();
    id.setIdentificationID(convertString(idStr));
    id.setIdentificationCategory(
        niemCoreObjFac.createIdentificationCategoryText(convertText(category)));
    return id;
  }
  
  /** Converts a Java string to a URI. */
  public static gov.niem.niem.proxy.xsd._2.AnyURI convertUri(String uri) {
    gov.niem.niem.proxy.xsd._2.AnyURI anyUri = niemProxyObjFac.createAnyURI();
    anyUri.setValue(uri);
    return anyUri;
  }
  
  public static gov.niem.niem.proxy.xsd._2.Base64Binary convertBase64(final byte[] rawContent) {
    gov.niem.niem.proxy.xsd._2.Base64Binary binaryString = niemProxyObjFac.createBase64Binary();
    binaryString.setValue(Base64.getEncoder().encode(rawContent));
    return binaryString;
  }
  
  public static CourtType convertCourtType(String courtId) {
    CourtType court = jxObjFac.createCourtType();
    JAXBElement<gov.niem.niem.niem_core._2.IdentificationType> idType = 
        niemCoreObjFac.createOrganizationIdentification(XmlHelper.convertId(courtId));
    court.setOrganizationIdentification(idType);
    return court;
  }
}
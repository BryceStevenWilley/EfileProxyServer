package edu.suffolk.litlab.efspserver.ecf;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hubspot.algebra.Result;

import edu.suffolk.litlab.efspserver.Address;
import edu.suffolk.litlab.efspserver.ContactInformation;
import edu.suffolk.litlab.efspserver.Person;
import edu.suffolk.litlab.efspserver.XmlHelper;
import edu.suffolk.litlab.efspserver.codes.CodeDatabase;
import edu.suffolk.litlab.efspserver.services.FailFastCollector;
import edu.suffolk.litlab.efspserver.services.FilingError;
import edu.suffolk.litlab.efspserver.services.InfoCollector;
import edu.suffolk.litlab.efspserver.services.InterviewVariable;
import gov.niem.niem.fbi._2.SEXCodeSimpleType;
import gov.niem.niem.fbi._2.SEXCodeType;
import gov.niem.niem.fips_10_4._2.CountryCodeSimpleType;
import gov.niem.niem.fips_10_4._2.CountryCodeType;
import gov.niem.niem.iso_639_3._2.LanguageCodeType;
import gov.niem.niem.niem_core._2.AddressType;
import gov.niem.niem.niem_core._2.ContactInformationType;
import gov.niem.niem.niem_core._2.FullTelephoneNumberType;
import gov.niem.niem.niem_core._2.PersonLanguageType;
import gov.niem.niem.niem_core._2.ProperNameTextType;
import gov.niem.niem.niem_core._2.StreetType;
import gov.niem.niem.niem_core._2.StructuredAddressType;
import gov.niem.niem.niem_core._2.TelephoneNumberType;
import gov.niem.niem.structures._2.AugmentationType;
import gov.niem.niem.usps_states._2.USStateCodeSimpleType;
import gov.niem.niem.usps_states._2.USStateCodeType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.CaseParticipantType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.OrganizationAugmentationType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.OrganizationType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.PersonAugmentationType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.PersonType;

public class EcfCourtSpecificSerializer {
  private static Logger log = LoggerFactory.getLogger(EcfCourtSpecificSerializer.class); 
  
  private CodeDatabase cd;
  private String courtId;
  gov.niem.niem.niem_core._2.ObjectFactory niemObjFac =
      new gov.niem.niem.niem_core._2.ObjectFactory();
  gov.niem.niem.niem_core._2.ObjectFactory coreObjFac = 
      new gov.niem.niem.niem_core._2.ObjectFactory(); 
  oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ObjectFactory ecfOf = 
      new oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ObjectFactory();

  public EcfCourtSpecificSerializer(CodeDatabase cd, String courtId) {
    this.cd = cd;
    this.courtId = courtId;
  }
  
  /** Needs to have participant role set. */
  public Result<CaseParticipantType, FilingError> serializeEcfCaseParticipant(Person per, InfoCollector collector) {
    AugmentationType aug;
    if (per.isOrg()) {
      aug = ecfOf.createOrganizationAugmentationType();
    } else {
      aug = ecfOf.createPersonAugmentationType();
    }
    Result<ContactInformationType, FilingError> cit = serializeEcfContactInformation(per.getContactInfo(), collector);
    if (cit.isErr()) {
      return cit.mapOk((o) -> null);
    }
    if (per.isOrg()) {
      ((OrganizationAugmentationType) aug).getContactInformation().add(cit.unwrapOrElseThrow());
      OrganizationType ot = ecfOf.createOrganizationType();
      ot.setOrganizationName(XmlHelper.convertText(per.getName().getFullName()));
      ot.getRest().add(ecfOf.createOrganizationAugmentation((OrganizationAugmentationType) aug));
      CaseParticipantType cpt = ecfOf.createCaseParticipantType();
      cpt.setEntityRepresentation(ecfOf.createEntityOrganization(ot));
      return Result.ok(cpt);
    }

    // Else, it's a person: add other optional person stuff
    ((PersonAugmentationType) aug).getContactInformation().add(cit.unwrapOrElseThrow());

    PersonType pt = ecfOf.createPersonType();
    pt.setId(per.getIdString()); 
    pt.setPersonName(per.getName().getNameType());
    pt.setPersonAugmentation((PersonAugmentationType) aug);

    per.getGender().ifPresent((gen) -> {
      SEXCodeType sct = new SEXCodeType();
      if (gen.equalsIgnoreCase("male") || gen.equalsIgnoreCase("m")) {
        sct.setValue(SEXCodeSimpleType.M);
      } else if (gen.equalsIgnoreCase("female") || gen.equals("f")) {
        sct.setValue(SEXCodeSimpleType.F);
      } else {
        sct.setValue(SEXCodeSimpleType.U);
      }
      pt.setPersonSex(niemObjFac.createPersonSexCode(sct));
    });

    per.getLanguage().ifPresent((lang) -> {
      List<String> langs = cd.getLanguages(this.courtId);
      if (!langs.isEmpty() && !langs.contains(lang)) {
        log.info("Can't have language: " + lang);
        collector.addWrong(collector.requestVar("language", "The primary language of this person", "choice", langs));
        return;
      }
      final gov.niem.niem.iso_639_3._2.ObjectFactory lctOf =
          new gov.niem.niem.iso_639_3._2.ObjectFactory();
      LanguageCodeType lct = lctOf.createLanguageCodeType();
      lct.setValue(lang);
      PersonLanguageType plt = niemObjFac.createPersonLanguageType();
      plt.getLanguage().add(niemObjFac.createLanguageCode(lct));
      pt.setPersonPrimaryLanguage(plt);
    });
    if (collector.finished()) {
      return Result.err(FilingError.wrongValue(collector.getWrong().get(collector.getWrong().size() - 1)));
    }

    per.getBirthdate().ifPresent((bd) -> {
      pt.setPersonBirthDate(XmlHelper.convertDate(bd));
    });

    CaseParticipantType cpt = ecfOf.createCaseParticipantType();
    cpt.setEntityRepresentation(ecfOf.createEntityPerson(pt));
    return Result.ok(cpt);
  }
  
  public Result<ContactInformationType, FilingError> serializeEcfContactInformation(
      ContactInformation contactInfo, InfoCollector collector) {
    ContactInformationType cit = niemObjFac.createContactInformationType();
    if (contactInfo.getAddress().isPresent()) {
      Address addr = contactInfo.getAddress().get();
      Result<JAXBElement<AddressType>, FilingError> contactMeans = serializeNiemContactMeans(addr, collector);
      if (contactMeans.isErr()) {
        return contactMeans.mapOk((o) -> null);
      }
      cit.getContactMeans().add(contactMeans.unwrapOrElseThrow()); 
    }
      
    for (String phoneNumber : contactInfo.getPhoneNumbers()) {
      TelephoneNumberType tnt = niemObjFac.createTelephoneNumberType();
      FullTelephoneNumberType ftnt = niemObjFac.createFullTelephoneNumberType();
      ftnt.setTelephoneNumberFullID(XmlHelper.convertString(phoneNumber));
      tnt.setTelephoneNumberRepresentation(niemObjFac.createFullTelephoneNumber(ftnt));
      cit.getContactMeans().add(niemObjFac.createContactTelephoneNumber(tnt));
    }
    contactInfo.getEmail().ifPresent((em) -> {
      cit.getContactMeans().add(niemObjFac.createContactEmailID(XmlHelper.convertString(em))); 
    });
    return Result.ok(cit);
  }

  public Result<tyler.efm.services.schema.common.AddressType, FilingError> serializeTylerAddress(Address myAddr) {
    tyler.efm.services.schema.common.ObjectFactory efmObjFac = 
        new tyler.efm.services.schema.common.ObjectFactory();
    tyler.efm.services.schema.common.AddressType addr = efmObjFac.createAddressType();
    addr.setAddressLine1(myAddr.getStreet());
    addr.setAddressLine2(myAddr.getApartment()); 
    addr.setCity(myAddr.getCity()); 
    FailFastCollector collector = new FailFastCollector();
    Optional<CountryCodeType> cct = strToCountryCode(myAddr.getCountry(), collector);
    if (cct.isEmpty()) {
      return Result.err(FilingError.serverError("Country Code is wrong"));
    }
    addr.setState(myAddr.getState()); 
    addr.setZipCode(myAddr.getZip()); 
    addr.setCountry(myAddr.getCountry()); 
    return Result.ok(addr);
  }
  
  /** Returns the "ContactMeans" XML object from this address. Can be used in the 
   * ContactInformation element.
   */
  public Result<JAXBElement<AddressType>, FilingError> serializeNiemContactMeans(Address address,
      InfoCollector collector) {
    StreetType st = niemObjFac.createStreetType();
    st.setStreetFullText(XmlHelper.convertText(address.getStreet() + " " + address.getApartment())); 
    StructuredAddressType sat = niemObjFac.createStructuredAddressType();
    sat.getAddressDeliveryPoint().add(niemObjFac.createLocationStreet(st));
    ProperNameTextType pntt = niemObjFac.createProperNameTextType();
    pntt.setValue(address.getCity());
    sat.setLocationCityName(pntt);
    Optional<CountryCodeType> cct = strToCountryCode(address.getCountry(), collector);
    if (cct.isEmpty()) {
      List<String> countries = Arrays.stream(CountryCodeSimpleType.values()) 
          .map((t) -> t.toString())
          .collect(Collectors.toList());
      InterviewVariable var = collector.requestVar("country", "County of the World, in an address", 
          "choices", countries);
      collector.addWrong(var);
      if (collector.finished()) {
        return Result.err(FilingError.wrongValue(var));
      }
    }
    sat.setLocationCountry(niemObjFac.createLocationCountryFIPS104Code(cct.get()));
    if (!fillStateCode(address.getState(), cct.get(), sat)) {
      String countryString = cct.get().getValue().value();
      List<String> stateCodes = cd.getStateCodes(cct.get().getValue().value());
      InterviewVariable var = collector.requestVar("state", "State in a country", "choices", stateCodes);
      collector.addWrong(var);
      if (stateCodes.isEmpty()) {
        FilingError err = FilingError.malformedInterview("There are no allowed states for " + countryString);
        collector.error(err);
        return Result.err(err);
      }
      if (collector.finished()) {
        return Result.err(FilingError.wrongValue(var));
      }
    }
    sat.setLocationPostalCode(XmlHelper.convertString(address.getZip())); 
    gov.niem.niem.niem_core._2.AddressType at = niemObjFac.createAddressType();
    at.setAddressRepresentation(niemObjFac.createStructuredAddress(sat));
    return Result.ok(niemObjFac.createContactMailingAddress(at));
  }

  
  private Optional<CountryCodeType> strToCountryCode(String country, InfoCollector collector) {
    CountryCodeType cct = new CountryCodeType();
    try {
      cct.setValue(CountryCodeSimpleType.fromValue(country)); 
      return Optional.of(cct);
    } catch (IllegalArgumentException ex) {
      log.error("DevOps ERROR:" + ex); 
      return Optional.empty();
    }
  }
  
  /** True if it worked. */
  private boolean fillStateCode(String state, CountryCodeType country, 
      StructuredAddressType sat) {
    String countryString = country.getValue().toString();
    List<String> stateCodes = cd.getStateCodes(countryString);
    
    if (!stateCodes.contains(state)) {
      return false;
    }
    
    if (country.getValue().equals(CountryCodeSimpleType.US)) {
      try {
        USStateCodeSimpleType stateSimple = USStateCodeSimpleType.fromValue(state);

        USStateCodeType stateCode = new USStateCodeType();
        stateCode.setValue(stateSimple);
        sat.setLocationState(coreObjFac.createLocationStateUSPostalServiceCode(stateCode));
        return true;
      } catch (IllegalArgumentException ex) {
        log.error("DevOps ERROR: " + ex);
        return false;
      }
    } 
    
    ProperNameTextType pntt = niemObjFac.createProperNameTextType();
    pntt.setValue(state); 
    sat.setLocationState(coreObjFac.createLocationStateName(pntt));
    return true;
  }
  
}
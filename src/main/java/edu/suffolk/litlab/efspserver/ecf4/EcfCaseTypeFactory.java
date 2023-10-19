package edu.suffolk.litlab.efspserver.ecf4;

import com.fasterxml.jackson.databind.JsonNode;
import edu.suffolk.litlab.efspserver.CaseServiceContact;
import edu.suffolk.litlab.efspserver.ContactInformation;
import edu.suffolk.litlab.efspserver.FilingInformation;
import edu.suffolk.litlab.efspserver.Name;
import edu.suffolk.litlab.efspserver.PartyId;
import edu.suffolk.litlab.efspserver.Person;
import edu.suffolk.litlab.efspserver.services.FilingError;
import edu.suffolk.litlab.efspserver.services.InfoCollector;
import edu.suffolk.litlab.efspserver.services.InterviewVariable;
import edu.suffolk.litlab.efspserver.tyler.QueryType;
import edu.suffolk.litlab.efspserver.tyler.codes.CaseCategory;
import edu.suffolk.litlab.efspserver.tyler.codes.CodeDatabase;
import edu.suffolk.litlab.efspserver.tyler.codes.ComboCaseCodes;
import edu.suffolk.litlab.efspserver.tyler.codes.CourtLocationInfo;
import edu.suffolk.litlab.efspserver.tyler.codes.DataFieldRow;
import edu.suffolk.litlab.efspserver.tyler.codes.FilerType;
import edu.suffolk.litlab.efspserver.tyler.codes.NameAndCode;
import edu.suffolk.litlab.efspserver.tyler.codes.PartyType;
import gov.niem.niem.iso_4217._2.CurrencyCodeSimpleType;
import gov.niem.niem.niem_core._2.AmountType;
import gov.niem.niem.niem_core._2.CaseType;
import gov.niem.niem.niem_core._2.IdentificationType;
import gov.niem.niem.niem_core._2.PersonNameType;
import gov.niem.niem.niem_core._2.TextType;
import gov.niem.niem.structures._2.ReferenceType;
import jakarta.xml.bind.JAXBElement;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.appellatecase_4.AppellateCaseType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.casequerymessage_4.CaseQueryCriteriaType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.civilcase_4.CivilCaseType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.CaseOfficialType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.CaseParticipantType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.OrganizationType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.PersonType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.criminalcase_4.CriminalCaseType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.domesticcase_4.DomesticCaseType;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tyler.ecf.extensions.common.FilingAssociationType;
import tyler.ecf.extensions.common.OrganizationIdentificationType;
import tyler.ecf.extensions.common.ProcedureRemedyType;
import tyler.ecf.extensions.common.ServicePartyDataType;

public class EcfCaseTypeFactory {
  private static Logger log = LoggerFactory.getLogger(EcfCaseTypeFactory.class);

  private final CodeDatabase cd;
  private final tyler.ecf.extensions.common.ObjectFactory tylerObjFac;
  private final oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ObjectFactory
      ecfCommonObjFac;
  private final gov.niem.niem.niem_core._2.ObjectFactory of;
  private final gov.niem.niem.structures._2.ObjectFactory structObjFac;
  private final String jurisdiction;

  public EcfCaseTypeFactory(CodeDatabase cd, String jurisdiction) {
    this.cd = cd;
    this.tylerObjFac = new tyler.ecf.extensions.common.ObjectFactory();
    this.ecfCommonObjFac =
        new oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ObjectFactory();
    this.of = new gov.niem.niem.niem_core._2.ObjectFactory();
    this.structObjFac = new gov.niem.niem.structures._2.ObjectFactory();
    this.jurisdiction = jurisdiction;
  }

  public static Optional<tyler.ecf.extensions.common.CaseAugmentationType> getCaseAugmentation(
      gov.niem.niem.niem_core._2.CaseType filedCase) {
    List<JAXBElement<?>> restList = List.of();
    if (filedCase instanceof CivilCaseType civilCase) {
      restList = civilCase.getRest();
    } else if (filedCase instanceof DomesticCaseType domesCase) {
      restList = domesCase.getRest();
    } else if (filedCase instanceof CriminalCaseType criminalCase) {
      restList = criminalCase.getRest();
    } else if (filedCase instanceof AppellateCaseType appellate) {
      restList = appellate.getRest();
    }
    for (JAXBElement<?> elem : restList) {
      if (elem.getValue() instanceof tyler.ecf.extensions.common.CaseAugmentationType aug) {
        return Optional.of(aug);
      }
    }
    return Optional.empty();
  }

  public static Optional<gov.niem.niem.domains.jxdm._4.CaseAugmentationType> getJCaseAugmentation(
      gov.niem.niem.niem_core._2.CaseType filedCase) {
    List<JAXBElement<?>> restList = List.of();
    if (filedCase instanceof CivilCaseType civilCase) {
      restList = civilCase.getRest();
    } else if (filedCase instanceof DomesticCaseType domesCase) {
      restList = domesCase.getRest();
    } else if (filedCase instanceof CriminalCaseType criminalCase) {
      restList = criminalCase.getRest();
    } else if (filedCase instanceof AppellateCaseType appellate) {
      restList = appellate.getRest();
    }
    for (JAXBElement<?> elem : restList) {
      if (elem.getValue() instanceof gov.niem.niem.domains.jxdm._4.CaseAugmentationType aug) {
        return Optional.of(aug);
      }
    }
    return Optional.empty();
  }

  /** TODO(brycew): finish this function, lots of possible rabbit holes here. */
  public static Optional<Map<PartyId, Person>> getCaseParticipants(
      gov.niem.niem.niem_core._2.CaseType filedCase) {
    Map<PartyId, Person> existingParticipants = new HashMap<>();
    Optional<tyler.ecf.extensions.common.CaseAugmentationType> maybeAug =
        getCaseAugmentation(filedCase);
    if (maybeAug.isEmpty()) {
      return Optional.empty();
    }

    for (var jaxPart : maybeAug.get().getCaseParticipant()) {
      String role = jaxPart.getValue().getCaseParticipantRoleCode().getValue();
      if (role.equalsIgnoreCase("ATTY")) {
        // Skip attorneys, who aren't actually participants with codes
        continue;
      }
      var entRep = jaxPart.getValue().getEntityRepresentation();
      boolean isOrg = false;
      String efmId = "";
      Name name = new Name("", "", "");
      if (entRep.getValue() instanceof PersonType perType) {
        isOrg = false;
        if (perType.getPersonName() != null) {
          PersonNameType xmlName = perType.getPersonName();
          String firstName =
              (xmlName.getPersonGivenName() != null) ? xmlName.getPersonGivenName().getValue() : "";
          String middleName =
              (xmlName.getPersonMiddleName() != null)
                  ? xmlName.getPersonMiddleName().getValue()
                  : "";
          String lastName =
              (xmlName.getPersonSurName() != null) ? xmlName.getPersonSurName().getValue() : "";
          name = new Name(firstName, middleName, lastName);
        }
        for (IdentificationType idType : perType.getPersonOtherIdentification()) {
          efmId = idType.getIdentificationID().getValue();
        }
      } else if (entRep.getValue() instanceof OrganizationType orgType) {
        isOrg = true;
        IdentificationType idType = orgType.getOrganizationIdentification().getValue();
        if (idType instanceof OrganizationIdentificationType orgIdObj) {
          for (IdentificationType orgId : orgIdObj.getIdentification()) {
            if (((TextType) orgId.getIdentificationCategory().getValue())
                    .getValue()
                    .equals("CASEPARTYID")
                && orgId.getIdentificationID() != null) {
              efmId = orgId.getIdentificationID().getValue();
              break;
            }
          }
          if (efmId.isBlank() && orgIdObj.getIdentificationID() != null) {
            efmId = orgIdObj.getIdentificationID().getValue();
          }
        } else {
          if (idType != null && idType.getIdentificationID() != null) {
            efmId = idType.getIdentificationID().getValue();
          }
        }
        if (orgType.getOrganizationName() != null) {
          name = new Name(orgType.getOrganizationName().getValue());
        }
      }
      if (efmId.isBlank()) {
        log.warn("Need Id for participants on existing case");
        return Optional.empty();
      }
      Person per =
          Person.FromEfm(
              name,
              new ContactInformation(""),
              Optional.empty(),
              Optional.empty(),
              Optional.empty(),
              isOrg,
              role,
              UUID.fromString(efmId));
      existingParticipants.put(PartyId.Already(efmId), per);
    }
    return Optional.of(existingParticipants);
  }

  public static CaseQueryCriteriaType getCriteria() {
    CaseQueryCriteriaType crit = new CaseQueryCriteriaType();
    // TODO(brycew-later): should this be configurable?
    crit.setIncludeParticipantsIndicator(Ecf4Helper.convertBool(true));
    crit.setIncludeDocketEntryIndicator(Ecf4Helper.convertBool(false));
    crit.setIncludeCalendarEventIndicator(Ecf4Helper.convertBool(false));
    crit.setDocketEntryTypeCodeFilterText(Ecf4Helper.convertText("false"));
    crit.setCalendarEventTypeCodeFilterText(Ecf4Helper.convertText("false"));
    return crit;
  }

  /**
   * Dumb XML requires you to have the Object itself before you can reference it, you can't just use
   * the ID. But we don't have the filing document objects when making the case.
   */
  public List<FilingAssociationType> lateStageFilingAssociationAdd(
      EcfCourtSpecificSerializer serializer,
      Map<String, Object> filingIdToObj,
      Map<String, List<PartyId>> filingAssociations,
      Map<String, Object> partyIdToRefObj) {

    DataFieldRow filingcaseparties = serializer.allDataFields.getFieldRow("FilingEventCaseParties");
    var toReturn = new ArrayList<FilingAssociationType>();
    var structOf = new gov.niem.niem.structures._2.ObjectFactory();
    if (filingcaseparties.isrequired) {
      for (Map.Entry<String, Object> filingObj : filingIdToObj.entrySet()) {
        gov.niem.niem.structures._2.ReferenceType filingRt = structOf.createReferenceType();
        filingRt.setRef(filingObj.getValue());
        var filingAssociation = filingAssociations.get(filingObj.getKey());
        for (PartyId partyId : filingAssociation) {
          if (partyIdToRefObj.containsKey(partyId.getIdString())) {
            ReferenceType partyRef = structObjFac.createReferenceType();
            partyRef.setRef(partyIdToRefObj.get(partyId.getIdString()));
            FilingAssociationType association = tylerObjFac.createFilingAssociationType();
            association.setPartyReference(partyRef);
            association.setFilingReference(filingRt);
            toReturn.add(association);
          } else {
            log.warn("When adding filing associations, Party " + partyId + " isn't an object?");
            log.warn("Parties that do exist: " + List.of(partyIdToRefObj.keySet().toArray()));
          }
        }
      }
    }
    return toReturn;
  }

  /**
   * Makes a case from specific info + unorganized JSON dumps.
   *
   * @param courtLocation the court's name a unique string. ex: 'adams'
   * @param comboCodes contains the case category, the case type code, and the case subtype code (if
   *     present)
   * @param info The full information about the filing
   * @param isInitialFiling
   * @param isFirstIndexedFiling
   * @param queryType
   * @param miscInfo
   * @param serializer
   * @param collector
   * @param serviceContactToXmlObjs
   * @return two objects: the actual case object, and a map from party ID to the object inside the
   *     case, used to associate other elements to the party element later on.
   * @throws FilingError
   * @throws SQLException
   */
  public Pair<JAXBElement<? extends gov.niem.niem.niem_core._2.CaseType>, Map<String, Object>>
      makeCaseTypeFromTylerCategory(
          CourtLocationInfo courtLocation,
          ComboCaseCodes comboCodes,
          FilingInformation info,
          boolean isInitialFiling,
          boolean isFirstIndexedFiling,
          // HACK(brycew): hacky: needed because "fees" querys and "service" put the payment stuff
          // in the tyler Aug
          QueryType queryType,
          JsonNode miscInfo, // TODO(brycew-later): if we get XML Answer files, this isn't generic
          EcfCourtSpecificSerializer serializer,
          InfoCollector collector,
          Map<String, Object> serviceContactToXmlObjs)
          throws SQLException, FilingError {
    JAXBElement<gov.niem.niem.domains.jxdm._4.CaseAugmentationType> caseAug =
        makeNiemCaseAug(courtLocation.code, info.getPreviousCaseId());
    var pair =
        makeTylerCaseAug(
            courtLocation,
            comboCodes,
            info,
            isInitialFiling,
            isFirstIndexedFiling,
            queryType,
            miscInfo,
            serializer,
            collector,
            serviceContactToXmlObjs);
    JAXBElement<tyler.ecf.extensions.common.CaseAugmentationType> tylerAug = pair.getLeft();
    JAXBElement<? extends gov.niem.niem.niem_core._2.CaseType> myCase;
    // TODO(brycew): This in NOT correct. Wait on Tyler case 10482535 for how to fix.
    String ecfcasetype = comboCodes.cat.getEcfCaseType().orElse("CivilCase");
    if (ecfcasetype.equals("CivilCase")) {
      Optional<BigDecimal> amountInControversy = Optional.empty();
      boolean anyAmountInControversy =
          comboCodes.filings.stream()
              .anyMatch(f -> f.amountincontroversy.equalsIgnoreCase("Required"));
      if (anyAmountInControversy) {
        JsonNode jsonAmt = info.getMiscInfo().get("amount_in_controversy");
        if (jsonAmt != null && jsonAmt.isNumber()) {
          amountInControversy = Optional.of(jsonAmt.decimalValue());
        } else {
          collector.addRequired(
              collector.requestVar("amount_in_controversy", "ad danum amount", "currency"));
        }
      }
      myCase =
          makeCivilCaseType(
              caseAug,
              tylerAug,
              info.getCaseDocketNumber(),
              info.getPreviousCaseId(),
              amountInControversy);
    } else if (ecfcasetype.equals("DomesticCase")) {
      myCase =
          makeDomesticCaseType(
              caseAug, tylerAug, info.getCaseDocketNumber(), info.getPreviousCaseId(), miscInfo);
    } else if (ecfcasetype.equals("AppellateCase")) {
      myCase = makeAppellateCaseType(caseAug, tylerAug, info, miscInfo, collector);
    } else if (ecfcasetype.equals("BankruptcyCase")
        || ecfcasetype.equals("CitationCase")
        || ecfcasetype.equals("JuvenileCase")
        || ecfcasetype.equals("CriminalCase")) {
      // TODO(brycew): handle these
      InterviewVariable var =
          collector.requestVar(
              "efile_case_category",
              "The "
                  + comboCodes.cat.getName().orElse(comboCodes.cat.getCode())
                  + " Case category requires an ECF case type that we know about but don't yet"
                  + " support ("
                  + ecfcasetype
                  + ")",
              "text");
      collector.addWrong(var);
      FilingError err = FilingError.wrongValue(var);
      throw err;
    } else {
      InterviewVariable var =
          collector.requestVar(
              "efile_case_category",
              "The "
                  + comboCodes.cat.getName().orElse(comboCodes.cat.getCode())
                  + " Case category requires an ECF case type that we don't know about or support ("
                  + ecfcasetype
                  + ")",
              "text");
      collector.addWrong(var);
      FilingError err = FilingError.wrongValue(var);
      throw err;
    }
    myCase.getValue().setCaseCategoryText(Ecf4Helper.convertText(comboCodes.cat.getCode()));
    return Pair.of(myCase, pair.getRight());
  }

  private static JAXBElement<gov.niem.niem.domains.jxdm._4.CaseAugmentationType> makeNiemCaseAug(
      String courtLocationId, Optional<String> trackingId) {
    var jof = new gov.niem.niem.domains.jxdm._4.ObjectFactory();
    var caseAug = jof.createCaseAugmentationType();
    caseAug.setCaseCourt(Ecf4Helper.convertCourtType(courtLocationId));
    trackingId.ifPresent(
        tracking -> {
          var coreObjFac = new gov.niem.niem.niem_core._2.ObjectFactory();
          var ct = coreObjFac.createCaseType();
          ct.setCaseTrackingID(Ecf4Helper.convertString(tracking));
          caseAug.getCaseLineageCase().add(ct);
        });
    return jof.createCaseAugmentation(caseAug);
  }

  private Pair<JAXBElement<tyler.ecf.extensions.common.CaseAugmentationType>, Map<String, Object>>
      makeTylerCaseAug(
          CourtLocationInfo courtLocation,
          ComboCaseCodes comboCodes,
          FilingInformation info,
          boolean isInitialFiling,
          boolean isFirstIndexedFiling,
          QueryType queryType,
          JsonNode miscInfo,
          EcfCourtSpecificSerializer serializer,
          InfoCollector collector,
          Map<String, Object> serviceContactXmlObjs)
          throws SQLException, FilingError {
    var ecfAug = tylerObjFac.createCaseAugmentationType();

    if (comboCodes.type.getCode().isEmpty()) {
      log.warn("Type's code is empty?: " + comboCodes);
    } else {
      log.info("Setting case type text to " + comboCodes.type.toString());
      ecfAug.setCaseTypeText(Ecf4Helper.convertText(comboCodes.type.getCode()));
    }

    DataFieldRow subTypeConfig = serializer.allDataFields.getFieldRow("CaseInformationCaseSubType");
    if (subTypeConfig.isvisible) {
      List<NameAndCode> subTypes =
          cd.getCaseSubtypesFor(courtLocation.code, comboCodes.type.getCode());
      Optional<NameAndCode> maybeSubtype =
          subTypes.stream()
              .filter(type -> type.getCode().equals(info.getCaseSubtypeCode()))
              .findFirst();

      if (maybeSubtype.isPresent()) {
        ecfAug.setCaseSubTypeText(Ecf4Helper.convertText(maybeSubtype.get().getCode()));
      } else if (subTypeConfig.isrequired) {
        InterviewVariable subTypeVar =
            collector.requestVar(
                "efile_case_subtype",
                "Sub type of the case",
                "choices",
                subTypes.stream().map(nac -> nac.getName()).collect(Collectors.toList()));
        collector.addWrong(subTypeVar);
      }
    }

    List<PartyType> partyTypes = cd.getPartyTypeFor(courtLocation.code, comboCodes.type.getCode());
    Set<String> requiredTypes =
        partyTypes.stream().filter(t -> t.isrequired).map(t -> t.code).collect(Collectors.toSet());
    Set<String> presentPartyTypes = new HashSet<>();
    Map<String, Object> partyIdToRefObj = new HashMap<>();
    int i = 0;
    for (Person plaintiff : info.getNewPlaintiffs()) {
      collector.pushAttributeStack("plaintiffs[" + i + ']');
      CaseParticipantType cp =
          serializer.serializeEcfCaseParticipant(plaintiff, collector, partyTypes);
      collector.popAttributeStack();
      ecfAug.getCaseParticipant().add(ecfCommonObjFac.createCaseParticipant(cp));
      partyIdToRefObj.put(plaintiff.getIdString(), cp.getEntityRepresentation().getValue());
      presentPartyTypes.add(plaintiff.getRole());
    }

    i = 0;
    for (Person defendant : info.getNewDefendants()) {
      collector.pushAttributeStack("defendants[" + i + ']');
      CaseParticipantType cp =
          serializer.serializeEcfCaseParticipant(defendant, collector, partyTypes);
      collector.popAttributeStack();
      ecfAug.getCaseParticipant().add(ecfCommonObjFac.createCaseParticipant(cp));
      partyIdToRefObj.put(defendant.getIdString(), cp.getEntityRepresentation().getValue());
      presentPartyTypes.add(defendant.getRole());
    }

    // We need to make sure the initial filing handles all required parties: subsequents we can
    // assume the required parties are there
    if (isFirstIndexedFiling) {
      requiredTypes.removeAll(presentPartyTypes);
      if (!requiredTypes.isEmpty()) {
        FilingError err =
            FilingError.serverError(
                "DEV ERROR: All required parties not covered by existing party types. ("
                    + presentPartyTypes
                    + ". Missing "
                    + requiredTypes);
        collector.error(err);
      }
    }

    int attorneyCount = 1;
    Map<String, PersonType> attorneyIdToXmlId = new HashMap<>();
    for (String attorneyId : info.getAttorneyIds()) {
      IdentificationType id = of.createIdentificationType();
      id.setIdentificationCategory(
          of.createIdentificationCategoryText(Ecf4Helper.convertText("ATTORNEYID")));
      id.setIdentificationID(Ecf4Helper.convertString(attorneyId));
      PersonType pt = ecfCommonObjFac.createPersonType();
      pt.getPersonOtherIdentification().add(id);
      String xmlId = "attorneyCount-" + Integer.toString(attorneyCount);
      pt.setId(xmlId);
      CaseParticipantType cp = ecfCommonObjFac.createCaseParticipantType();
      cp.setEntityRepresentation(ecfCommonObjFac.createEntityPerson(pt));
      // All attorneys have the role code of ATTY
      cp.setCaseParticipantRoleCode(Ecf4Helper.convertText("ATTY"));
      ecfAug.getCaseParticipant().add(ecfCommonObjFac.createCaseParticipant(cp));
      attorneyIdToXmlId.put(attorneyId, pt);
      attorneyCount += 1;
    }

    // Add all of the existing parties that are referenced somehow (either adding new attorney,
    // service contact, or filing party) to the XML
    var stream =
        Stream.concat(
                Stream.concat(
                    info.getServiceContacts().stream()
                        .filter(c -> c.partyAssociated.isPresent())
                        .map(c -> c.partyAssociated.get()),
                    info.getPartyAttorneyMap().entrySet().stream().map(pa -> pa.getKey())),
                info.getFilings().stream().flatMap(filing -> filing.getFilingPartyIds().stream()))
            .distinct();
    Iterable<PartyId> iterator = stream::iterator;
    for (PartyId partyId : iterator) {
      log.info("Referenced PartyId (" + partyId + ")");
      if (partyId.isAlreadyInCase()) {
        if (partyId.getIdentificationString().contains(" ")) {
          FilingError err =
              FilingError.serverError(
                  "Party ID " + partyId.getIdentificationString() + " should be a GUID but isn't");
          collector.error(err);
        }
        CaseParticipantType cpt = ecfCommonObjFac.createCaseParticipantType();
        IdentificationType id = of.createIdentificationType();
        id.setIdentificationCategory(
            of.createIdentificationCategoryText(Ecf4Helper.convertText("CASEPARTYID")));
        id.setIdentificationID(Ecf4Helper.convertString(partyId.getIdentificationString()));
        var pInfo = comboCodes.partyInfo.get(partyId.getIdentificationString());
        boolean isOrg = false;
        if (pInfo == null) {
          log.warn("PartyId (" + partyId + ") has null info in combocodes. Why? Assuming person");
          log.warn(
              "Service contacts: "
                  + info.getServiceContacts().stream()
                      .filter(c -> c.partyAssociated.isPresent())
                      .map(c -> c.partyAssociated.get()));
          log.warn(
              "Party attorneys: "
                  + info.getPartyAttorneyMap().entrySet().stream().map(pa -> pa.getKey()));
          log.warn(
              "Filing party IDs:"
                  + info.getFilings().stream()
                      .flatMap(filing -> filing.getFilingPartyIds().stream()));
        } else {
          isOrg = pInfo.getRight();
        }
        if (isOrg) {
          OrganizationIdentificationType orgId = tylerObjFac.createOrganizationIdentificationType();
          orgId.getIdentification().add(id);
          OrganizationType ot = ecfCommonObjFac.createOrganizationType();
          ot.setOrganizationIdentification(
              tylerObjFac.createOrganizationOtherIdentification(orgId));
          ot.setId(partyId.getIdString());
          cpt.setEntityRepresentation(ecfCommonObjFac.createEntityOrganization(ot));
          partyIdToRefObj.put(partyId.getIdString(), ot);
        } else {
          PersonType pt = ecfCommonObjFac.createPersonType();
          pt.setId(partyId.getIdString());
          pt.getPersonOtherIdentification().add(id);
          cpt.setEntityRepresentation(ecfCommonObjFac.createEntityPerson(pt));
          partyIdToRefObj.put(partyId.getIdString(), pt);
        }
        var partyInfo = comboCodes.partyInfo.get(partyId.getIdentificationString());
        if (partyInfo != null && partyInfo.getLeft() != null) {
          cpt.setCaseParticipantRoleCode(Ecf4Helper.convertText(partyInfo.getLeft().code));
        } else {
          cpt.setCaseParticipantRoleCode(
              Ecf4Helper.convertText("")); // Will likely cause an error, but what can we do now?
        }
        ecfAug.getCaseParticipant().add(ecfCommonObjFac.createCaseParticipant(cpt));
      }
    }

    DataFieldRow attRow = serializer.allDataFields.getFieldRow("PartyAttorney");
    for (Map.Entry<PartyId, List<String>> partyAttys : info.getPartyAttorneyMap().entrySet()) {
      log.info("Setting Attorneys for : " + partyAttys.getKey());
      Object partyObj;
      if (partyIdToRefObj.containsKey(partyAttys.getKey().getIdString())) {
        partyObj = partyIdToRefObj.get(partyAttys.getKey().getIdString());
      } else if (partyIdToRefObj.isEmpty()) {
        log.warn(
            "No current filing parties, but "
                + partyAttys.getKey().getIdString()
                + " is in the current filing?");
        continue;
      } else {
        log.warn(
            "Can't handle current filing participant ("
                + partyAttys.getKey().getIdString()
                + ") not already added?!");
        continue;
      }
      ReferenceType repdRef = structObjFac.createReferenceType();
      repdRef.setRef(partyObj);
      if (!attRow.isvisible) {
        continue;
      }
      if (partyAttys.getValue().isEmpty()) {
        // Is Self-Represented
        if (attRow.isrequired) {
          InterviewVariable var =
              collector.requestVar(
                  "party_to_attorney", "Attorneys are required for this court", "DADict");
          collector.addRequired(var);
        }
        CaseOfficialType t = ecfCommonObjFac.createCaseOfficialType();
        t.getCaseRepresentedPartyReference().add(repdRef);
        ReferenceType selfRepresentedRep = structObjFac.createReferenceType();
        selfRepresentedRep.setRef(partyObj);
        t.setRoleOfPersonReference(selfRepresentedRep);
        ecfAug.getCaseOtherEntityAttorney().add(t);
      } else {
        if (!courtLocation.allowmultipleattorneys && partyAttys.getValue().size() > 1) {
          FilingError err =
              FilingError.malformedInterview(
                  "Court "
                      + info.getCourtLocation()
                      + " doesn't allow multiple lawyers per case party.");
          collector.error(err);
        }
        for (String attyId : partyAttys.getValue()) {
          CaseOfficialType t = ecfCommonObjFac.createCaseOfficialType();
          t.getCaseRepresentedPartyReference().add(repdRef);
          ReferenceType attRef = structObjFac.createReferenceType();
          attRef.setRef(attorneyIdToXmlId.get(attyId));
          t.setRoleOfPersonReference(attRef);
          ecfAug.getCaseOtherEntityAttorney().add(t);
        }
      }
    }

    List<CaseServiceContact> attachedContacts =
        info.getServiceContacts().stream()
            .filter(c -> c.partyAssociated.isPresent())
            .collect(Collectors.toList());
    boolean anyServicePartyAttached = attachedContacts.size() > 0;
    ecfAug.setAttachServiceContactIndicator(Ecf4Helper.convertBool(anyServicePartyAttached));
    for (CaseServiceContact attachedContact : attachedContacts) {
      ServicePartyDataType ref = tylerObjFac.createServicePartyDataType();
      ReferenceType servRef = structObjFac.createReferenceType();
      servRef.setRef(serviceContactXmlObjs.get(attachedContact.refId));
      ref.setServiceReference(servRef);
      attachedContact.partyAssociated.ifPresent(
          partyId -> {
            if (partyIdToRefObj.containsKey(partyId.getIdString())) {
              ReferenceType partyRef = structObjFac.createReferenceType();
              partyRef.setRef(partyIdToRefObj.get(partyId.getIdString()));
              ref.setPartyReference(partyRef);
            } else {
              log.warn(
                  "Couldn't find partyId: " + partyId + ", not in " + partyIdToRefObj.keySet());
            }
          });
      ecfAug.getExtendedData().add(tylerObjFac.createServicePartyReference(ref));
    }

    Optional<ProcedureRemedyType> res =
        makeProcedureRemedyType(
            isInitialFiling, comboCodes.cat, courtLocation.code, miscInfo, collector, serializer);

    Optional<ProcedureRemedyType> resPlus =
        addDamageAmountType(
            res,
            isInitialFiling,
            comboCodes.cat,
            courtLocation.code,
            miscInfo,
            collector,
            serializer);
    if (resPlus.isPresent()) {
      ecfAug.setProcedureRemedy(resPlus.get());
    }

    // Filing
    DataFieldRow filertype = serializer.allDataFields.getFieldRow("FilingFilerType");
    if (filertype.isvisible) {
      List<FilerType> allTypes = cd.getFilerTypes(courtLocation.code);
      String filerTypeName = "filer_type";
      JsonNode filerTypeNode = miscInfo.get(filerTypeName);
      InterviewVariable var =
          collector.requestVar(
              filerTypeName,
              "Metadata about the filer of this case",
              "choices",
              allTypes.stream().map(t -> t.code).collect(Collectors.toList()));
      if (filerTypeNode != null && filerTypeNode.isTextual()) {
        String filerType = filerTypeNode.asText();
        Optional<FilerType> typeInfo =
            allTypes.stream().filter(t -> t.code.equalsIgnoreCase(filerType)).findFirst();
        if (typeInfo.isPresent()) {
          ecfAug.setFilerTypeText(Ecf4Helper.convertText(typeInfo.get().code));
        } else {
          collector.addWrong(var);
        }
      } else {
        Optional<FilerType> defaultType = allTypes.stream().filter(t -> t.isDefault).findFirst();
        if (defaultType.isPresent()) {
          ecfAug.setFilerTypeText(Ecf4Helper.convertText(defaultType.get().code));
        } else if (filertype.isrequired) {
          collector.addRequired(var);
        }
      }
    }

    if (courtLocation.allowreturndate && info.getReturnDate().isPresent()) {
      ecfAug.setReturnDate(Ecf4Helper.convertDate(info.getReturnDate().get()));
    }

    // HACK(brycew): hacky: needed because "fees" querys and "service" put the payment stuff in the
    // tyler Aug, but not the review api.
    if (queryType.equals(QueryType.Fees) || queryType.equals(QueryType.Service)) {
      // if (info.getPaymentId() == null || info.getPaymentId().isBlank()) {
      //  collector.addRequired(collector.requestVar("tyler_payment_id", "The ID of the payment
      // method", "text"));
      // }
      ecfAug.setProviderCharge(
          PaymentFactory.makeProviderChargeType(info.getPaymentId(), this.jurisdiction));
    }

    if (miscInfo.has("max_fee_amount") && courtLocation.allowmaxfeeamount) {
      AmountType amountType = new AmountType();
      amountType.setCurrencyCode(CurrencyCodeSimpleType.USD);
      if (miscInfo.get("max_fee_amount").isNumber()) {
        BigDecimal amnt = miscInfo.get("max_fee_amount").decimalValue();
        amountType.setValue(amnt);
        ecfAug.setMaxFeeAmount(amountType);
      } else {
        FilingError err =
            FilingError.malformedInterview("max_fee_amount needs to be a decimal (float) value");
        collector.error(err);
      }
    }

    log.info("Full ecfAug: " + ecfAug);
    return Pair.of(tylerObjFac.createCaseAugmentation(ecfAug), partyIdToRefObj);
  }

  private Optional<ProcedureRemedyType> makeProcedureRemedyType(
      boolean initial,
      CaseCategory cat,
      String courtLocationId,
      JsonNode miscInfo,
      InfoCollector collector,
      EcfCourtSpecificSerializer serializer)
      throws SQLException, FilingError {
    List<NameAndCode> procedureRemedies = cd.getProcedureOrRemedy(courtLocationId, cat.getCode());
    String procedureViewName = "CivilCaseProcedureView" + ((initial) ? "Initial" : "Subsequent");
    DataFieldRow procedureView = DataFieldRow.MissingDataField(procedureViewName);
    String procBehavior =
        (initial) ? cat.getProcedureRemedyInitial() : cat.getProcedureRemedySubsequent();
    if (!procBehavior.isEmpty() && !procBehavior.equals("Not Available")) {
      procedureView = serializer.allDataFields.getFieldRow(procedureViewName);
    }
    InterviewVariable var;
    if (procedureRemedies.isEmpty()) {
      var = collector.requestVar("procedure_remedy", "Procedure Remedy", "text");
    } else {
      var =
          collector.requestVar(
              "procedure_remedy",
              "Procedure Remedy",
              "choices",
              procedureRemedies.stream().map(nac -> nac.getName()).collect(Collectors.toList()));
    }
    ProcedureRemedyType type = new ProcedureRemedyType();
    JsonNode proRem = miscInfo.get("procedure_remedy");
    if (proRem != null && !proRem.isNull() && proRem.isTextual()) {
      if (procedureView.isvisible) {
        List<NameAndCode> maybeProcedures =
            procedureRemedies.stream()
                .filter(nac -> nac.getName().equals(proRem.asText()))
                .collect(Collectors.toList());
        if (!maybeProcedures.isEmpty()) {
          for (NameAndCode nac : maybeProcedures) {
            type.getRemedyCode().add(Ecf4Helper.convertText(nac.getCode()));
          }
          return Optional.of(type);
        }
        collector.addWrong(var);
      } else {
        log.info(
            "Dropping procedure_remedy "
                + proRem.asText()
                + ", since isvisible is false for "
                + courtLocationId);
      }
    } else {
      if (procedureView.isrequired) {
        collector.addRequired(var);
      } else {
        collector.addOptional(var);
      }
    }
    return Optional.empty();
  }

  private Optional<ProcedureRemedyType> addDamageAmountType(
      Optional<ProcedureRemedyType> existingType,
      boolean initial,
      CaseCategory cat,
      String courtLocationId,
      JsonNode miscInfo,
      InfoCollector collector,
      EcfCourtSpecificSerializer serializer)
      throws SQLException, FilingError {
    List<NameAndCode> damageAmounts = cd.getDamageAmount(courtLocationId, cat.getCode());
    DataFieldRow damageConfig = cd.getDataField(courtLocationId, "DamageAmount");
    String damgBehavior =
        (initial) ? cat.getDamageAmountInitial() : cat.getDamageAmountSubsequent();
    if (!damgBehavior.isEmpty() && !damgBehavior.equals("Not Available")) {
      damageConfig =
          serializer.allDataFields.getFieldRow(
              "CivilCaseDamageAmount" + ((initial) ? "Initial" : "Subsequent"));
    }
    InterviewVariable docVar;
    if (damageAmounts.isEmpty()) {
      docVar = collector.requestVar("procedure_remedy", "Procedure Remedy", "text");
    } else {
      docVar =
          collector.requestVar(
              "procedure_remedy",
              "Procedure Remedy",
              "choices",
              damageAmounts.stream().map(nac -> nac.getName()).collect(Collectors.toList()));
    }
    JsonNode jsonDmg = miscInfo.get("damage_amount");
    if (jsonDmg != null && !jsonDmg.isNull() && jsonDmg.isTextual()) {
      if (damageConfig.isvisible) {
        Optional<NameAndCode> maybeDmg =
            damageAmounts.stream()
                .filter(nac -> nac.getName().equals(jsonDmg.asText()))
                .findFirst();
        if (maybeDmg.isPresent()) {
          ProcedureRemedyType type = existingType.orElse(new ProcedureRemedyType());
          type.setDamageAmountCode(Ecf4Helper.convertText(maybeDmg.get().getCode()));
          return Optional.of(type);
        } else {
          collector.addWrong(docVar);
        }
      } else {
        log.info(
            "Dropping damage_amount "
                + jsonDmg.asText()
                + ", since isvisible is false for "
                + courtLocationId);
      }
    } else {
      if (damageConfig.isrequired) {
        collector.addRequired(docVar);
      } else {
        collector.addOptional(docVar);
      }
    }
    return existingType;
  }

  /**
   * Makes a civil case to include in queries like FeesQuery and ReviewFiling.
   *
   * @param caseAug The Niem JXDM augmentation type, contains the case court
   * @param tylerAug The Tyler case augementation type, contains most of the case info
   * @param amountInControversy "The ad damnum value. Only applicable if the filing code is
   *     configured AmountInControversy=Required. When enabled this value affects the calculations
   *     for fees and return date."
   * @return A complete Civil Case type
   */
  private static JAXBElement<CivilCaseType> makeCivilCaseType(
      JAXBElement<gov.niem.niem.domains.jxdm._4.CaseAugmentationType> caseAug,
      JAXBElement<tyler.ecf.extensions.common.CaseAugmentationType> tylerAug,
      Optional<String> caseDocketId,
      Optional<String> caseTrackingId,
      Optional<BigDecimal> amountInControversy) {
    var ecfCivilObjFac =
        new oasis.names.tc.legalxml_courtfiling.schema.xsd.civilcase_4.ObjectFactory();
    var ecfCommonObjFac =
        new oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ObjectFactory();
    CivilCaseType c = ecfCivilObjFac.createCivilCaseType();
    caseDocketId.ifPresent(
        docket -> {
          c.setCaseDocketID(Ecf4Helper.convertString(docket));
        });
    caseTrackingId.ifPresent(
        trackingId -> {
          c.setCaseTrackingID(Ecf4Helper.convertString(trackingId));
        });
    c.getRest().add(caseAug);
    c.getRest().add(tylerAug);
    JAXBElement<TextType> causeOfAction = ecfCommonObjFac.createCauseOfActionCode(new TextType());
    c.getRest().add(causeOfAction);

    if (amountInControversy.isPresent()) {
      AmountType amount = new AmountType();
      amount.setValue(amountInControversy.get());
      amount.setCurrencyCode(CurrencyCodeSimpleType.USD);
      c.getRest().add(ecfCivilObjFac.createAmountInControversy(amount));
    }
    c.getRest().add(ecfCivilObjFac.createClassActionIndicator(Ecf4Helper.convertBool(false)));
    c.getRest().add(ecfCivilObjFac.createJurisdictionalGroundsCode(Ecf4Helper.convertText("")));
    c.getRest().add(ecfCivilObjFac.createJuryDemandIndicator(Ecf4Helper.convertBool(false)));
    JAXBElement<TextType> relief = ecfCivilObjFac.createReliefTypeCode(new TextType());
    c.getRest().add(relief);
    return ecfCivilObjFac.createCivilCase(c);
  }

  /**
   * Required JSON info from DA interview: - lower_court_case.title - lower_court_case.docket_number
   * - lower_court_case.judge - trial_court.name (specifically lower_court_case.court.name)
   */
  private static JAXBElement<AppellateCaseType> makeAppellateCaseType(
      JAXBElement<gov.niem.niem.domains.jxdm._4.CaseAugmentationType> caseAug,
      JAXBElement<tyler.ecf.extensions.common.CaseAugmentationType> tylerAug,
      FilingInformation info,
      JsonNode node,
      InfoCollector collector)
      throws FilingError {
    var ecfAppellateObjFac =
        new oasis.names.tc.legalxml_courtfiling.schema.xsd.appellatecase_4.ObjectFactory();
    var niemObjFac = new gov.niem.niem.niem_core._2.ObjectFactory();
    AppellateCaseType appl = ecfAppellateObjFac.createAppellateCaseType();
    info.getCaseDocketNumber()
        .ifPresent(
            docket -> {
              appl.setCaseDocketID(Ecf4Helper.convertString(docket));
            });
    info.getPreviousCaseId()
        .ifPresent(
            trackingId -> {
              appl.setCaseTrackingID(Ecf4Helper.convertString(trackingId));
            });

    appl.getRest().add(caseAug);

    CaseType ct = niemObjFac.createCaseType();

    InterviewVariable lowerNameVar =
        collector.requestVar("trial_court.name", "The lower court name", "text");
    if (info.getLowerCourtInfo().isPresent()) {
      var lowerInfo = info.getLowerCourtInfo().get();
      if (lowerInfo.lowerCourtCode == null || lowerInfo.lowerCourtCode.isBlank()) {
        collector.addRequired(lowerNameVar);
      }
      tylerAug.getValue().setLowerCourtText(Ecf4Helper.convertText(lowerInfo.lowerCourtCode));
      if (lowerInfo.lowerCourtJudgeName == null || lowerInfo.lowerCourtJudgeName.isBlank()) {
        InterviewVariable var =
            collector.requestVar(
                "judge", "The name of the Judge who gave the lower court decision", "text");
        collector.addRequired(var);
      }
      tylerAug
          .getValue()
          .setLowerCourtJudgeText(Ecf4Helper.convertText(lowerInfo.lowerCourtJudgeName));
      if (lowerInfo.caseTitleText.isBlank()) {
        InterviewVariable var =
            collector.requestVar("title", "The name of the lower court case", "text");
        collector.addRequired(var);
      }
      ct.setCaseTitleText(Ecf4Helper.convertText(lowerInfo.caseTitleText));
      if (lowerInfo.caseDocketId.isBlank()) {
        InterviewVariable var =
            collector.requestVar(
                "docket_number", "The docket number of the lower court case", "text");
        collector.addRequired(var);
      }
      ct.setCaseDocketID(Ecf4Helper.convertString(lowerInfo.caseDocketId));
      appl.getRest().add(tylerAug);
      appl.getAppellateCaseOriginalCase().add(ct);
    } else {
      collector.addRequired(lowerNameVar);
    }

    return ecfAppellateObjFac.createAppellateCase(appl);
  }

  private static JAXBElement<DomesticCaseType> makeDomesticCaseType(
      JAXBElement<gov.niem.niem.domains.jxdm._4.CaseAugmentationType> caseAug,
      JAXBElement<tyler.ecf.extensions.common.CaseAugmentationType> tylerAug,
      Optional<String> caseDocketId,
      Optional<String> caseTrackingId,
      JsonNode node) {
    var ecfDomesticObjFac =
        new oasis.names.tc.legalxml_courtfiling.schema.xsd.domesticcase_4.ObjectFactory();
    var ecfCommonObjFac =
        new oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ObjectFactory();
    DomesticCaseType d = ecfDomesticObjFac.createDomesticCaseType();
    caseDocketId.ifPresent(
        docket -> {
          d.setCaseDocketID(Ecf4Helper.convertString(docket));
        });
    caseTrackingId.ifPresent(
        trackingId -> {
          d.setCaseTrackingID(Ecf4Helper.convertString(trackingId));
        });
    d.getRest().add(caseAug);
    d.getRest().add(tylerAug);
    JAXBElement<TextType> causeOfAction = ecfCommonObjFac.createCauseOfActionCode(new TextType());
    d.getRest().add(causeOfAction);
    boolean contestedCase = false;
    JsonNode jsonContested = node.get("is_contested_case");
    if (jsonContested != null && jsonContested.isBoolean()) {
      contestedCase = jsonContested.asBoolean();
    }
    d.getRest()
        .add(ecfDomesticObjFac.createCaseContestedIndicator(Ecf4Helper.convertBool(contestedCase)));
    d.getRest().add(ecfDomesticObjFac.createDomesticCasePerson(new PersonType()));
    return ecfDomesticObjFac.createDomesticCase(d);
  }

  /*
  private JAXBElement<CriminalCaseType> makeCriminalCaseType(
      JAXBElement<gov.niem.niem.domains.jxdm._4.CaseAugmentationType> caseAug,
      JAXBElement<tyler.ecf.extensions.common.CaseAugmentationType> tylerAug) {
    // BIG OLD TODO(#86): make all of the TODO's separate issues/tickets to implement criminal
    // stuff. There's alot here.
    // TODO(#86): support criminal case filings

    // TODO(#86): there are just too many tables for separate TODOs.
    // Most of these are empty in IL, so we aren't supporting for now.
    // Listing them individually:
    // * vehicletype, i.e. Four Door, 34 PU, Moped, etc.
    // * vehiclemake
    // * vehiclecolor
    // * statuetypes, a whole lot, lol
    // * statute
    // * race: PersonRaceText in ReviewFiling
    // * question: QuestionAnswer in ReviewFiling
    // * answer: QuestionAnswer in ReviewFiling
    // * bond: relates to CaseChargeBondAmount, CaseChargeBondType, and BondTypeText in ReviewFiling
    // * arrestlocation
    // * chargephase: in ReviewFiling Criminal
    // * citationjurisdiction
    // * haircolor
    // * general offense
    // * lawenforcementunit: EnforcementOfficialText
    // * eyecolor
    // * ethnicity
    // * physicalfeature: personphysicalfeature, and physicalfeature
    // * degree: statutelevelchange
    return null;
  }
  */
}

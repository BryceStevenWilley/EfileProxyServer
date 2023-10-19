package edu.suffolk.litlab.efspserver.ecf4;

import static edu.suffolk.litlab.efspserver.StdLib.exists;
import static edu.suffolk.litlab.efspserver.StdLib.strFromException;
import static edu.suffolk.litlab.efspserver.services.ServiceHelpers.setupFirmPort;

import com.hubspot.algebra.Result;
import edu.suffolk.litlab.efspserver.CaseServiceContact;
import edu.suffolk.litlab.efspserver.FilingDoc;
import edu.suffolk.litlab.efspserver.FilingInformation;
import edu.suffolk.litlab.efspserver.PartyId;
import edu.suffolk.litlab.efspserver.Person;
import edu.suffolk.litlab.efspserver.StdLib;
import edu.suffolk.litlab.efspserver.services.EfmCheckableFilingInterface;
import edu.suffolk.litlab.efspserver.services.FailFastCollector;
import edu.suffolk.litlab.efspserver.services.FilingError;
import edu.suffolk.litlab.efspserver.services.FilingResult;
import edu.suffolk.litlab.efspserver.services.InfoCollector;
import edu.suffolk.litlab.efspserver.services.InterviewVariable;
import edu.suffolk.litlab.efspserver.services.ServiceHelpers;
import edu.suffolk.litlab.efspserver.tyler.QueryType;
import edu.suffolk.litlab.efspserver.tyler.TylerLogin;
import edu.suffolk.litlab.efspserver.tyler.TylerUrls;
import edu.suffolk.litlab.efspserver.tyler.TylerUserNamePassword;
import edu.suffolk.litlab.efspserver.tyler.codes.CodeDatabase;
import edu.suffolk.litlab.efspserver.tyler.codes.ComboCaseCodes;
import edu.suffolk.litlab.efspserver.tyler.codes.CourtLocationInfo;
import edu.suffolk.litlab.efspserver.tyler.codes.DataFieldRow;
import edu.suffolk.litlab.efspserver.tyler.codes.Disclaimer;
import edu.suffolk.litlab.efspserver.tyler.codes.FilingCode;
import edu.suffolk.litlab.efspserver.tyler.codes.ServiceCodeType;
import gov.niem.niem.niem_core._2.DateRangeType;
import gov.niem.niem.niem_core._2.DateType;
import gov.niem.niem.niem_core._2.EntityType;
import gov.niem.niem.niem_core._2.IdentificationType;
import gov.niem.niem.niem_core._2.MeasureType;
import gov.niem.niem.niem_core._2.TextType;
import gov.niem.niem.proxy.xsd._2.Date;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.BindingProvider;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.casequerymessage_4.CaseQueryMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.caseresponsemessage_4.CaseResponseMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ElectronicServiceInformationType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.QueryMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.corefilingmessage_4.CoreFilingMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.courtpolicyresponsemessage_4.CourtPolicyResponseMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.feescalculationquerymessage_4.FeesCalculationQueryMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.feescalculationresponsemessage_4.FeesCalculationResponseMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.filinglistquerymessage_4.FilingListQueryMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.filinglistresponsemessage_4.FilingListResponseMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.filinglistresponsemessage_4.MatchingFilingType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.filingstatusquerymessage_4.FilingStatusQueryMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.filingstatusresponsemessage_4.FilingStatusResponseMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.messagereceiptmessage_4.MessageReceiptMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.paymentmessage_4.PaymentMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.servicereceiptmessage_4.ServiceReceiptMessageType;
import oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4.ReviewFilingRequestMessageType;
import oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4_0.CourtRecordMDEPort;
import oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4_0.FilingReviewMDEPort;
import oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4_0.ServiceMDEPort;
import org.apache.cxf.headers.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tyler.ecf.extensions.cancelfilingmessage.CancelFilingMessageType;
import tyler.ecf.extensions.cancelfilingresponsemessage.CancelFilingResponseMessageType;
import tyler.ecf.extensions.common.DocumentType;
import tyler.ecf.extensions.filingdetailquerymessage.FilingDetailQueryMessageType;
import tyler.ecf.extensions.filingdetailresponsemessage.FilingDetailResponseMessageType;
import tyler.ecf.extensions.filingservicequerymessage.FilingServiceQueryMessageType;
import tyler.ecf.extensions.filingservicequerymessage.ServiceContactIdentificationType;
import tyler.ecf.extensions.filingserviceresponsemessage.FilingServiceResponseMessageType;
import tyler.ecf.extensions.servicetypesrequestmessage.ServiceTypesRequestMessageType;
import tyler.ecf.extensions.servicetypesresponsemessage.ServiceTypesResponseMessageType;
import tyler.efm.services.EfmFirmService;
import tyler.efm.services.IEfmFirmService;
import tyler.efm.wsdl.webservicesprofile_implementation_4_0.CourtRecordMDEService;
import tyler.efm.wsdl.webservicesprofile_implementation_4_0.FilingReviewMDEService;
import tyler.efm.wsdl.webservicesprofile_implementation_4_0.ServiceMDEService;

public class Ecf4Filer extends EfmCheckableFilingInterface {
  private static Logger log = LoggerFactory.getLogger(Ecf4Filer.class);
  private final Supplier<CodeDatabase> cdSupplier;
  private final String headerKey;
  private final oasis.names.tc.legalxml_courtfiling.schema.xsd.filingstatusquerymessage_4
          .ObjectFactory
      statusObjFac;
  private final oasis.names.tc.legalxml_courtfiling.schema.xsd.filinglistquerymessage_4
          .ObjectFactory
      listObjFac;
  private final tyler.ecf.extensions.filingdetailquerymessage.ObjectFactory detailObjFac;
  private final tyler.ecf.extensions.cancelfilingmessage.ObjectFactory cancelObjFac;
  private final oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ObjectFactory
      commonObjFac;
  private final gov.niem.niem.niem_core._2.ObjectFactory niemObjFac;
  private final gov.niem.niem.proxy.xsd._2.ObjectFactory proxyObjFac;
  private final CourtRecordMDEService recordFactory;
  private final FilingReviewMDEService filingFactory;
  private final EfmFirmService firmFactory;
  private final ServiceMDEService serviceFactory;
  private static final PolicyCacher policyCacher = new PolicyCacher();
  private final String jurisdiction;

  public Ecf4Filer(String jurisdiction, String env, Supplier<CodeDatabase> cdSupplier) {
    this.jurisdiction = jurisdiction;
    this.cdSupplier = cdSupplier;
    TylerLogin login = new TylerLogin(jurisdiction, env);
    this.headerKey = login.getHeaderKey();
    statusObjFac =
        new oasis.names.tc.legalxml_courtfiling.schema.xsd.filingstatusquerymessage_4
            .ObjectFactory();
    listObjFac =
        new oasis.names.tc.legalxml_courtfiling.schema.xsd.filinglistquerymessage_4.ObjectFactory();
    detailObjFac = new tyler.ecf.extensions.filingdetailquerymessage.ObjectFactory();
    cancelObjFac = new tyler.ecf.extensions.cancelfilingmessage.ObjectFactory();
    commonObjFac = new oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ObjectFactory();
    niemObjFac = new gov.niem.niem.niem_core._2.ObjectFactory();
    proxyObjFac = new gov.niem.niem.proxy.xsd._2.ObjectFactory();
    Optional<CourtRecordMDEService> maybeCourt =
        SoapClientChooser.getCourtRecordFactory(jurisdiction, env);
    if (maybeCourt.isEmpty()) {
      throw new RuntimeException("Cannot find " + jurisdiction + " for court record factory");
    }
    this.recordFactory = maybeCourt.get();
    Optional<FilingReviewMDEService> maybeReview =
        SoapClientChooser.getFilingReviewFactory(jurisdiction, env);
    if (maybeReview.isEmpty()) {
      throw new RuntimeException("Cannot find " + jurisdiction + " for filing review factory");
    }
    this.filingFactory = maybeReview.get();
    Optional<ServiceMDEService> maybeServiceFac =
        SoapClientChooser.getServiceFactory(jurisdiction, env);
    if (maybeServiceFac.isEmpty()) {
      throw new RuntimeException("Cannot find " + jurisdiction + " for service mde factory");
    }
    this.serviceFactory = maybeServiceFac.get();
    Optional<EfmFirmService> maybeFirmFactory = TylerUrls.getEfmFirmFactory(jurisdiction, env);
    if (maybeFirmFactory.isEmpty()) {
      throw new RuntimeException("Cannot find " + jurisdiction + " for firm mde factory");
    }
    this.firmFactory = maybeFirmFactory.get();
  }

  @Override
  public String getHeaderKey() {
    return this.headerKey;
  }

  private CoreMessageAndNames prepareFiling(
      FilingInformation info,
      InfoCollector collector,
      String apiToken,
      FilingReviewMDEPort filingPort,
      CourtRecordMDEPort recordPort,
      QueryType queryType)
      throws FilingError {
    String existingCaseTitle = null;
    String caseCategoryName = "";
    try (CodeDatabase cd = cdSupplier.get()) {
      EcfCaseTypeFactory ecfCaseFactory = new EcfCaseTypeFactory(cd, this.jurisdiction);
      Optional<CourtLocationInfo> maybeLocationInfo =
          cd.getFullLocationInfo(info.getCourtLocation());
      if (maybeLocationInfo.isEmpty()) {
        collector.error(
            FilingError.serverError(
                "Court setup wrong: can't find full location info for " + info.getCourtLocation()));
      }

      CourtLocationInfo locationInfo = maybeLocationInfo.orElse(new CourtLocationInfo());
      String courtName = locationInfo.name;

      CourtPolicyResponseMessageType policy =
          policyCacher.getPolicyFor(filingPort, info.getCourtLocation());

      if (!locationInfo.allowfilingintononindexedcase
          && info.getCaseDocketNumber().isPresent()
          && info.getPreviousCaseId().isEmpty()) {
        FilingError err =
            FilingError.malformedInterview(
                "Court "
                    + info.getCourtLocation()
                    + " doesn't allow subsequent filing into non-indexed cases. If this case is in"
                    + " the court system, provide the Case tracking ID. If it's not, don't provide"
                    + " the docket number.");
        collector.error(err);
      }

      EcfCourtSpecificSerializer serializer = new EcfCourtSpecificSerializer(cd, locationInfo);
      boolean isFirstIndexedFiling = info.getPreviousCaseId().isEmpty();
      ComboCaseCodes allCodes;
      if (!isFirstIndexedFiling) {
        CaseQueryMessageType query = new CaseQueryMessageType();
        Ecf4Helper.prep(query, info.getCourtLocation());
        query.setCaseTrackingID(Ecf4Helper.convertString(info.getPreviousCaseId().get()));
        query.setCaseQueryCriteria(EcfCaseTypeFactory.getCriteria());
        CaseResponseMessageType resp = recordPort.getCase(query);
        if (resp.getCase() != null && resp.getCase().getValue() != null) {
          if (resp.getCase().getValue().getCaseTitleText() != null) {
            existingCaseTitle = resp.getCase().getValue().getCaseTitleText().getValue();
          }
        } else {
          var filingVar =
              collector.requestVar(
                  "previous_case_id",
                  "Could not find the given case id (" + info.getPreviousCaseId().get() + ")",
                  "text");
          collector.addWrong(filingVar);
        }

        String catCode = resp.getCase().getValue().getCaseCategoryText().getValue();
        String typeCode =
            EcfCaseTypeFactory.getCaseAugmentation(resp.getCase().getValue())
                .get()
                .getCaseTypeText()
                .getValue();
        Map<PartyId, Person> exisitingPartips =
            EcfCaseTypeFactory.getCaseParticipants(resp.getCase().getValue()).get();
        List<Optional<String>> maybeFilingCodes =
            info.getFilings().stream().map(f -> f.getFilingCode()).collect(Collectors.toList());
        if (maybeFilingCodes.stream().anyMatch(fc -> fc.isEmpty())) {
          InterviewVariable filingVar =
              collector.requestVar(
                  "court_bundle[i].filing_type", "What filing type is this?", "text");
          collector.addRequired(filingVar);
        }
        List<String> filingCodeStrs =
            maybeFilingCodes.stream().map(fc -> fc.orElse("")).collect(Collectors.toList());
        Map<String, Person> newPartyCodes =
            Stream.concat(info.getNewPlaintiffs().stream(), info.getNewDefendants().stream())
                .collect(Collectors.toMap(per -> per.getIdString(), per -> per));
        Map<String, Person> existingPartyCodes =
            exisitingPartips.entrySet().stream()
                .collect(
                    Collectors.toMap(
                        ent -> ent.getKey().getIdentificationString(), ent -> ent.getValue()));
        log.info(
            "Existing cat, type, and filings: " + catCode + "," + typeCode + "," + filingCodeStrs);
        allCodes =
            serializer.serializeCaseCodesIndexed(
                catCode,
                typeCode,
                filingCodeStrs,
                existingPartyCodes,
                newPartyCodes,
                collector,
                info.isInitialFiling());
      } else {
        allCodes = serializer.serializeCaseCodes(info, collector, info.isInitialFiling());
      }
      caseCategoryName = allCodes.cat.getName().orElse(allCodes.cat.getCode());
      log.info("have all codes");

      var coreObjFac =
          new oasis.names.tc.legalxml_courtfiling.schema.xsd.corefilingmessage_4.ObjectFactory();
      CoreFilingMessageType cfm = coreObjFac.createCoreFilingMessageType();

      int i = 0;
      Map<String, Object> serviceContactXmlObjs = new HashMap<>();
      for (CaseServiceContact servContact : info.getServiceContacts()) {
        ElectronicServiceInformationType servInfo =
            commonObjFac.createElectronicServiceInformationType();
        List<ServiceCodeType> types = cd.getServiceTypes(info.getCourtLocation());
        Optional<ServiceCodeType> serviceCode =
            types.stream()
                .filter(t -> t.code.equalsIgnoreCase(servContact.serviceType))
                .findFirst();
        if (serviceCode.isEmpty()) {
          InterviewVariable var =
              collector.requestVar(
                  "service_contact[" + i + "].service_type",
                  "service type should be",
                  "choices",
                  types.stream().map(t -> t.code).collect(Collectors.toList()));
          collector.addWrong(var);
        }
        /*
        if (serviceCode.get().code.equals("-580") && isInitialFiling && locationInfo.get().disallowelectronicserviceonnewcontacts) { // Eservice
          collector.addWrong(var.appendDesc(", but can't be e-service for an initial contact"));
        }
        */
        IdentificationType id = Ecf4Helper.convertId(servContact.guid, "SERVICECONTACTID");
        id.setIdentificationSourceText(Ecf4Helper.convertText(serviceCode.get().code));
        servInfo.setServiceRecipientID(id);
        servInfo.setId(servContact.refId);
        servInfo.setReceivingMDEProfileCode(ServiceHelpers.MDE_PROFILE_CODE);
        servInfo.setReceivingMDELocationID(Ecf4Helper.convertId(ServiceHelpers.ASSEMBLY_PORT));
        serviceContactXmlObjs.put(servContact.refId, servInfo);
        cfm.getElectronicServiceInformation().add(servInfo);
      }

      log.info("Assembling case");
      var pair =
          ecfCaseFactory.makeCaseTypeFromTylerCategory(
              locationInfo,
              allCodes,
              info,
              info.isInitialFiling(),
              isFirstIndexedFiling,
              queryType,
              info.getMiscInfo(),
              serializer,
              collector,
              serviceContactXmlObjs);
      JAXBElement<? extends gov.niem.niem.niem_core._2.CaseType> assembledCase = pair.getLeft();
      log.info("Assembled case");

      Map<String, String> crossReferences =
          serializer.getCrossRefIds(info.getMiscInfo(), collector, allCodes.type.getCode());
      for (Map.Entry<String, String> ref : crossReferences.entrySet()) {
        IdentificationType id = niemObjFac.createIdentificationType();
        id.setIdentificationID(Ecf4Helper.convertString(ref.getValue()));
        id.setIdentificationCategory(
            niemObjFac.createIdentificationCategoryText(
                Ecf4Helper.convertText("CaseCrossReferenceNumber")));
        id.setIdentificationSourceText(Ecf4Helper.convertText(ref.getKey()));
        cfm.getDocumentIdentification().add(id);
      }

      Optional<Boolean> serviceOnInitial = locationInfo.allowserviceoninitial;
      if (serviceOnInitial.isEmpty()) {
        serviceOnInitial =
            Optional.of(
                cd.getDataField(locationInfo.code, "FilingServiceCheckBoxInitial").isvisible);
      }
      if (info.isInitialFiling()
          && !serviceOnInitial.orElse(
              cd.getDataField(locationInfo.code, "FilingServiceCheckBoxInitial").isvisible)
          && info.getServiceContacts().size() > 0) {
        FilingError err =
            FilingError.malformedInterview(
                "Court " + locationInfo.name + " doesn't allow service on initial filings");
        collector.error(err);
      }
      DataFieldRow checkBoxSub =
          cd.getDataField(locationInfo.code, "FilingServiceCheckBoxSubsequent");
      if (!info.isInitialFiling()
          && !checkBoxSub.isvisible
          && info.getServiceContacts().size() > 0) {
        FilingError err =
            FilingError.malformedInterview(
                "Court " + locationInfo.name + " doesn't allow service on subsequent filings");
        collector.error(err);
      }

      cfm.setSendingMDELocationID(Ecf4Helper.convertId(ServiceHelpers.SERVICE_URL));
      cfm.setSendingMDEProfileCode(ServiceHelpers.MDE_PROFILE_CODE);
      cfm.setCase(assembledCase);

      MeasureType maxIndivDocSize =
          policy.getDevelopmentPolicyParameters().getValue().getMaximumAllowedAttachmentSize();
      long maxSize = Ecf4Helper.sizeMeasureAsBytes(maxIndivDocSize);
      long cumulativeBytes = 0;

      Optional<IEfmFirmService> firmPort = setupFirmPort(firmFactory, apiToken);
      boolean isIndividual =
          firmPort.map(port -> port.getFirm().getFirm().isIsIndividual()).orElse(true);
      Map<String, Object> filingIdToObj = new HashMap<>();
      int seqNum = 0;
      for (FilingDoc filingDoc : info.getFilings()) {
        long bytes = filingDoc.allAttachmentsLength();
        if (bytes > maxSize) {
          FilingError err =
              FilingError.malformedInterview(
                  "Document "
                      + filingDoc
                          .getDescription()
                          .map(d -> d.get())
                          .orElse(filingDoc.getFilingComments())
                      + " is too big! Must be max "
                      + maxSize
                      + ", is "
                      + bytes);
          collector.error(err);
        }
        cumulativeBytes += bytes;

        FilingCode fc = allCodes.filings.get(seqNum);

        collector.pushAttributeStack("al_court_bundle[" + seqNum + "]");
        JAXBElement<DocumentType> result =
            serializer.filingDocToXml(
                filingDoc,
                seqNum,
                info.isInitialFiling(),
                allCodes.cat,
                allCodes.type,
                fc,
                isIndividual,
                info.getMiscInfo(),
                collector);
        collector.popAttributeStack();
        filingIdToObj.put(filingDoc.getIdString(), result.getValue());
        if (filingDoc.isLead()) {
          cfm.getFilingLeadDocument().add(result);
        } else {
          cfm.getFilingConnectedDocument().add(result);
        }
        seqNum += 1;
        log.info("Added a document to the XML");
      }
      MeasureType maxTotalDocSize =
          policy.getDevelopmentPolicyParameters().getValue().getMaximumAllowedMessageSize();
      long maxTotal = Ecf4Helper.sizeMeasureAsBytes(maxTotalDocSize);
      if (cumulativeBytes > maxTotal) {
        FilingError err =
            FilingError.malformedInterview(
                "All Documents combined are too big! Must be max"
                    + maxSize
                    + ", are "
                    + cumulativeBytes);
        collector.error(err);
      }
      EcfCaseTypeFactory.getCaseAugmentation(assembledCase.getValue())
          .ifPresent(
              aug -> {
                Map<String, List<PartyId>> filingAssociations =
                    info.getFilings().stream()
                        .collect(
                            Collectors.toMap(f -> f.getIdString(), f -> f.getFilingPartyIds()));
                for (var association :
                    ecfCaseFactory.lateStageFilingAssociationAdd(
                        serializer, filingIdToObj, filingAssociations, pair.getRight())) {
                  aug.getFilingAssociation().add(association);
                }
              });
      log.info(
          "Full cfm: "
              + Ecf4Helper.objectToXmlStrOrError(cfm, CoreFilingMessageType.class)
                  .replaceAll("<ns2:BinaryBase64Object>[^<]+<\\/ns2:BinaryBase64Object>", ""));
      return new CoreMessageAndNames(cfm, existingCaseTitle, caseCategoryName, courtName);
    } catch (IOException | SQLException ex) {
      log.error("IO Error when making filing! " + strFromException(ex));
      throw FilingError.serverError("Got Exception assembling the filing: " + ex);
    }
  }

  private Result<FilingResult, FilingError> serveFilingIfReady(
      CoreFilingMessageType cfm, FilingInformation info, InfoCollector collector, String apiToken) {
    Optional<ServiceMDEPort> port = setupServicePort(apiToken);
    if (port.isEmpty()) {
      return Result.err(
          FilingError.serverError("Couldn't make a service MDE port with the given API token"));
    }
    ServiceReceiptMessageType receipt = port.get().serveFiling(cfm);
    StringBuilder sb = new StringBuilder();
    boolean anyErrors = false;
    for (oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ErrorType err :
        receipt.getError()) {
      if (!err.getErrorCode().getValue().equalsIgnoreCase("0")) {
        anyErrors = true;
        sb.append("\n  * " + err.getErrorText().getValue());
      }
    }
    if (anyErrors) {
      try {
        FilingError err = FilingError.serverError(sb.toString());
        collector.error(err);
        return Result.err(err);
      } catch (FilingError err) {
        return Result.err(err);
      }
    }
    List<UUID> ids =
        receipt.getServiceRecipientStatus().stream()
            .map(
                st -> {
                  String id = st.getServiceRecipientID().getIdentificationID().getValue();
                  return UUID.fromString(id);
                })
            .collect(Collectors.toList());
    return Result.ok(new FilingResult(ids, info.getLeadContact()));
  }

  @Override
  public Result<FilingResult, FilingError> submitFilingIfReady(
      FilingInformation info, InfoCollector collector, String apiToken, ApiChoice choice) {
    FilingReviewMDEPort filingPort;
    CoreFilingMessageType cfm;
    String existingCaseTitle = null;
    String caseCategoryName = null;
    String courtName = null;
    try {
      Optional<FilingReviewMDEPort> maybeFilingPort = setupFilingPort(apiToken);
      Optional<CourtRecordMDEPort> recordPort = setupRecordPort(apiToken);
      if (maybeFilingPort.isEmpty() || recordPort.isEmpty()) {
        FilingError err =
            FilingError.serverError("Couldn't create SOAP port object with token: " + apiToken);
        collector.error(err);
      }
      filingPort = maybeFilingPort.get();
      QueryType queryType =
          (choice.equals(ApiChoice.ServiceApi)) ? QueryType.Service : QueryType.Review;
      var coreAndExisting =
          prepareFiling(info, collector, apiToken, filingPort, recordPort.get(), queryType);
      cfm = coreAndExisting.cfm;
      caseCategoryName = coreAndExisting.caseCategoryName;
      courtName = coreAndExisting.courtName;
      existingCaseTitle = coreAndExisting.existingCaseTitle;
      // if (!queryType.equals(QueryType.Service)
      //    && (info.getPaymentId() == null || info.getPaymentId().isBlank())) {
      //  collector.addRequired(collector.requestVar("tyler_payment_id", "The ID of the payment
      // method", "text"));
      // }
    } catch (FilingError err) {
      return Result.err(err);
    }

    if (!collector.okToSubmit()) {
      return Result.ok(new FilingResult(List.of(), null));
    }

    if (choice.equals(ApiChoice.ServiceApi)) {
      return serveFilingIfReady(cfm, info, collector, apiToken);
    }

    var wsOf =
        new oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4
            .ObjectFactory();
    PaymentMessageType pmt =
        PaymentFactory.makePaymentMessage(info.getPaymentId(), this.jurisdiction);
    ReviewFilingRequestMessageType rfrm = wsOf.createReviewFilingRequestMessageType();
    rfrm.setSendingMDELocationID(Ecf4Helper.convertId(ServiceHelpers.SERVICE_URL));
    rfrm.setSendingMDEProfileCode(ServiceHelpers.MDE_PROFILE_CODE);
    rfrm.setCoreFilingMessage(cfm);
    rfrm.setPaymentMessage(pmt);

    log.debug(Ecf4Helper.objectToXmlStrOrError(rfrm, ReviewFilingRequestMessageType.class));
    MessageReceiptMessageType mrmt = filingPort.reviewFiling(rfrm);
    if (mrmt.getError().size() > 0) {
      for (var err : mrmt.getError()) {
        if (!err.getErrorCode().getValue().equals("0")) {
          log.warn(
              "Error from Tyler: "
                  + err.getErrorCode().getValue()
                  + ", "
                  + err.getErrorText().getValue());
        }
      }
    }
    BiFunction<IdentificationType, String, Boolean> filterId =
        (id, idType) -> {
          TextType text = (TextType) id.getIdentificationCategory().getValue();
          return text.getValue().equalsIgnoreCase(idType);
        };
    List<IdentificationType> ids = mrmt.getDocumentIdentification();
    List<String> filingIdStrs =
        ids.stream()
            .filter(id -> filterId.apply(id, "FILINGID"))
            .map((id) -> id.getIdentificationID().getValue())
            .collect(Collectors.toList());
    Optional<String> caseId =
        ids.stream()
            .filter(id -> filterId.apply(id, "CASEID"))
            .map(id -> id.getIdentificationID().getValue())
            .findFirst();
    Optional<String> envelopeId =
        ids.stream()
            .filter(id -> filterId.apply(id, "ENVELOPEID"))
            .map(id -> id.getIdentificationID().getValue())
            .findFirst();
    if (filingIdStrs.isEmpty()) {
      log.error("Couldn't get back the filing id from Tyler!");
      return Result.err(
          FilingError.serverError(
              "Couldn't get back filing id from tyler: "
                  + mrmt.getError().stream()
                      .reduce(
                          "",
                          (str, err) -> {
                            return str + ", " + err.getErrorText();
                          },
                          (str, str2) -> str + str2)));
    }
    String caseTitle = (exists(existingCaseTitle)) ? existingCaseTitle : info.makeCaseTitle();
    log.info(Ecf4Helper.objectToXmlStrOrError(mrmt, MessageReceiptMessageType.class));
    return Result.ok(
        new FilingResult(
            caseId.get(),
            envelopeId.get(),
            filingIdStrs.stream().map(str -> UUID.fromString(str)).collect(Collectors.toList()),
            info.getLeadContact(),
            caseTitle,
            caseCategoryName,
            courtName));
  }

  @Override
  public Result<Response, FilingError> getFilingFees(FilingInformation info, String apiToken) {
    FailFastCollector collector = new FailFastCollector();
    CoreFilingMessageType cfm;
    Optional<FilingReviewMDEPort> filingPort = setupFilingPort(apiToken);
    Optional<CourtRecordMDEPort> recordPort = setupRecordPort(apiToken);
    if (filingPort.isEmpty() || recordPort.isEmpty()) {
      FilingError err =
          FilingError.serverError("Couldn't create SOAP port object with token: " + apiToken);
      return Result.err(err);
    }
    log.info("Getting Filing Fees");
    try {
      cfm =
          prepareFiling(
                  info, collector, apiToken, filingPort.get(), recordPort.get(), QueryType.Fees)
              .cfm;
    } catch (FilingError err) {
      return Result.err(err);
    }

    FeesCalculationQueryMessageType query =
        prep(new FeesCalculationQueryMessageType(), info.getCourtLocation());
    query.setCoreFilingMessage(cfm);
    FeesCalculationResponseMessageType resp = filingPort.get().getFeesCalculation(query);

    Response httpResponse = Ecf4Helper.makeResponse(resp, () -> Response.ok(resp).build());
    return Result.ok(httpResponse);
  }

  private Optional<CourtLocationInfo> getCourtInfo(FilingInformation info) {
    try (CodeDatabase cd = cdSupplier.get()) {
      return cd.getFullLocationInfo(info.getCourtLocation());
    } catch (SQLException ex) {
      log.error("IN EcfEfiler, can't get CodesDB: " + StdLib.strFromException(ex));
      return Optional.empty();
    }
  }

  private List<String> getAllLocations() throws SQLException {
    try (CodeDatabase cd = cdSupplier.get()) {
      return cd.getAllLocations();
    }
  }

  @Override
  public Result<Response, FilingError> getServiceTypes(FilingInformation info, String apiToken) {
    Optional<CourtLocationInfo> court = getCourtInfo(info);
    if (court.isEmpty()) {
      return Result.ok(Response.status(404).entity("No court " + info.getCourtLocation()).build());
    }
    if (!court.get().hasconditionalservicetypes) {
      return Result.ok(
          Response.status(400)
              .entity(
                  "Court "
                      + info.getCourtLocation()
                      + " doesn't have conditional service types. Check the code list instead")
              .build());
    }

    FailFastCollector collector = new FailFastCollector();
    Optional<FilingReviewMDEPort> filingPort = setupFilingPort(apiToken);
    Optional<CourtRecordMDEPort> recordPort = setupRecordPort(apiToken);
    if (filingPort.isEmpty() || recordPort.isEmpty()) {
      FilingError err =
          FilingError.serverError("Couldn't create SOAP port object with token: " + apiToken);
      return Result.err(err);
    }
    CoreFilingMessageType cfm;
    try {
      cfm =
          prepareFiling(
                  info, collector, apiToken, filingPort.get(), recordPort.get(), QueryType.Review)
              .cfm;
    } catch (FilingError err) {
      return Result.err(err);
    }

    ServiceTypesRequestMessageType query =
        prep(new ServiceTypesRequestMessageType(), info.getCourtLocation());
    query.setCoreFilingMessage(cfm);
    ServiceTypesResponseMessageType resp = filingPort.get().getServiceTypes(query);
    return Result.ok(
        Ecf4Helper.mapTylerCodesToHttp(
            resp.getError(), () -> Response.ok(resp.getServiceType()).build()));
  }

  @Override
  public Response getFilingList(
      String courtId,
      String submitterId,
      java.time.LocalDate startDate,
      java.time.LocalDate beforeDate,
      String apiToken) {
    try {
      List<String> courtIds = getAllLocations();
      if (courtId != null && !courtId.equals("0") && !courtIds.contains(courtId)) {
        return Response.status(404).entity("Court " + courtId + " not in jurisdiction").build();
      }
    } catch (SQLException ex) {
      log.error("Couldn't connect to database?" + ex);
      return Response.status(500).entity("Ops Error: Could not connect to database").build();
    }
    log.info(
        "Getting filing list with these params: "
            + courtId
            + ", "
            + submitterId
            + ", "
            + startDate
            + ", "
            + beforeDate
            + ", "
            + apiToken);

    Optional<FilingReviewMDEPort> port = setupFilingPort(apiToken);
    if (port.isEmpty()) {
      return Response.status(403).build();
    }
    FilingListQueryMessageType m = prep(listObjFac.createFilingListQueryMessageType(), courtId);
    if (courtId == null || courtId.equals("0")) {
      // Search all courts
      m.setCaseCourt(null);
    }
    if (submitterId != null && !submitterId.isBlank()) {
      IdentificationType id = niemObjFac.createIdentificationType();
      id.setIdentificationID(Ecf4Helper.convertString(submitterId));
      gov.niem.niem.niem_core._2.PersonType per = niemObjFac.createPersonType();
      per.getPersonOtherIdentification().add(id);
      EntityType entity = niemObjFac.createEntityType();
      entity.setEntityRepresentation(niemObjFac.createEntityPerson(per));
      m.setDocumentSubmitter(entity);
    } else {
      m.setDocumentSubmitter(null);
    }
    if (startDate != null && beforeDate != null) {
      try {
        GregorianCalendar startCal = new GregorianCalendar();
        startCal.set(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth());
        DatatypeFactory fac = DatatypeFactory.newInstance();
        Date actualStart = proxyObjFac.createDate();
        actualStart.setValue(fac.newXMLGregorianCalendar(startCal));
        DateType niemStart = niemObjFac.createDateType();
        niemStart.setDateRepresentation(niemObjFac.createDate(actualStart));

        // endCal is exclusive!
        GregorianCalendar endCal = new GregorianCalendar();
        endCal.set(beforeDate.getYear(), beforeDate.getMonthValue(), beforeDate.getDayOfMonth());
        Date actualEnd = proxyObjFac.createDate();
        actualEnd.setValue(fac.newXMLGregorianCalendar(endCal));
        DateType niemEnd = niemObjFac.createDateType();
        niemEnd.setDateRepresentation(niemObjFac.createDate(actualEnd));

        DateRangeType range = niemObjFac.createDateRangeType();
        range.setStartDate(niemStart);
        range.setEndDate(niemEnd);
        m.getDateRange().add(range);
      } catch (DatatypeConfigurationException ex) {
        log.error("Why is datatypeconfigurationexception checked?: " + ex);
        return Response.status(500).build();
      }
    }
    log.info(
        "Final query: " + Ecf4Helper.objectToXmlStrOrError(m, FilingListQueryMessageType.class));
    FilingListResponseMessageType resp = port.get().getFilingList(m);
    for (MatchingFilingType match : resp.getMatchingFiling()) {
      log.trace("Matched: " + match.getCaseTrackingID() + ", " + match);
    }
    return Ecf4Helper.mapTylerCodesToHttp(
        resp.getError(),
        () -> {
          if (resp.getMatchingFiling().size() <= 0) {
            return Response.noContent().build();
          }
          return Response.ok().entity(resp.getMatchingFiling()).build();
        });
  }

  @Override
  public Response getFilingStatus(String courtId, String filingId, String apiToken) {
    try {
      List<String> courtIds = getAllLocations();
      if (!courtIds.contains(courtId)) {
        return Response.status(404).entity("Court " + courtId + " not in jurisdiction").build();
      }
    } catch (SQLException ex) {
      return Response.status(500).entity("Ops Error: Could not connect to database").build();
    }

    Optional<FilingReviewMDEPort> port = setupFilingPort(apiToken);
    if (port.isEmpty()) {
      return Response.status(403).build();
    }
    FilingStatusQueryMessageType status =
        prep(statusObjFac.createFilingStatusQueryMessageType(), courtId);
    status.setDocumentIdentification(Ecf4Helper.convertId(filingId));
    FilingStatusResponseMessageType statusResp = port.get().getFilingStatus(status);

    return Ecf4Helper.mapTylerCodesToHttp(
        statusResp.getError(), () -> Response.ok().entity(statusResp).build());
  }

  @Override
  public Response getFilingService(
      String courtId, String filingId, String contactId, String apiToken) {
    Optional<FilingReviewMDEPort> port = setupFilingPort(apiToken);
    if (port.isEmpty()) {
      return Response.status(403).build();
    }
    FilingServiceQueryMessageType req = new FilingServiceQueryMessageType();
    ServiceContactIdentificationType id = new ServiceContactIdentificationType();
    id.setIdentificationCategory(
        niemObjFac.createIdentificationCategoryText(Ecf4Helper.convertText("SERVICECONTACTID")));
    id.setIdentificationID(Ecf4Helper.convertString(contactId));
    req.setServiceContactIdentification(id);
    FilingServiceResponseMessageType resp = port.get().getFilingService(req);
    return Ecf4Helper.mapTylerCodesToHttp(resp.getError(), () -> Response.ok(resp).build());
  }

  @Override
  public Response getFilingDetails(String courtId, String filingId, String apiToken) {
    try {
      List<String> courtIds = getAllLocations();
      if (!courtIds.contains(courtId)) {
        return Response.status(422).entity("Court " + courtId + " not in jurisdiction").build();
      }
    } catch (SQLException ex) {
      return Response.status(500).entity("Ops Error: Could not connect to database").build();
    }

    Optional<FilingReviewMDEPort> port = setupFilingPort(apiToken);
    if (port.isEmpty()) {
      return Response.status(403).build();
    }
    FilingDetailQueryMessageType m =
        prep(detailObjFac.createFilingDetailQueryMessageType(), courtId);
    m.setDocumentIdentification(Ecf4Helper.convertId(filingId));
    FilingDetailResponseMessageType resp = port.get().getFilingDetails(m);
    return Ecf4Helper.mapTylerCodesToHttp(
        resp.getError(), () -> Response.ok().entity(resp).build());
  }

  @Override
  public Response getPolicy(String courtId, String apiToken) {
    try {
      List<String> courtIds = getAllLocations();
      if (!courtIds.contains(courtId)) {
        return Response.status(422).entity("Court " + courtId + " not in jurisdiction").build();
      }
    } catch (SQLException ex) {
      return Response.status(500).entity("Couldn't access database: " + ex).build();
    }
    Optional<FilingReviewMDEPort> port = setupFilingPort(apiToken);
    if (port.isEmpty()) {
      return Response.status(403).build();
    }

    CourtPolicyResponseMessageType resp = policyCacher.getPolicyFor(port.get(), courtId);
    return Ecf4Helper.mapTylerCodesToHttp(resp.getError(), () -> Response.ok(resp).build());
  }

  @Override
  public Response cancelFiling(String courtId, String filingId, String apiToken) {
    try {
      List<String> courtIds = getAllLocations();
      if (!courtIds.contains(courtId)) {
        return Response.status(422).entity("Court " + courtId + " not in jurisdiction").build();
      }

      Optional<FilingReviewMDEPort> port = setupFilingPort(apiToken);
      if (port.isEmpty()) {
        return Response.status(403).build();
      }
      CancelFilingMessageType cancel = prep(cancelObjFac.createCancelFilingMessageType(), courtId);
      cancel.setDocumentIdentification(Ecf4Helper.convertId(filingId));
      CancelFilingResponseMessageType resp = port.get().cancelFiling(cancel);
      return Ecf4Helper.mapTylerCodesToHttp(resp.getError(), () -> Response.noContent().build());
    } catch (SQLException ex) {
      return Response.status(500).entity("Ops Error: Could not connect to database").build();
    }
  }

  @Override
  public Response disclaimers(String courtId) {
    try (CodeDatabase cd = cdSupplier.get()) {
      List<Disclaimer> disclaimers = cd.getDisclaimerRequirements(courtId);
      return Response.status(200).entity(disclaimers).build();
    } catch (SQLException ex) {
      log.error("In disclaimer get, couldn't get codes db: " + StdLib.strFromException(ex));
      return Response.status(500).build();
    }
  }

  @Override
  public String getOrgName() {
    // No real API key we need to save
    return "tyler";
  }

  private static <T extends QueryMessageType> T prep(T newMsg, String courtId) {
    return Ecf4Helper.prep(newMsg, courtId);
  }

  private Optional<FilingReviewMDEPort> setupFilingPort(String apiToken) {
    Optional<TylerUserNamePassword> creds = ServiceHelpers.userCredsFromAuthorization(apiToken);
    if (creds.isEmpty()) {
      return Optional.empty();
    }

    FilingReviewMDEPort port = makeFilingPort();
    Map<String, Object> ctx = ((BindingProvider) port).getRequestContext();
    List<Header> headersList = List.of(creds.get().toHeader());
    ctx.put(Header.HEADER_LIST, headersList);
    return Optional.of(port);
  }

  private Optional<ServiceMDEPort> setupServicePort(String apiToken) {
    Optional<TylerUserNamePassword> creds = ServiceHelpers.userCredsFromAuthorization(apiToken);
    if (creds.isEmpty()) {
      return Optional.empty();
    }

    ServiceMDEPort port = makeServicePort();
    Map<String, Object> ctx = ((BindingProvider) port).getRequestContext();
    List<Header> headersList = List.of(creds.get().toHeader());
    ctx.put(Header.HEADER_LIST, headersList);
    return Optional.of(port);
  }

  private Optional<CourtRecordMDEPort> setupRecordPort(String apiToken) {
    Optional<TylerUserNamePassword> creds = ServiceHelpers.userCredsFromAuthorization(apiToken);
    if (creds.isEmpty()) {
      return Optional.empty();
    }

    CourtRecordMDEPort port = recordFactory.getCourtRecordMDEPort();
    ServiceHelpers.setupServicePort((BindingProvider) port);
    Map<String, Object> ctx = ((BindingProvider) port).getRequestContext();
    List<Header> headersList = List.of(creds.get().toHeader());
    ctx.put(Header.HEADER_LIST, headersList);
    return Optional.of(port);
  }

  private FilingReviewMDEPort makeFilingPort() {
    FilingReviewMDEPort port = filingFactory.getFilingReviewMDEPort();
    ServiceHelpers.setupServicePort((BindingProvider) port);
    return port;
  }

  private ServiceMDEPort makeServicePort() {
    ServiceMDEPort port = serviceFactory.getServiceMDEPort();
    ServiceHelpers.setupServicePort((BindingProvider) port);
    return port;
  }
}

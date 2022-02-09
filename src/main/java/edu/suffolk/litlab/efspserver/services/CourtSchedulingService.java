package edu.suffolk.litlab.efspserver.services;

import static edu.suffolk.litlab.efspserver.docassemble.JsonHelpers.isNull;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.BindingProvider;

import org.apache.cxf.headers.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hubspot.algebra.Result;

import ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.messagewrappers.ReserveCourtDateRequestType;
import ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.messagewrappers.ReturnDateRequestType;
import ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.reservedate.ReserveCourtDateMessageType;
import ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.wsdl.courtschedulingmde.CourtSchedulingMDE;
import ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.wsdl.courtschedulingmde.CourtSchedulingMDE_Service;
import ecfv5.tyler.ecf.v5_0.extensions.common.CourtScheduleType;
import ecfv5.tyler.ecf.v5_0.extensions.reservedateresponse.ReserveDateResponseMessageType;
import ecfv5.tyler.ecf.v5_0.extensions.returndate.ReturnDateMessageType;
import ecfv5.tyler.ecf.v5_0.extensions.returndateresponse.ReturnDateResponseMessageType;
import edu.suffolk.litlab.efspserver.FilingInformation;
import edu.suffolk.litlab.efspserver.PartyId;
import edu.suffolk.litlab.efspserver.Person;
import edu.suffolk.litlab.efspserver.SoapClientChooser;
import edu.suffolk.litlab.efspserver.TylerUserNamePassword;
import edu.suffolk.litlab.efspserver.XmlHelper;
import edu.suffolk.litlab.efspserver.codes.CodeDatabase;
import edu.suffolk.litlab.efspserver.codes.CourtLocationInfo;
import edu.suffolk.litlab.efspserver.db.AtRest;
import edu.suffolk.litlab.efspserver.ecf.ComboCaseCodes;
import edu.suffolk.litlab.efspserver.ecf.EcfCaseTypeFactory;
import edu.suffolk.litlab.efspserver.ecf.EcfCourtSpecificSerializer;
import edu.suffolk.litlab.efspserver.ecf.Ecfv5CaseTypeFactory;
import edu.suffolk.litlab.efspserver.ecf.TylerLogin;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.casequerymessage_4.CaseQueryMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.caseresponsemessage_4.CaseResponseMessageType;
import oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4_0.CourtRecordMDEPort;
import tyler.efm.wsdl.webservicesprofile_implementation_4_0.CourtRecordMDEService;
import ecfv5.gov.niem.release.niem.domains.cbrn._4.MessageContentErrorType;
import ecfv5.gov.niem.release.niem.domains.cbrn._4.MessageErrorType;
import ecfv5.gov.niem.release.niem.domains.cbrn._4.MessageStatusType;
import ecfv5.gov.niem.release.niem.domains.jxdm._6.CourtEventType;
import ecfv5.gov.niem.release.niem.niem_core._4.CaseType;
import ecfv5.gov.niem.release.niem.niem_core._4.DateRangeType;
import ecfv5.gov.niem.release.niem.niem_core._4.DateType;
import ecfv5.gov.niem.release.niem.niem_core._4.IdentificationType;
import ecfv5.gov.niem.release.niem.proxy.xsd._4.Duration;
import ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.ecf.CaseFilingType;
import ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.ecf.ResponseMessageType;

@Path("/scheduling/")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class CourtSchedulingService {

  private static Logger log = LoggerFactory.getLogger(CourtSchedulingService.class);
  private static CourtSchedulingMDE_Service schedFactory = new CourtSchedulingMDE_Service();

  private ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.messagewrappers.ObjectFactory oasisWrapObjFac;
  private ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.reservedate.ObjectFactory reserveDateObjFac;
  private final ecfv5.gov.niem.release.niem.niem_core._4.ObjectFactory niemObjFac;
  private final ecfv5.gov.niem.release.niem.proxy.xsd._4.ObjectFactory proxyObjFac;
  private final Map<String, InterviewToFilingEntityConverter> converterMap;
  private final SecurityHub security;
  private final CodeDatabase cd;
  private final CourtRecordMDEService recordFactory; 

  
  public CourtSchedulingService(Map<String, InterviewToFilingEntityConverter> converterMap, 
      SecurityHub security, CodeDatabase cd, String jurisdiction) {
    this.cd = cd;
    this.security = security;
    this.converterMap = converterMap;
    this.oasisWrapObjFac = new ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.messagewrappers.ObjectFactory();
    this.reserveDateObjFac = new ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.reservedate.ObjectFactory();
    this.niemObjFac = new ecfv5.gov.niem.release.niem.niem_core._4.ObjectFactory();
    this.proxyObjFac = new ecfv5.gov.niem.release.niem.proxy.xsd._4.ObjectFactory();
    Optional<CourtRecordMDEService> maybeCourt = SoapClientChooser.getCourtRecordFactory(jurisdiction);
    if (maybeCourt.isEmpty()) {
      throw new RuntimeException("Cannot find " + jurisdiction + " for court record factory");
    }
    this.recordFactory = maybeCourt.get();
  }

  /**
  @POST
  @Path("/return_date_test123")
  public Response getReturnDate(@Context HttpHeaders httpHeaders) throws SQLException, JAXBException {
    String courtId = "cook:cvd1";
    Optional<CourtSchedulingMDE> maybeServ = setupSchedulingPort(httpHeaders);
    if (maybeServ.isEmpty()) {
      return Response.status(401).build();
    }
    Optional<CourtLocationInfo> locationInfo = cd.getFullLocationInfo(courtId); 
    if (locationInfo.isEmpty()) {
      return Response.status(404).entity("No court: " + courtId).build(); 
    }
    if (!locationInfo.get().allowreturndate) {
      return Response.status(400).entity("Court " + courtId + " doesn't allow handling return dates").build();
    }
    
    ReturnDateRequestType r = oasisWrapObjFac.createReturnDateRequestType();
    ReturnDateMessageType m = new ReturnDateMessageType();
    setupReq(m, courtId);
    CaseType ct = niemObjFac.createCaseType();
    
    var jAug = jxObjFac.createCaseAugmentationType();
    var event = jxObjFac.createCourtEventType();
    var e = oasisObjFac.createCourtEventAugmentationType();
    DateType currentDate = Ecfv5XmlHelper.convertDate(LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()));
    e.setCourtEventEnteredOnDocketDate(currentDate);
    e.setCourtEventTypeCode(Ecfv5XmlHelper.convertText("141709"));
    event.getCourtEventAugmentationPoint().add(oasisObjFac.createCourtEventAugmentation(e));
    jAug.getCaseCourtEvent().add(event);
    ct.getCaseAugmentationPoint().add(jxObjFac.createCaseAugmentation(jAug));

    var ecfAug = oasisObjFac.createCaseAugmentationType();
    ecfAug.getRest().add(oasisObjFac.createCaseCategoryCode(Ecfv5XmlHelper.convertText("7"))); 
    ecfAug.getRest().add(oasisObjFac.createCaseTypeCode(Ecfv5XmlHelper.convertNormalized("63655")));
    EntityType ent = niemObjFac.createEntityType();
    PersonAugmentationType pat = oasisObjFac.createPersonAugmentationType();
    pat.getCaseParticipantRoleCode().add(Ecfv5XmlHelper.convertText("9837"));
    pat.setParticipantID(Ecfv5XmlHelper.convertId("SRL"));
    PersonType perType = niemObjFac.createPersonType();
    perType.getPersonAugmentationPoint().add(oasisObjFac.createPersonAugmentation(pat));
    ent.setEntityRepresentation(niemObjFac.createEntityPerson(perType));
    ent.setId("Party1");
    ecfAug.getRest().add(oasisObjFac.createCaseParty(ent)); 
    ct.getCaseAugmentationPoint().add(oasisObjFac.createCaseAugmentation(ecfAug));
    
    var tylerAug = tylerObjFac.createCaseAugmentationType();
    IdentificationType idType = niemObjFac.createIdentificationType();
    idType.setIdentificationID(Ecfv5XmlHelper.convertString("99500")); 
    idType.setIdentificationSourceText(Ecfv5XmlHelper.convertText("87374")); 
    tylerAug.getCrossReferenceNumber().add(idType);
    ct.getCaseAugmentationPoint().add(tylerObjFac.createCaseAugmentation(tylerAug));

    var oasisAug = oasisCivilObjFac.createCaseAugmentationType();
    AmountType at = niemObjFac.createAmountType();
    Decimal d = proxyObjFac.createDecimal();
    d.setValue(new BigDecimal(0.00));
    at.setAmount(d); 
    oasisAug.setAmountInControversy(at);
    oasisAug.setJuryDemandIndicator(Ecfv5XmlHelper.convertBool(false));
    ct.getCaseAugmentationPoint().add(oasisCivilObjFac.createCaseAugmentation(oasisAug));
    m.setCase(ct);
    LocalDate returnDate = LocalDate.of(2021, 11, 17);
    m.setReturnDate(Ecfv5XmlHelper.convertDate(returnDate));
    m.setOutOfStateIndicator(Ecfv5XmlHelper.convertBool(false));
    r.setReturnDateMessage(m);

    log.info("Full msg: " + XmlHelper.objectToXmlStrOrError(r, ReturnDateRequestType.class));
    ReturnDateResponseMessageType resp = maybeServ.get().getReturnDate(r).getReturnDateResponseMessage();
    log.info("Full resp: " + XmlHelper.objectToXmlStrOrError(resp, ReturnDateResponseMessageType.class));
    return Response.ok(resp).build();
  }
  */
  
  @POST
  @Path("/courts/{court_id}/return_date")
  public Response getReturnDate(@Context HttpHeaders httpHeaders, 
      @PathParam("court_id") String courtId, String allVars) throws SQLException, JAXBException {
    Optional<CourtSchedulingMDE> maybeServ = setupSchedulingPort(httpHeaders);
    if (maybeServ.isEmpty()) {
      return Response.status(401).build();
    }
    Optional<CourtLocationInfo> locationInfo = cd.getFullLocationInfo(courtId); 
    if (locationInfo.isEmpty()) {
      return Response.status(404).entity("No court: " + courtId).build(); 
    }
    if (!locationInfo.get().allowreturndate) {
      return Response.status(400).entity("Court " + courtId + " doesn't allow handling return dates").build();
    }
    
    MediaType mediaType = httpHeaders.getMediaType();
    if (mediaType == null) {
      mediaType = MediaType.valueOf("application/json");
    }
    InfoCollector collector = new FailFastCollector();
    Result<FilingInformation, FilingError> res = converterMap.get(mediaType.toString()).traverseInterview(allVars, collector);
    if (res.isErr()) {
      return Response.status(400).entity(collector.jsonSummary()).build();
    }
    FilingInformation info = res.unwrapOrElseThrow();
    info.setCourtLocation(courtId);
    if (info.getFilings().isEmpty()) {
      return Response.status(400).entity("Need to have a filing to calculate a request date").build();
    }
    try {
    EcfCourtSpecificSerializer serializer = new EcfCourtSpecificSerializer(cd, locationInfo.get());
    boolean isInitialFiling = info.getPreviousCaseId().isEmpty() && info.getCaseDocketNumber().isEmpty();
    boolean isFirstIndexedFiling = info.getPreviousCaseId().isEmpty(); 
    ComboCaseCodes allCodes;
    Optional<Map<PartyId, Person>> existingParties = Optional.empty();
    if (!isFirstIndexedFiling) {
      Optional<CourtRecordMDEPort> recordPort = setupRecordPort(httpHeaders);
      if (recordPort.isEmpty()) {
        return Response.status(500).entity("Can't make connection to retrieve court records for subsequent case").build();
      }
      CaseQueryMessageType query = new CaseQueryMessageType();
      ServiceHelpers.prep(query, info.getCourtLocation());
      query.setCaseTrackingID(XmlHelper.convertString(info.getPreviousCaseId().get()));
      query.setCaseQueryCriteria(EcfCaseTypeFactory.getCriteria());
      CaseResponseMessageType resp = recordPort.get().getCase(query);
      String catCode = resp.getCase().getValue().getCaseCategoryText().getValue();
      String typeCode = EcfCaseTypeFactory.getCaseAugmentation(resp.getCase().getValue()).get().getCaseTypeText().getValue(); 
      existingParties = EcfCaseTypeFactory.getCaseParticipants(resp.getCase().getValue());
      if (existingParties.isEmpty()) {
        log.info("Couldn't get exsting parties?");
        throw FilingError.serverError("Couldn't get existing parties for case " + info.getPreviousCaseId().get());
      }
      List<Optional<String>> maybeFilingCodes = info.getFilings().stream().map(f -> f.getFilingCode()).collect(Collectors.toList()); 
      if (maybeFilingCodes.stream().anyMatch(fc -> fc.isEmpty())) {
        InterviewVariable filingVar = collector.requestVar("court_bundle[i].tyler_filing_type", "What filing type is this?", "text"); 
        collector.addRequired(filingVar);
      }
      List<String> filingCodeStrs = maybeFilingCodes.stream().map(fc -> fc.orElse("")).collect(Collectors.toList());
      Map<String, Person> newPartyMap = Stream.concat(info.getNewPlaintiffs().stream(), info.getNewDefendants().stream())
          .collect(Collectors.toMap(per -> per.getIdString(), per -> per));
      Map<String, Person> existingPartyMap = existingParties.get().entrySet().stream().collect(Collectors.toMap(ent -> ent.getKey().id, ent -> ent.getValue()));
      log.info("Existing cat, type, and filings: " + catCode + "," + typeCode + "," + filingCodeStrs);
      allCodes = serializer.serializeCaseCodesIndexed(catCode, typeCode, filingCodeStrs, newPartyMap, existingPartyMap, collector);
    } else {
      allCodes = serializer.serializeCaseCodes(info, collector, isInitialFiling);
    }
    Optional<LocalDate> returnDate = info.getReturnDate();
    if (returnDate.isEmpty()) {
      return Response.status(400).entity("Need return_date").build();
    }
    
    ReturnDateMessageType m = new ReturnDateMessageType();
    setupReq(m, courtId);
    Ecfv5CaseTypeFactory caseTypeFac = new Ecfv5CaseTypeFactory();
    Result<CaseType, FilingError> ctRes = caseTypeFac.createCaseType(info, allCodes, cd, serializer,
        collector, existingParties);

    if (ctRes.isErr()) {
      return Response.status(400).entity(ctRes.unwrapErrOrElseThrow().toJson()).build();
    }

    CaseType ct = ctRes.unwrapOrElseThrow();
    m.setCase(ct);
    m.setReturnDate(Ecfv5XmlHelper.convertDate(returnDate.get()));

    boolean outOfState = false; 
    if (info.getMiscInfo().has("out_of_state")) {
      outOfState = info.getMiscInfo().get("out_of_state").asBoolean(false);
    }
    m.setOutOfStateIndicator(Ecfv5XmlHelper.convertBool(outOfState));
    ReturnDateRequestType r = oasisWrapObjFac.createReturnDateRequestType();
    r.setReturnDateMessage(m);
    log.info("Full msg: " + XmlHelper.objectToXmlStrOrError(r, ReturnDateRequestType.class));
    ReturnDateResponseMessageType resp = maybeServ.get().getReturnDate(r).getReturnDateResponseMessage();
    log.info("Full resp: " + XmlHelper.objectToXmlStrOrError(resp, ReturnDateResponseMessageType.class));
    for (MessageContentErrorType err : resp.getMessageStatus().getMessageContentError()) {
      if (err.getErrorDescription().getErrorCodeText().getValue().equals("344")) {
        for (var aug : ct.getCaseAugmentationPoint()) {
          if (aug.getValue() instanceof ecfv5.tyler.ecf.v5_0.extensions.common.CaseAugmentationType tylerAug) {
            tylerAug.getCrossReferenceNumber().clear();
          }
        }
        m.setCase(ct);
        r.setReturnDateMessage(m);
        log.info("New full msg: " + XmlHelper.objectToXmlStrOrError(r, ReturnDateRequestType.class));
        resp = maybeServ.get().getReturnDate(r).getReturnDateResponseMessage();
        log.info("New full resp: " + XmlHelper.objectToXmlStrOrError(resp, ReturnDateResponseMessageType.class));
      }
    }
    // TODO(brycew:) have gotten "451: AmountInControversy is not supported" for Handling error, should
    // able to automatically not do the amount in those cases.
    MessageErrorType err = resp.getMessageStatus().getMessageHandlingError();
      if (err.getErrorCodeText().getValue().equals("344")) {
        for (var aug : ct.getCaseAugmentationPoint()) {
          if (aug.getValue() instanceof ecfv5.tyler.ecf.v5_0.extensions.common.CaseAugmentationType tylerAug) {
            tylerAug.getCrossReferenceNumber().clear();
          }
        }
        m.setCase(ct);
        r.setReturnDateMessage(m);
        log.info("New full msg: " + XmlHelper.objectToXmlStrOrError(r, ReturnDateRequestType.class));
        resp = maybeServ.get().getReturnDate(r).getReturnDateResponseMessage();
        log.info("New full resp: " + XmlHelper.objectToXmlStrOrError(resp, ReturnDateResponseMessageType.class));
      }
    if (hasError(resp)) {
      return Response.status(400).entity(resp.getMessageStatus()).build();
    }
    if (resp.getReturnDate() != null) {
      return Response.ok(resp.getReturnDate().getDateRepresentation().getValue()).build();
    } else {
      return Response.status(502).entity("No actual return dates given!").build();
    }
    } catch (FilingError err) {
      return Response.status(422).entity(collector.jsonSummary()).build();
    }
  }
  
  @POST
  @Path("/courts/{court_id}/reserve_date")
  public Response reserveCourtDateSync(@Context HttpHeaders httpHeaders,
      @PathParam("court_id") String courtId, String paramStr) throws JsonMappingException, JsonProcessingException, DatatypeConfigurationException {
    log.info("AllParams: " + paramStr);
    JsonMapper mapper = new JsonMapper();
    JsonNode params = mapper.readTree(paramStr); 
    
    Optional<CourtLocationInfo> locationInfo = cd.getFullLocationInfo(courtId); 
    if (locationInfo.isEmpty()) {
      return Response.status(404).entity("No court: " + courtId).build(); 
    }
    if (!locationInfo.get().allowhearing) {
      return Response.status(400).entity("Court " + courtId + " doesn't allow handling return dates").build();
    }

    ReserveCourtDateMessageType msg = reserveDateObjFac.createReserveCourtDateMessageType();
    setupReq(msg, courtId);
    JsonNode docJson = params.get("doc_id");
    if (isNull(docJson) || !docJson.isTextual()) {
      return Response.status(400).entity("Need to pass doc_id").build();
    }
    IdentificationType idType = niemObjFac.createIdentificationType();
    idType.setIdentificationID(Ecfv5XmlHelper.convertString(docJson.asText()));
    msg.getDocumentIdentification().add(idType);
    
    JsonNode estDurJson = params.get("estimated_duration");
    JsonNode afterJson = params.get("range_after");
    JsonNode beforeJson = params.get("range_before");
    if (isNull(afterJson) && isNull(estDurJson) && isNull(beforeJson)) {
      // Ok! Continue without a duration / date
    } else if (isNull(afterJson) || isNull(estDurJson) || isNull(beforeJson)) {
      return Response.status(400).entity("Need to pass estimated_duration, range_start, and range_end").build();
    } else {
      DateRangeType drt = niemObjFac.createDateRangeType();
      String startDateTime = afterJson.asText();
      drt.setStartDate(Ecfv5XmlHelper.convertCourtReserveDate(OffsetDateTime.parse(startDateTime), 1)); 
      String endDateTime = beforeJson.asText();
      drt.setEndDate(Ecfv5XmlHelper.convertCourtReserveDate(OffsetDateTime.parse(endDateTime), 1)); 
      msg.getPotentialStartTimeRange().add(drt);

      int estDurInSeconds = estDurJson.asInt();
      Duration dur = proxyObjFac.createDuration();
      DatatypeFactory df = DatatypeFactory.newInstance();
      int cappedSeconds = estDurInSeconds % 60;
      int cappedMinutes = (estDurInSeconds / 60) % 60;
      // TODO(brycew): can court sessions last days?
      int cappedHours = (estDurInSeconds / 60 / 60) % 60;
      javax.xml.datatype.Duration tmpDur = df.newDuration(true, 0, 0, 0, cappedHours, cappedMinutes, cappedSeconds); 
      dur.setValue(tmpDur); 
      msg.setEstimatedDuration(dur);
    }

    Optional<CourtSchedulingMDE> maybeServ = setupSchedulingPort(httpHeaders);
    if (maybeServ.isEmpty()) {
      return Response.status(401).build();
    }
    ReserveCourtDateRequestType req = oasisWrapObjFac.createReserveCourtDateRequestType();
    req.setReserveCourtDateMessage(msg);
    log.info("full REQ: " + XmlHelper.objectToXmlStrOrError(req, ReserveCourtDateRequestType.class));
    ReserveDateResponseMessageType resp = maybeServ.get().reserveCourtDateSync(req).getReserveDateResponseMessage();
    log.info("Full resp: " + XmlHelper.objectToXmlStrOrError(resp, ReserveDateResponseMessageType.class));
    if (hasError(resp)) {
      return Response.status(400).entity(resp.getMessageStatus()).build();
    }
    // get all the jxdm augs and get the courtEvents (just the first one?)
    Stream<List<CourtEventType>> augEvent = resp.getCase().getCaseAugmentationPoint().stream().filter(aug -> {
      return aug.getValue() instanceof ecfv5.gov.niem.release.niem.domains.jxdm._6.CaseAugmentationType;
    }).map(aug -> ((ecfv5.gov.niem.release.niem.domains.jxdm._6.CaseAugmentationType) aug.getValue()).getCaseCourtEvent());
    List<CourtScheduleType> ret = new ArrayList<CourtScheduleType>();
    augEvent.forEach(events -> {
      for (CourtEventType event : events) {
        Stream<List<CourtScheduleType>> bb = event.getCourtEventAugmentationPoint().stream().filter(p -> {
          // get the tylerCourtEventAugmentation
          return p.getValue() instanceof ecfv5.tyler.ecf.v5_0.extensions.common.CourtEventAugmentationType;
          // make a list of each CourtSchedule
        }).map(p -> ((ecfv5.tyler.ecf.v5_0.extensions.common.CourtEventAugmentationType) p.getValue()).getCourtSchedule());
        bb.forEach(schedList -> ret.addAll(schedList));
      }
    });
    return Response.ok(ret).build();
  }
    
  private Optional<CourtSchedulingMDE> setupSchedulingPort(HttpHeaders httpHeaders) {
    String apiKey = httpHeaders.getHeaderString("X-API-KEY");
    Optional<AtRest> atRest = security.getAtRestInfo(apiKey);
    if (atRest.isEmpty()) {
      log.warn("Couldn't checkLogin");
      return Optional.empty();
    }
    String tylerToken = httpHeaders.getHeaderString(TylerLogin.getHeaderKeyStatic()); 
    Optional<TylerUserNamePassword> creds = ServiceHelpers.userCredsFromAuthorization(tylerToken);
    if (creds.isEmpty()) {
      log.warn("No creds?");
      return Optional.empty();
    }
    CourtSchedulingMDE serv = schedFactory.getCourtSchedulingMDEPort();
    ServiceHelpers.setupServicePort((BindingProvider) serv);
    Map<String, Object> ctx = ((BindingProvider) serv).getRequestContext();
    try {
      List<Header> headersList = List.of(creds.get().toHeader());
      ctx.put(Header.HEADER_LIST, headersList);
    } catch (JAXBException ex) {
      log.warn(ex.toString());
      return Optional.empty();
    }
    return Optional.of(serv);
  }

  private Optional<CourtRecordMDEPort> setupRecordPort(HttpHeaders httpHeaders) {
    String tylerToken = httpHeaders.getHeaderString(TylerLogin.getHeaderKeyStatic()); 

    Optional<TylerUserNamePassword> creds = ServiceHelpers.userCredsFromAuthorization(tylerToken);
    if (creds.isEmpty()) {
      return Optional.empty();
    }

    CourtRecordMDEPort port = recordFactory.getCourtRecordMDEPort();
    ServiceHelpers.setupServicePort((BindingProvider) port);
    Map<String, Object> ctx = ((BindingProvider) port).getRequestContext();
    try {
      List<Header> headersList = List.of(creds.get().toHeader());
      ctx.put(Header.HEADER_LIST, headersList);
    } catch (JAXBException ex) {
      log.warn(ex.toString());
      return Optional.empty();
    }

    return Optional.of(port);
  }
  
  private static void setupReq(CaseFilingType cft, String courtId) {
    DateType currentDate = Ecfv5XmlHelper.convertDateTime(Instant.now(), 0); 
    cft.setDocumentPostDate(currentDate);
    cft.setCaseCourt(Ecfv5XmlHelper.convertCourtType(courtId));
    cft.setServiceInteractionProfileCode(Ecfv5XmlHelper.convertNormalized(ServiceHelpers.MDE_PROFILE_CODE_5));
    cft.setSendingMDELocationID(Ecfv5XmlHelper.convertId(ServiceHelpers.SERVICE_URL));
  }
  
  private static boolean hasError(ResponseMessageType rt) {
    MessageStatusType ms = rt.getMessageStatus();
    String errorCodeText = ms.getMessageHandlingError().getErrorCodeText().getValue();
    log.info("Ms: " + ms);
    log.info("error code: " + errorCodeText);
    return !ms.getMessageContentError().isEmpty() || (!errorCodeText.isBlank() && !errorCodeText.equals("0"));
  }
  
}

package edu.suffolk.litlab.efspserver;

import edu.suffolk.litlab.efspserver.codes.CodeDatabase;
import gov.niem.niem.domains.jxdm._4.CourtType;
import gov.niem.niem.niem_core._2.EntityType;
import gov.niem.niem.niem_core._2.IdentificationType;
import gov.niem.niem.niem_core._2.ObjectFactory;
import gov.niem.niem.niem_core._2.PersonType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.stream.XMLStreamException;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.courtpolicyquerymessage_4.CourtPolicyQueryMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.courtpolicyresponsemessage_4.CourtCodelistType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.courtpolicyresponsemessage_4.CourtPolicyResponseMessageType;
import oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4_0.FilingReviewMDEPort;
import org.apache.cxf.headers.Header;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tyler.efm.services.EfmUserService;
import tyler.efm.services.IEfmUserService;
import tyler.efm.services.schema.authenticaterequest.AuthenticateRequestType;
import tyler.efm.services.schema.authenticateresponse.AuthenticateResponseType;
import tyler.efm.wsdl.webservicesprofile_implementation_4_0.FilingReviewMDEService;

public class CodeUpdater {
  private static Logger log =
      LoggerFactory.getLogger(CodeUpdater.class);

  public static final Map<String, String> ncToTableName = new HashMap<String, String>();

  static {
    // TODO(brycew): there's little more info on these mappings and how they combine
    // besides:
    // The name of an ECF element to be substituted by a court-specific codelist or
    // extension
    // Is this actually an XPath? Should figure it out
    ncToTableName.put("nc:CaseCategoryText", "casecategory");
    ncToTableName.put("tyler:CaseTypeText", "casetype");
    ncToTableName.put("nc:LocationCountryName", "country");
    ncToTableName.put("tyler:DamageAmountCode", "damageamount");
    ncToTableName.put("tyler:DisclaimerRequirementCode", "disclaimerrequirement");
    ncToTableName.put("nc:BinaryFormatStandardName", "documenttype");
    ncToTableName.put("tyler:FilerTypeText", "filertype");
    ncToTableName.put("j:RegisterActionDescriptionText", "filing");
    ncToTableName.put("nc:BinaryCategoryText", "filingcomponent");
    ncToTableName.put("tyler:DocumentOptionalService", "optionalservices");
    ncToTableName.put("ecf:CaseParticipantRoleCode", "partytype");
    ncToTableName.put("tyler:RemedyCode", "procedureremedy");
    ncToTableName.put("nc:LocationStateName", "state");
    ncToTableName.put("ecf:FilingStatusCode", "filingstatus");
    ncToTableName.put("nc:PersonNameSuffixText", "namesuffix");
    ncToTableName.put("tyler:DataFieldConfigCode", "datafield");
    ncToTableName.put("ecf:ServiceRecipientID/nc:IdentificationSourceText", "servicetype");
    ncToTableName.put("nc:BinaryLocationURI", "filetype");
    ncToTableName.put("j:/ArrestCharge/j:ArrestLocation/nc:LocationName", "arrestlocation");
    ncToTableName.put("cext:BondTypeText", "bond");
    ncToTableName.put("cext:Charge/cext:ChargeStatute/j:StatuteLevelText", "degree");
    ncToTableName.put(
        "j:ArrestOfficial/j:EnforcementOfficialUnit/nc:OrganizationIdentification/nc:IdentificationID",
        "lawenforcementunit");
    ncToTableName.put("nc:DocumentIdentification/nc:IdentificationSourceText", "crossreference");
    ncToTableName.put("cext:GeneralOffenseText", "generaloffense");
    ncToTableName.put("cext:StatuteTypeText", "statutetype");
    ncToTableName.put(
        "ecf:PersonDriverLicense/nc:DriverLicenseIdentification/nc:IdentificationCategoryText",
        "driverlicensetype");
    ncToTableName.put(
        "cext:Charge/cext:ChargeStatute/j:StatuteCodeIdentification/nc:IdentificationID",
        "statute");
    ncToTableName.put("nc:PrimaryLanguage\\nc:LanguageCode", "language");
    ncToTableName.put("nc:PersonPhysicalFeature\\nc:PhysicalFeatureCategoryCode",
        "physicalfeature");
    ncToTableName.put("nc:PersonHairColorCode", "haircolor");
    ncToTableName.put("nc:PersonEyeColorCode", "eyecolor");
    ncToTableName.put("nc:PersonEthnicityText", "ethnicity");
    ncToTableName.put("nc:PersonRaceText", "race");
    ncToTableName.put("tyler:CaseSubTypeText", "casesubtype");
    ncToTableName.put("cext:Charge\\cext:ChargePhaseText", "chargephase");
    ncToTableName.put("tyler:VehicleTypeCode", "vehicletype");
    ncToTableName.put("nc:VehicleMakeCode", "vehiclemake");
    ncToTableName.put("nc:VehicleColorPrimaryCode", "vehiclecolor");
    ncToTableName.put("tyler:MotionTypeCode", "motiontype");
    ncToTableName.put("j:Citation\\nc:ActivityIdentification\\tyler:JurisdictionCode",
        "citationjurisdiction");
    ncToTableName.put("tyler:QuestionCode", "question");
    ncToTableName.put("tyler:AnswerCode", "answer");
  }

  /**
   * https://stackoverflow.com/a/1485730/11416267
   *
   * @return
   * @throws IOException
   */
  public InputStream getHtml(String urlToRead, String authHeader) throws IOException {
    URL url = new URL(urlToRead);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("tyl-efm-api", authHeader);
    return conn.getInputStream();
  }

  private Duration downloads = Duration.ZERO;
  private Duration updates = Duration.ZERO;

  private boolean downloadAndReadZip(String url, String signedTime, String tableName,
      String location, CodeDatabase cd) throws JAXBException, SQLException, XMLStreamException {
    Instant startTable = Instant.now(Clock.systemUTC());
    try (InputStream urlStream = getHtml(url, signedTime)) {
      // Write out the zip file
      String zipName = tableName + "_" + location + ".zip";
      FileOutputStream fileOut = new FileOutputStream(zipName);
      long length = urlStream.transferTo(fileOut);
      log.info(length + " bytes transfered for " + zipName);
      downloads = downloads.plus(Duration.between(startTable, Instant.now(Clock.systemUTC())));
      ZipFile zip = new ZipFile(zipName);
      ZipEntry entry = zip.entries().nextElement();

      Instant updateTableLoc = Instant.now(Clock.systemUTC());
      cd.updateTable(tableName, location, zip.getInputStream(entry));
      updates = updates.plus(Duration.between(updateTableLoc, Instant.now(Clock.systemUTC())));
      zip.close();
      // Delete the zip file after processing: no need to have it hanging around.
      File f = new File(zipName);
      f.delete();
      return true;
    } catch (IOException ex) {
      // Some system codes (everything but "country", "state", "filingstatus", "datafield",
      // and "servicetype") are expected to 500. Not really sure why they give us bad URLs.
      log.warn("Skipping " + url + ", got exception downloading zip: " + ex.toString());
      return false;
    }
  }

  private boolean downloadSystemTables(String baseUrl, CodeDatabase cd, HeaderSigner signer)
      throws SQLException, OperatorCreationException, GeneralSecurityException, CMSException,
      IOException, JAXBException, XMLStreamException {
    Map<String, String> codeUrls = Map.of("version", "/CodeService/codes/version/", "location",
        "/CodeService/codes/location/",
        // NOTE: the Tyler docs say this is available from `GetPolicy'. That is wrong.
        "error", "/CodeService/codes/error");
    // TODO(brycew): can tell if court codes will differ if they have a Row in Version codes
    // where the simple value differs from the "0" Row. Need to check both

    Savepoint sp = cd.setSavePoint("systemTables");
    cd.dropTables(codeUrls.entrySet().stream().map((e) -> e.getKey()).collect(Collectors.toList()));

    // On first download, there won't be installed versions of things yet.
    cd.createTableIfAbsent("installedversion");

    Optional<String> signedTime = signer.signedCurrentTime();
    if (signedTime.isEmpty()) {
      cd.rollback(sp);
      return false;
      
    }
    for (Map.Entry<String, String> urlSuffix : codeUrls.entrySet()) {
      boolean updateSuccess = false;
      try {
        updateSuccess = downloadAndReadZip(baseUrl + urlSuffix.getValue(), signedTime.get(),
            urlSuffix.getKey(), "", cd);
      } catch (SQLException ex) {
        updateSuccess = false;
      }
      if (!updateSuccess) {
        cd.rollback(sp);
        return false;
      }
    }
    cd.commit();
    return true;
  }

  /**
   * 
   * @param location
   * @param codes If empty, all versions will be downloaded
   * @param cd
   * @param signer
   * @param conn
   */
  private void downloadCourtTables(String location, Optional<List<String>> codes, CodeDatabase cd,
      HeaderSigner signer, FilingReviewMDEPort filingPort)
      throws JAXBException, OperatorCreationException, IOException, SQLException,
      XMLStreamException, GeneralSecurityException, CMSException {
    log.info("Location: " + location);
    // final Instant startLoc = Instant.now(Clock.systemUTC());
    CourtPolicyQueryMessageType m = new CourtPolicyQueryMessageType();
    IdentificationType courtId = XmlHelper.convertId(location);
    ObjectFactory of = new ObjectFactory();
    JAXBElement<IdentificationType> elem = of.createOrganizationIdentification(courtId);
    CourtType court = new CourtType();
    court.setOrganizationIdentification(elem);
    m.setCaseCourt(court);
    // TODO(brycew): change this stuff
    m.setSendingMDELocationID(XmlHelper.convertId("https://filingassemblymde.com"));
    m.setSendingMDEProfileCode(
        "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:WebServicesMessaging-2.0");
    JAXBElement<PersonType> elem2 = of.createEntityPerson(new PersonType());
    EntityType typ = new EntityType();
    typ.setEntityRepresentation(elem2);
    m.setQuerySubmitter(typ);
    CourtPolicyResponseMessageType p = filingPort.getPolicy(m);
    XmlHelper.objectToXmlFile(p, CourtPolicyResponseMessageType.class, 
        new File("full_court_obj_" + location + ".xml"));

    // TODO(brycew): use the version codes to check the checksums of each table,
    // then see if we need to update
    for (CourtCodelistType ccl : p.getRuntimePolicyParameters().getCourtCodelist()) {
      String ecfElem = ccl.getECFElementName().getValue();
      if (ncToTableName.containsKey(ecfElem)) {
        String tableName = ncToTableName.get(ecfElem);
        if (codes.isEmpty() || codes.get().contains(tableName)) {
          // TODO(brycew): check that the effective date is later than today
          // JAXBElement<?> obj = ccl.getEffectiveDate().getDateRepresentation();

          // Tyler gives us URLs w/ spaces, which aren't valid. This makes them valid
          String url = ccl.getCourtCodelistURI().getIdentificationID().getValue().replace(" ",
              "%20");
          Optional<String> signedTime = signer.signedCurrentTime();
          if (signedTime.isEmpty()) {
            log.error("Couldn't get signed time to download codeds, skipping all");
            return;
          }
          downloadAndReadZip(url, signedTime.get(), tableName, location, cd);
        }
      } else {
        log.error(ecfElem + " not in the nc map!");
      }
    }
    cd.commit();
    log.info("Downloads took: " + downloads + ", and updates took: " + updates);
  }

  public void updateAll(String baseUrl, FilingReviewMDEPort filingPort, CodeDatabase cd)
      throws SQLException, OperatorCreationException, GeneralSecurityException, CMSException,
      IOException, JAXBException, XMLStreamException {
    Savepoint sp = cd.setSavePoint("mysavepoint");
    HeaderSigner signer = new HeaderSigner(
        System.getenv("PATH_TO_KEYSTORE"), System.getenv("X509_PASSWORD"));
    if (!downloadSystemTables(baseUrl, cd, signer)) {
      log.warn("System tables didn't update, but we needed them "
          + " to actually figure out new versions");
      return;
    }

    // Drop each of tables that need to be updated
    Map<String, List<String>> versionsToUpdate = cd.getVersionsToUpdate();
    for (Entry<String, List<String>> courtAndTables : versionsToUpdate.entrySet()) {
      String courtLocation = courtAndTables.getKey();
      List<String> tables = courtAndTables.getValue();
      log.info("Updating court " + courtLocation);
      for (String table : tables) {
        log.info("Refreshing table " + table + " for court " + courtLocation);
        if (!cd.deleteFromTable(table, courtLocation)) {
          log.warn("Couldn't delete from " + table + " at " + courtLocation + ", aborting");
          cd.rollback(sp);
          return;
        }
      }
      downloadCourtTables(courtLocation, Optional.of(tables), cd, signer, filingPort);
    }
    cd.commit();
  }

  public void downloadAll(String baseUrl, FilingReviewMDEPort filingPort, CodeDatabase cd)
      throws SQLException, OperatorCreationException, PropertyException, GeneralSecurityException,
      CMSException, IOException, JAXBException, XMLStreamException {
    HeaderSigner signer = new HeaderSigner(
        System.getenv("PATH_TO_KEYSTORE"), System.getenv("X509_PASSWORD"));
    log.info("Downloading system tables");
    downloadSystemTables(baseUrl, cd, signer);

    List<String> tablesToDrop = ncToTableName.entrySet().stream().map((e) -> e.getValue())
        .collect(Collectors.toUnmodifiableList());
    cd.dropTables(tablesToDrop);

    downloads = Duration.ZERO;
    updates = Duration.ZERO;
    List<String> locs = cd.getAllLocations();
    for (String location : locs) {
      log.info("Downloading tables for " + location);
      downloadCourtTables(location, Optional.empty(), cd, signer, filingPort);
    }
    log.info("Downloads took: " + downloads + ", and updates took: " + updates);
    cd.commit();
  }
  
  public static void downloadAll(String codesSite, CodeDatabase cd) throws JAXBException, OperatorCreationException, 
      SQLException, GeneralSecurityException, CMSException, IOException, XMLStreamException {
    ClientCallbackHandler.setX509Password(System.getenv("X509_PASSWORD"));
    AuthenticateRequestType authReq = new AuthenticateRequestType();
    authReq.setEmail("bwilley@suffolk.edu");
    authReq.setPassword(System.getenv("BRYCE_USER_PASSWORD"));
    URL userWsdlUrl = EfmUserService.WSDL_LOCATION;
    IEfmUserService userPort = EfmClient.makeUserService(userWsdlUrl);
    AuthenticateResponseType authRes = userPort.authenticateUser(authReq);
    log.info("Auth'd?: " + authRes.getError().getErrorText());
    List<Header> headersList = TylerUserNamePassword.makeHeaderList(authRes);
    FilingReviewMDEPort filingPort = EfmClient
        .makeFilingService(FilingReviewMDEService.WSDL_LOCATION, headersList);
    CodeUpdater cu = new CodeUpdater();
    cu.downloadAll(codesSite, filingPort, cd);
  }

  public static void main(String[] args) throws Exception {
    CodeDatabase cd = new CodeDatabase(System.getenv("POSTGRES_URL"), 
        Integer.parseInt(System.getenv("POSTGRES_PORT")), 
        System.getenv("POSTGRES_CODES_DB"));
    cd.createDbConnection(System.getenv("POSTGRES_USER"), System.getenv("POSTGRES_PASSWORD"));
  }
}

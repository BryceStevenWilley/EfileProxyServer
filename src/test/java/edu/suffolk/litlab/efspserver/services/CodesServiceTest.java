package edu.suffolk.litlab.efspserver.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;

import edu.suffolk.litlab.efspserver.codes.CodeDatabase;
import edu.suffolk.litlab.efspserver.db.DatabaseCreator;
import edu.suffolk.litlab.efspserver.ecf.EcfCodesService;

public class CodesServiceTest {
  private static Logger log = LoggerFactory.getLogger(CodesServiceTest.class);
  
  @Container
  public PostgreSQLContainer<?> postgres =
    new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.2"));

  private static final String ENDPOINT_ADDRESS = "http://localhost:9090";
  private Server server;
  private CodeDatabase cd;
  
  private void startServer() throws Exception {
    DataSource ds = DatabaseCreator.makeDataSource(postgres.getJdbcUrl(), postgres.getDatabaseName(), postgres.getUsername(), postgres.getPassword(), 2, 100);
    cd = new CodeDatabase("illinois", "stage", ds.getConnection());
    cd.createTablesIfAbsent();
    Set<String> tables = Set.of(
        "casecategory", 
        "casesubtype", 
        "casetype", 
        "filingstatus",
        "servicetype", 
        "optionalservices"); 
    cd.createTableIfAbsent("location");
    cd.updateTable("location", "0", this.getClass().getResourceAsStream("/0_location_test.xml"));
    for (String table: tables) {
      cd.createTableIfAbsent(table);
      String filename = "/" + "adams" + "_" + table + "_test.xml";
      log.info("Updating from file: " + filename);
      cd.updateTable(table, "adams", this.getClass().getResourceAsStream(filename)); 
    }

    JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
    sf.setResourceClasses(EcfCodesService.class);
    sf.setResourceProvider(EcfCodesService.class, new SingletonResourceProvider(new EcfCodesService("illinois", "stage", ds)));
    sf.setAddress(ENDPOINT_ADDRESS);
    Map<Object, Object> extensionMappings = Map.of(
        "xml", MediaType.APPLICATION_XML,
        "json", MediaType.APPLICATION_JSON);
    sf.setExtensionMappings(extensionMappings);
    List<Object> providers = List.of(
        new JAXBElementProvider<Object>(),
        new JacksonJsonProvider());
    sf.setProviders(providers);
    server = sf.create();
  }
  
  @BeforeEach
  void init() throws Exception {
    postgres.start();
    startServer();
  }
  
  @AfterEach
  public void destroy() throws Exception {
    server.stop();
    server.destroy();
    cd.close();
    postgres.stop();
  }
  
  @SuppressWarnings("static-method")
  private String getServerResponseAt(String path) {
    WebClient client = WebClient.create(ENDPOINT_ADDRESS);
    client.accept("application/json");
    client.path(path);
    Response resp = client.get();
    log.info("resp: " + resp);
    return resp.readEntity(String.class);
  }
  
  @Test
  public void testGetAll() throws JsonMappingException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(getServerResponseAt("/"));
    assertTrue(node.has("getCodesUnderCourt"), "didn't have codes post court: " + node);
    assertTrue(node.has("getCourtLocationCodes"), "didn't have court location codes: " + node);
    assertTrue(node.has("getCaseTypes"));
    assertTrue(node.has("getCaseSubtypes"));
    assertTrue(node.has("getPartyTypes"));
  }

  @Test
  public void testGetSubCourt() throws JsonMappingException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(getServerResponseAt("/courts/adams"));
    assertTrue(node.has("getCourtLocationCodes"), "didn't have court location codes: " + node);
    assertEquals(
        ServiceHelpers.EXTERNAL_URL + "/jurisdictions/illinois/codes/courts/adams/codes",
        node.get("getCourtLocationCodes").get("url").asText());
    assertTrue(node.has("getCaseTypes"));
    assertEquals(
        ServiceHelpers.EXTERNAL_URL + "/jurisdictions/illinois/codes/courts/adams/case_types",
        node.get("getCaseTypes").get("url").asText());
  }

  @Test
  public void testGetCase() throws JsonMappingException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(getServerResponseAt("/courts/adams/case_types/25361"));
    assertTrue(node.has("getCaseSubtypes"), "didn't have case sub types: " + node);
    assertEquals(
        ServiceHelpers.EXTERNAL_URL + "/jurisdictions/illinois/codes/courts/adams/case_types/25361/case_subtypes",
        node.get("getCaseSubtypes").get("url").asText());
    assertTrue(node.has("getPartyTypes"));
    assertEquals(
        ServiceHelpers.EXTERNAL_URL + "/jurisdictions/illinois/codes/courts/adams/case_types/25361/party_types",
        node.get("getPartyTypes").get("url").asText());
  }

  /**
   * Was hacky and now we don't let people go to sub resources of things that don't exist.
  @Test
  public void testGetUnderFilingCode() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(getServerResponseAt("/courts/adams/filing_types/1234"));
    assertTrue(node.has("getOptionalServices"), "didn't have optionalServices: " + node);
    assertEquals(
        ServiceHelpers.EXTERNAL_URL + "/jurisdictions/illinois/codes/courts/adams/filing_types/1234/optional_services",
          node.get("getOptionalServices").get("url").asText());
    assertTrue(node.has("getFilingComponents"));
    assertEquals(
        ServiceHelpers.EXTERNAL_URL + "/jurisdictions/illinois/codes/courts/adams/filing_types/1234/filing_components",
        node.get("getFilingComponents").get("url").asText());
  }
  */

}

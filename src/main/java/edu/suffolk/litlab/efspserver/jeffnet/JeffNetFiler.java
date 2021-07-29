package edu.suffolk.litlab.efspserver.jeffnet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hubspot.algebra.Result;
import edu.suffolk.litlab.efspserver.ContactInformation;
import edu.suffolk.litlab.efspserver.FilingDoc;
import edu.suffolk.litlab.efspserver.FilingInformation;
import edu.suffolk.litlab.efspserver.Name;
import edu.suffolk.litlab.efspserver.Person;
import edu.suffolk.litlab.efspserver.services.EfmFilingInterface;
import edu.suffolk.litlab.efspserver.services.FilingError;
import edu.suffolk.litlab.efspserver.services.InfoCollector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeffNetFiler implements EfmFilingInterface {
  private static Logger log =
      LoggerFactory.getLogger(JeffNetFiler.class);

  private URI filingEndpoint;
  private SimpleModule module;
  
  /** Constructor that takes the URL endpoint of JeffNet to call */
  public JeffNetFiler(String filingEndpoint) throws URISyntaxException {
    this.filingEndpoint = new URI(filingEndpoint);
    
    this.module = new SimpleModule();
    module.addSerializer(new FilingInformationJeffNetSerializer(FilingInformation.class));
    module.addSerializer(
        new ContactInfoJeffNetJacksonSerializer(ContactInformation.class));
    module.addSerializer(new NameJeffNetJacksonSerializer(Name.class));
    module.addSerializer(new FilingJeffNetJacksonSerializer(FilingDoc.class));
    module.addSerializer(new PersonJeffNetJacksonSerializer(Person.class));
  }
  
  @Override
  public String getOrgName() {
    return "jeffnet";
  }
  
  @Override
  public Result<List<UUID>, FilingError> sendFiling(FilingInformation info, String apiToken) {
    if (info.getFilings().isEmpty()) {
      return Result.err(FilingError.serverError("Error: cannot file with no filings"));
    }
    
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(this.module);
    mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    
    try {
      String finalStr = mapper.writeValueAsString(info);
      log.debug("Final Json object: " + finalStr);
    
      log.info("Sending to " + this.filingEndpoint);
      HttpClient client = HttpClient.newBuilder().build();
      HttpRequest request = HttpRequest.newBuilder()
          .POST(HttpRequest.BodyPublishers.ofString(finalStr))
          .header("X-API-KEY", apiToken)
          .header("Content-type", "application/json")
          .header("Accept", "application/json")
          .uri(this.filingEndpoint)
          .build();
      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      log.info("Got response code: " + response.body() + " " + response.statusCode());
      if (response.statusCode() != 200) {
        return Result.err(FilingError.serverError(response.statusCode() + " " + response.body()));
      }
      ApiResult result = mapper.readValue(response.body(), ApiResult.class);
      UUID transactionId = UUID.fromString(result.transactionId);
      // TODO(brycew): Break this into multiple: https://trello.com/c/QZaUFT2c/38
      return Result.ok(List.of(transactionId)); 
    } catch (InterruptedException ex) {
      return Result.err(FilingError.serverError("Interrupted getting response from " + this.filingEndpoint + ", " + ex));
    } catch (JsonMappingException ex) {
      return Result.err(FilingError.serverError("Couldn't understand JeffNet's response b/c: " + ex));
    } catch (IOException ex) {
      return Result.err(FilingError.serverError("Exception connecting to " + this.filingEndpoint + ", " + ex));
    }
  }

  @Override
  public List<String> getCourts() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void checkFiling(FilingInformation info, InfoCollector collector) {
    // TODO(brycew): check the filing here too 
    return;
  }

  @Override
  public Response getFilingDetails(String courtId, String filingId, String apiToken) {
    // TODO Auto-generated method stub
    return Response.status(500).build();
  }

  @Override
  public Response cancelFiling(String courtId, String filingId, String apiToken) {
    // TODO Auto-generated method stub
    return Response.status(500).build();
  }

  
  @Override
  public Response getFilingList(String courtId, String apiToken) {
    // TODO Auto-generated method stub
    return Response.status(500).build();
  }

  @Override
  public Response getFilingStatus(String courtId, String filingId, String apiToken) {
    // TODO Auto-generated method stub
    return Response.status(500).build();
  }

  private static class ApiResult {
    @JsonProperty("ResultCode")
    int resultCode;
    
    @JsonProperty("Message")
    String message;
    
    @JsonProperty("TransactionID")
    String transactionId;
  }

}

package edu.suffolk.litlab.efspserver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.suffolk.litlab.efspserver.db.NewTokens;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Path("/authenticate")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationService {

  private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

  private final SecurityHub security;

  public AuthenticationService(SecurityHub security) {
    this.security = security;
  }

  @POST
  @Operation(summary="Log in the user to all of the requested e-filing EFMs",
    description="Passes in a ",
    responses = {
      @ApiResponse(description= "The logged-in tokens"),
      @ApiResponse(responseCode="403", description="login information not valid")
    })
  public Response authenticateUser(@Context HttpHeaders httpHeaders, String loginInfo) {
    MDC.put(MDCWrappers.OPERATION, "AuthenticationService.authenticateUser");
    ObjectMapper mapper = new ObjectMapper();
    String apiKey;
    try {
      JsonNode node = mapper.readTree(loginInfo);
      if (!node.isObject() || !node.has("api_key") || !node.get("api_key").isTextual()) {
        log.error("Call didn't pass in an api_key in the auth call");
        return Response.status(401).build();
      }
      apiKey = node.get("api_key").asText();
    } catch (JsonProcessingException e) {
      log.error(e.toString());
      return Response.status(401).build();
    }
    if (apiKey == null || apiKey.isBlank()) {
      log.error("Call passed in a null / blank api_key");
      return Response.status(401).build();
    }
    log.info("Invoking User Auth for an apiKey");
    Optional<NewTokens> activeToken = security.login(apiKey, loginInfo);
    MDCWrappers.removeAllMDCs();
    return activeToken
        .map((toks) -> Response.ok(toks).build())
        .orElse(Response.status(403).build());
  }
}

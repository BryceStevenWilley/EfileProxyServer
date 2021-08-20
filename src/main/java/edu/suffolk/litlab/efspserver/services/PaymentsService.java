package edu.suffolk.litlab.efspserver.services;

import static edu.suffolk.litlab.efspserver.services.ServiceHelpers.makeResponse;

import java.util.Optional;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.suffolk.litlab.efspserver.SecurityHub;
import tyler.efm.services.IEfmFirmService;
import tyler.efm.services.schema.baseresponse.BaseResponseType;
import tyler.efm.services.schema.common.PaymentAccountType;
import tyler.efm.services.schema.getpaymentaccountrequest.GetPaymentAccountRequestType;
import tyler.efm.services.schema.getpaymentaccountresponse.GetPaymentAccountResponseType;
import tyler.efm.services.schema.paymentaccountlistresponse.PaymentAccountListResponseType;
import tyler.efm.services.schema.paymentaccounttypelistresponse.PaymentAccountTypeListResponseType;
import tyler.efm.services.schema.removepaymentaccountrequest.RemovePaymentAccountRequestType;
import tyler.efm.services.schema.updatepaymentaccountrequest.UpdatePaymentAccountRequestType;
import tyler.efm.services.schema.updatepaymentaccountresponse.UpdatePaymentAccountResponseType;

@Path("/payments/")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class PaymentsService {

  private tyler.efm.services.schema.common.ObjectFactory tylerCommonObjFac = 
      new tyler.efm.services.schema.common.ObjectFactory();
   
  @Context
  UriInfo uri;

  private SecurityHub security;
  
  public PaymentsService(SecurityHub security) {
    this.security = security;
  }

  @GET
  @Path("/global-accounts")
  public Response getGlobalPaymentList(@Context HttpHeaders httpHeaders) {
    Optional<IEfmFirmService> firmPort = ServiceHelpers.setupFirmPort(httpHeaders, security);
    if (firmPort.isEmpty()) {
      return Response.status(403).build();
    }
    
    PaymentAccountListResponseType accounts = firmPort.get().getGlobalPaymentAccountList();
    return makeResponse(accounts, () -> Response.ok(accounts.getPaymentAccount()).build());
  }
  
  @GET
  @Path("/global-accounts/{account_id}")
  public Response getGlobalPaymentAccount(@Context HttpHeaders httpHeaders,
      @PathParam("account_id") String accountId) {
    Optional<IEfmFirmService> firmPort = ServiceHelpers.setupFirmPort(httpHeaders, security);
    if (firmPort.isEmpty()) {
      return Response.status(403).build();
    }
    
    GetPaymentAccountRequestType req = new GetPaymentAccountRequestType();
    req.setPaymentAccountID(accountId);
    GetPaymentAccountResponseType account = firmPort.get().getGlobalPaymentAccount(req);
    return makeResponse(account, () -> Response.ok(account.getPaymentAccount()).build());
  }
  

  // TODO(#25): Can't write this until we have TOGA integration
  /*
  @PUT
  @Path("/global-accounts")
  public Response createPaymentAccount(@Context HttpHeaders httpHeaders,
      String json) {
    
    
  }
  */
  
  @PATCH
  @Path("/global-accounts/{account_id}")
  public Response updateGlobalPaymentAccount(@Context HttpHeaders httpHeaders,
      @PathParam("account_id") String accountId,
      String json) throws JsonMappingException, JsonProcessingException {

    Optional<IEfmFirmService> firmPort = ServiceHelpers.setupFirmPort(httpHeaders, security);
    if (firmPort.isEmpty()) {
      return Response.status(403).build();
    }
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(json);
    
    GetPaymentAccountRequestType query = new GetPaymentAccountRequestType();
    query.setPaymentAccountID(accountId);
    GetPaymentAccountResponseType existingResp = firmPort.get().getGlobalPaymentAccount(query);
    if (ServiceHelpers.hasError(existingResp)) {
      return Response.status(400).entity(existingResp.getError()).build();
    }

    UpdatePaymentAccountRequestType update = new UpdatePaymentAccountRequestType();
    update.setPaymentAccount(updateAccountType(existingResp.getPaymentAccount(), node));
    UpdatePaymentAccountResponseType resp = firmPort.get().updateGlobalPaymentAccount(update);
    return makeResponse(resp, () -> Response.ok().build());
  }
  
  @DELETE
  @Path("/global-accounts/{account_id}")
  public Response removeGlobalPaymentAccount(@Context HttpHeaders httpHeaders,
      @PathParam("account_id") String accountId) {
    Optional<IEfmFirmService> firmPort = ServiceHelpers.setupFirmPort(httpHeaders, security);
    if (firmPort.isEmpty()) {
      return Response.status(403).build();
    }
    
    RemovePaymentAccountRequestType req = new RemovePaymentAccountRequestType();
    req.setPaymentAccountID(accountId);
    BaseResponseType resp = firmPort.get().removeGlobalPaymentAccount(req);
    return makeResponse(resp, () -> Response.ok().build());
  }
  
  @GET
  @Path("/payment-accounts")
  public Response getPaymentAccountList(@Context HttpHeaders httpHeaders) {
    Optional<IEfmFirmService> firmPort = ServiceHelpers.setupFirmPort(httpHeaders, security);
    if (firmPort.isEmpty()) {
      return Response.status(403).build();
    }
    
    PaymentAccountListResponseType list = firmPort.get().getPaymentAccountList();
    return makeResponse(list, () -> Response.ok(list.getPaymentAccount()).build());
  }
  
  @GET
  @Path("/payment-accounts/{account_id}")
  public Response getPaymentAccount(@Context HttpHeaders httpHeaders, 
      @PathParam("account_id") String accountId) {
    Optional<IEfmFirmService> firmPort = ServiceHelpers.setupFirmPort(httpHeaders, security);
    if (firmPort.isEmpty()) {
      return Response.status(403).build();
    }
    
    GetPaymentAccountRequestType req = new GetPaymentAccountRequestType();
    req.setPaymentAccountID(accountId);
    GetPaymentAccountResponseType resp = firmPort.get().getPaymentAccount(req);

    return makeResponse(resp, () -> Response.ok(resp.getPaymentAccount()).build());
  }
  
  @DELETE
  @Path("/payment-accounts/{account_id}")
  public Response removePaymentAccount(@Context HttpHeaders httpHeaders,
      @PathParam("account_id") String accountId) {
    Optional<IEfmFirmService> firmPort = ServiceHelpers.setupFirmPort(httpHeaders, security);
    if (firmPort.isEmpty()) {
      return Response.status(403).build();
    }
    
    RemovePaymentAccountRequestType req = new RemovePaymentAccountRequestType();
    req.setPaymentAccountID(accountId);
    BaseResponseType resp = firmPort.get().removePaymentAccount(req);
    return makeResponse(resp, () -> Response.ok().build());
  }
  
  // TODO(#25): can't write this until we have TOGA integration
  /*
  @POST
  @Path("/payment-accounts")
  public Response createPaymentAccount(@Context HttpHeaders httpHeaders,
      String jsonAccount) {
    Optional<IEfmFirmService> firmPort = ServiceHelpers.setupFirmPort(httpHeaders, security);
    if (firmPort.isEmpty()) {
      return Response.status(403).build();
    }
    
    ObjectMapper mapper = new ObjectMapper();
    

    CreatePaymentAccountRequestType createAccount = new CreatePaymentAccountRequestType();
    createAccount.setPaymentAccount(newAccount);
    CreatePaymentAccountResponseType resp = firmPort.get().createPaymentAccount(createAccount);
    if (hasError(resp)) {
      return Response.status(400).entity(resp.getError()).build();
    }
    
    URI newUri = uri.getBaseUriBuilder().path("/payment-account/" + resp.getPaymentAccountID()).build();
    return Response.created(newUri).build();
  }
  */
  
  @PATCH
  @Path("/payment-accounts/{account_id}")
  public Response updatePaymentAccount(@Context HttpHeaders httpHeaders,
      @PathParam("account_id") String accountId,
      String json) throws JsonMappingException, JsonProcessingException {
    Optional<IEfmFirmService> firmPort = ServiceHelpers.setupFirmPort(httpHeaders, security);
    if (firmPort.isEmpty()) {
      return Response.status(403).build();
    }
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(json);
    
    GetPaymentAccountRequestType query = new GetPaymentAccountRequestType();
    query.setPaymentAccountID(accountId);
    GetPaymentAccountResponseType existingResp = firmPort.get().getPaymentAccount(query);
    if (ServiceHelpers.hasError(existingResp)) {
      return Response.status(400).entity(existingResp.getError()).build();
    }

    UpdatePaymentAccountRequestType update = new UpdatePaymentAccountRequestType();
    update.setPaymentAccount(updateAccountType(existingResp.getPaymentAccount(), node));
    UpdatePaymentAccountResponseType resp = firmPort.get().updatePaymentAccount(update);
    return makeResponse(resp, () -> Response.ok().build());
  }

  @GET
  @Path("/types")
  public Response getPaymentAccountTypeList(@Context HttpHeaders httpHeaders) {
    Optional<IEfmFirmService> firmPort = ServiceHelpers.setupFirmPort(httpHeaders, security);
    if (firmPort.isEmpty()) {
      return Response.status(403).build();
    }
    
    PaymentAccountTypeListResponseType types = firmPort.get().getPaymentAccountTypeList();
    return makeResponse(types, () -> Response.ok(types.getPaymentAccountType()).build());
  }
  
  /** Fluid interface, but modifies the input. */
  private PaymentAccountType updateAccountType(PaymentAccountType existingAccount,
      JsonNode updateInfo) {
    JsonNode active = updateInfo.get("active");
    if (active != null && active.isBoolean()) {
      existingAccount.setActive(tylerCommonObjFac.createPaymentAccountTypeActive(active.asBoolean()));
    }
    
    JsonNode name = updateInfo.get("account_name");
    if (name != null && name.isTextual()) {
      existingAccount.setAccountName(name.asText());
    }
    
    return existingAccount;
  }
  
}

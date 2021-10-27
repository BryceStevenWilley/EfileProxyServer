package edu.suffolk.litlab.efspserver.ecf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.suffolk.litlab.efspserver.XmlHelper;
import edu.suffolk.litlab.efspserver.codes.CodeDatabase;
import edu.suffolk.litlab.efspserver.codes.NameAndCode;
import edu.suffolk.litlab.efspserver.db.Transaction;
import edu.suffolk.litlab.efspserver.db.UserDatabase;
import edu.suffolk.litlab.efspserver.services.OrgMessageSender;
import edu.suffolk.litlab.efspserver.services.ServiceHelpers;
import gov.niem.niem.niem_core._2.IdentificationType;
import gov.niem.niem.niem_core._2.TextType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AllowanceChargeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CardAccountType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.DocumentRenditionType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ErrorType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.FilingStatusType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ReviewedDocumentType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.messagereceiptmessage_4.MessageReceiptMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.messagereceiptmessage_4.ObjectFactory;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.paymentmessage_4.PaymentMessageType;
import oasis.names.tc.legalxml_courtfiling.schema.xsd.reviewfilingcallbackmessage_4.ReviewFilingCallbackMessageType;
import oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4.NotifyFilingReviewCompleteRequestMessageType;
import oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4_0.FilingAssemblyMDEPort;
import tyler.ecf.extensions.eventcallbackmessage.EventCallbackMessageType;
import tyler.ecf.extensions.servicecallbackmessage.ServiceCallbackMessageType;

@javax.jws.WebService(
    serviceName=  "FilingAssemblyMDEService",
    portName="FilingAssemblyMDEPort",
    targetNamespace="urn:tyler:efm:wsdl:WebServicesProfile-Implementation-4.0",
    wsdlLocation="file:src/main/resources/wsdl/filingreview/ECF-4.0-FilingAssemblyMDEService-mod.wsdl",
    endpointInterface="oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4_0.FilingAssemblyMDEPort")
public class OasisEcfWsCallback implements FilingAssemblyMDEPort {
  private static Logger log =
      LoggerFactory.getLogger(OasisEcfFiler.class);

  private ObjectFactory recepitFac;
  private UserDatabase ud;
  private CodeDatabase cd;
  private OrgMessageSender msgSender;

  public OasisEcfWsCallback(UserDatabase ud, CodeDatabase cd, OrgMessageSender msgSender) {
    recepitFac = new ObjectFactory();
    this.ud = ud;
    this.cd = cd;
    this.msgSender = msgSender;
  }
  
  private static String chargeToStr(AllowanceChargeType charge) {
    StringBuilder chargeReason = new StringBuilder(); 
    String amountText = XmlHelper.amountToString(charge.getAmount());
    chargeReason.append(amountText);
    if (charge.getAllowanceChargeReason() != null) {
      chargeReason.append(" for " ).append(charge.getAllowanceChargeReason().getValue());
    }
    charge.getPaymentMeans().stream().forEach(m -> {
      CardAccountType acct = m.getCardAccount();
      if (acct != null) {
        String cardInfo = "";
        if (acct.getCardTypeCode() != null) {
          cardInfo += acct.getCardTypeCode().getValue();
        }
        if (acct.getPrimaryAccountNumberID() != null) {
          cardInfo += " (" + acct.getPrimaryAccountNumberID().getValue() + ")";
        }
        if (acct.getExpiryDate() != null && acct.getExpiryDate().getValue() != null) {
          cardInfo += " (exp " + acct.getExpiryDate().getValue().toString() + ")";
        }
        chargeReason.append(" paid for using " + cardInfo);
      }
    });
    return chargeReason.toString();
  }
  
  private static String documentToStr(ReviewedDocumentType doc) {
    StringBuilder docText = new StringBuilder();
    if (doc instanceof tyler.ecf.extensions.common.ReviewedDocumentType tylerDoc) {
      if (tylerDoc.getDocumentDescriptionText() != null
          && tylerDoc.getDocumentDescriptionText().getValue() != null
          && tylerDoc.getDocumentDescriptionText().getValue().isBlank()) {
        docText.append("The document (")
                   .append(tylerDoc.getDocumentDescriptionText().getValue())
                   .append(") ");
      } else {
        docText.append("The document ");
      }
      if (tylerDoc.getDocumentBinary() != null
          && tylerDoc.getDocumentBinary().getBinaryDescriptionText() != null) {
        docText.append("with file name ")
                   .append(tylerDoc.getDocumentBinary().getBinaryDescriptionText().getValue());
      } else if (tylerDoc.getDocumentRendition() != null) {
        for (DocumentRenditionType ren: tylerDoc.getDocumentRendition()) {
          if (ren.getDocumentBinary() != null 
              && ren.getDocumentBinary().getBinaryDescriptionText() != null) {
            docText.append(", file name ").append(ren.getDocumentBinary().getBinaryDescriptionText().getValue());
          }
        }
      }
        
      if (tylerDoc.getFilingReviewCommentsText() != null) {
        docText.append(" has the following review comments: ")
                   .append(tylerDoc.getFilingReviewCommentsText().getValue());
      }
        
      if (tylerDoc.getRejectReasonText() != null) {
        docText.append(", was rejected for the following reason: ")
                   .append(tylerDoc.getRejectReasonText().getValue());
      }
    } else {
      docText.append("The review was about the document ");
      for (DocumentRenditionType ren: doc.getDocumentRendition()) {
        if (ren.getDocumentBinary() != null 
            && ren.getDocumentBinary().getBinaryDescriptionText() != null) {
          docText.append(", file name ").append(ren.getDocumentBinary().getBinaryDescriptionText().getValue());
        }
      }
    }
    docText.append('.');
    return docText.toString();
  }
  
  private Map<String, String> reviewedFilingToStr(ReviewFilingCallbackMessageType revFiling,
      Transaction trans) {
    List<NameAndCode> names = cd.getFilingStatuses(trans.courtId);

    StringBuilder messageText = new StringBuilder();
    Map<String, String> statuses = new HashMap<>();
    if (revFiling.getReviewedLeadDocument() != null 
        && revFiling.getReviewedLeadDocument().getValue() != null) {
      ReviewedDocumentType leadDoc = revFiling.getReviewedLeadDocument().getValue();
      messageText.append(documentToStr(leadDoc));
    }
    if (revFiling.getReviewedConnectedDocument() != null) {
      for (var doc : revFiling.getReviewedConnectedDocument()) {
        if (doc != null && doc.getValue() != null) {
          messageText.append(documentToStr(doc.getValue())); 
        }
      }
    }
    FilingStatusType filingStat = revFiling.getFilingStatus();
    if (filingStat != null) {
      messageText.append(' ').append(revFiling.getFilingStatus().getStatusDescriptionText().stream()
                          .reduce("", (des, tt) -> des + ((tt != null) ? tt.getValue() : ""), 
                              (des1, des2) -> des1 + des2));
      
      final String replyCode = filingStat.getFilingStatusCode();
      Optional<String> statusText = names.stream()
          .filter(nac -> nac.getCode().equalsIgnoreCase(replyCode))
          .map(nac -> nac.getName()).findFirst();

      String statusCode = replyCode;
      if (replyCode == null || replyCode.isBlank()) {
        if (filingStat.getStatusText() != null) {
          statusCode = filingStat.getStatusText().getValue();
        } else {
          statusCode = "";
        }
      }
      statuses.put("status", statusText.orElse(statusCode));
    }
    statuses.put("message_text", messageText.toString());
    return statuses;
  }

  public MessageReceiptMessageType notifyFilingReviewComplete(
      NotifyFilingReviewCompleteRequestMessageType msg) {
    log.info("Full NotifyFilingReviewComplete msg" + msg);
    // The bare minimum: get the Document ID, see if we have it in the db, send email response
    PaymentMessageType payment = msg.getPaymentReceiptMessage();
    ReviewFilingCallbackMessageType revFiling = msg.getReviewFilingCallbackMessage();
    MessageReceiptMessageType reply = recepitFac.createMessageReceiptMessageType();
    ServiceHelpers.setupReplys(reply);
    // This shouldn't happen, but I don't trust this XML BS
    if (payment == null || revFiling == null) {
      log.error("Why did Tyler send us a notifyFilingReviewComplete without either a filing"
          + " review or a payment receipt?");
      log.error(msg.toString());
      return error(reply, "705", "NotifyFilingReviewComplete message not found");
    }

    // Handle payment stuff: Address is usually empty, it's all in Payment and AllowanceCharges
    List<String> charges = new ArrayList<>();
    for (AllowanceChargeType charge: payment.getAllowanceCharge()) {
      charges.add(chargeToStr(charge));
    }

    // Now for the review filing
    String filingId = "";
    for (IdentificationType id : revFiling.getDocumentIdentification()) {
      if (id.getIdentificationCategory().getValue() instanceof TextType category) {
        if (category.getValue().equalsIgnoreCase("FILINGID")) {
          filingId = id.getIdentificationID().getValue();
        }
      }
      // TODO(brycew-later): do we need to do anything with the parent envelope?
      // Maybe check them as well? But the filingId should be the same overall, and we'll save
      // most of them.
    }
    if (filingId.isBlank()) {
      log.error("Got back a review filing that has a blank / no FILINGID? " + revFiling.toString());
      return error(reply, "720", "Filing code not found in message");
    }
    try {
      Optional<Transaction> trans = ud.findTransaction(UUID.fromString(filingId));
      if (trans.isEmpty()) {
        log.warn("No transaction on record for filingId: " + filingId + " no one to send to");
        return error(reply, "724", "Filing ID " + filingId + " not found");
      }
      reply.setCaseCourt(XmlHelper.convertCourtType(trans.get().courtId));
      Map<String, String> statuses = reviewedFilingToStr(revFiling, trans.get());
      boolean success = msgSender.sendMessage(trans.get(), statuses);
      if (!success) {
        log.error("Couldn't properly send message to " + trans.get().name + "!");
      }
      return ok(reply);
    } catch (SQLException e) {
      log.error("Couldn't connect to the SQL database to get the transaction: " + e.toString());
      return error(reply, "-1", "Server error");
    }
  }

  @Override
  public MessageReceiptMessageType notifyEvent(EventCallbackMessageType eventCallbackMessage) {
    log.info("Full NotifyEvent msg" + eventCallbackMessage);
    // TODO not going to turn on for now. Someone should implement this and push upstream
    return null;
  }

  @Override
  public MessageReceiptMessageType notifyServiceComplete(
      ServiceCallbackMessageType serviceCallbackMessage) {
    log.info("Full NotifyServiceComplete msg" + serviceCallbackMessage);
    // TODO Auto-generated method stub
    return null;
  }
  
  private static MessageReceiptMessageType ok(MessageReceiptMessageType reply) {
    return error(reply, "0", "No Error");
  }
  
  private static MessageReceiptMessageType error(MessageReceiptMessageType reply, String code, String text) {
    ErrorType err = new ErrorType();
    err.setErrorCode(XmlHelper.convertText(code));
    err.setErrorText(XmlHelper.convertText(text));
    reply.getError().add(err);
    return reply;
  }
}

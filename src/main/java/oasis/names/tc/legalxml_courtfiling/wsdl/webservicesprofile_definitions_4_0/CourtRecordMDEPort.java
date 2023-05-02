package oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4_0;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.5.0-SNAPSHOT-3e40a2387febaf00f47fe893d97ffdf63aaa4626
 * 2021-07-21T17:05:34.939-04:00
 * Generated source version: 3.5.0-SNAPSHOT
 *
 */
@WebService(targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0", name = "CourtRecordMDEPort")
@XmlSeeAlso({tyler.ecf.extensions.filingdetailresponsemessage.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.casequerymessage_4.ObjectFactory.class, gov.niem.niem.iso_4217._2.ObjectFactory.class, tyler.ecf.extensions.eventcallbackmessage.ObjectFactory.class, tyler.ecf.extensions.serviceinformationhistoryresponsemessage.ObjectFactory.class, tyler.ecf.extensions.servicetypesresponsemessage.ObjectFactory.class, gov.niem.niem.proxy.xsd._2.ObjectFactory.class, gov.niem.niem.domains.screening._2.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.corefilingmessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.documentresponsemessage_4.ObjectFactory.class, tyler.ecf.extensions.cancelfilingresponsemessage.ObjectFactory.class, tyler.ecf.extensions.createcasemessage.ObjectFactory.class, gov.niem.niem.usps_states._2.ObjectFactory.class, org.w3._2000._09.xmldsig_.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.messagereceiptmessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.citationcase_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.servicereceiptmessage_4.ObjectFactory.class, gov.niem.niem.structures._2.ObjectFactory.class, tyler.ecf.extensions.filingserviceresponsemessage.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4.ObjectFactory.class, gov.niem.niem.ansi_d20._2.ObjectFactory.class, tyler.ecf.extensions.serviceinformationhistoryquerymessage.ObjectFactory.class, tyler.ecf.extensions.servicetypesrequestmessage.ObjectFactory.class, gov.niem.niem.fips_10_4._2.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.serviceinformationresponsemessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.appellatecase_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.recorddocketingcallbackmessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.caseresponsemessage_4.ObjectFactory.class, tyler.ecf.extensions.filingservicequerymessage.ObjectFactory.class, gov.niem.niem.iso_639_3._2.ObjectFactory.class, oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.caselistresponsemessage_4.ObjectFactory.class, gov.niem.niem.fips_6_4._2.ObjectFactory.class, tyler.ecf.extensions.serviceattachcaselistquerymessage.ObjectFactory.class, gov.niem.niem.fbi._2.ObjectFactory.class, gov.niem.niem.domains.jxdm._4.ObjectFactory.class, tyler.ecf.extensions.serviceattachcaselistresponsemessage.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.courtpolicyquerymessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.feescalculationquerymessage_4.ObjectFactory.class, tyler.ecf.extensions.criminal.ObjectFactory.class, tyler.ecf.extensions.cancelfilingmessage.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.courtpolicyresponsemessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.bankruptcycase_4.ObjectFactory.class, tyler.ecf.extensions.securecasemessage.ObjectFactory.class, un.unece.uncefact.data.specification.corecomponenttypeschemamodule._2.ObjectFactory.class, tyler.ecf.extensions.notifyservicemessage.ObjectFactory.class, tyler.ecf.extensions.createcasecallbackmessage.ObjectFactory.class, gov.niem.niem.ansi_nist._2.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.filingstatusquerymessage_4.ObjectFactory.class, oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.appinfo_4.ObjectFactory.class, tyler.ecf.extensions.servicecallbackmessage.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.documentquerymessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.recorddocketingmessage_4.ObjectFactory.class, gov.niem.niem.appinfo._2.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.criminalcase_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.juvenilecase_4.ObjectFactory.class, oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_2.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.feescalculationresponsemessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.serviceinformationquerymessage_4.ObjectFactory.class, tyler.ecf.extensions.filingdetailquerymessage.ObjectFactory.class, gov.niem.niem.niem_core._2.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.caselistquerymessage_4.ObjectFactory.class, gov.niem.niem.unece_rec20_misc._2.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.filinglistquerymessage_4.ObjectFactory.class, tyler.ecf.extensions.common.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.paymentreceiptmessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.commontypes_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.filingstatusresponsemessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.reviewfilingcallbackmessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.civilcase_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.domesticcase_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.paymentmessage_4.ObjectFactory.class, oasis.names.tc.legalxml_courtfiling.schema.xsd.filinglistresponsemessage_4.ObjectFactory.class, tyler.ecf.extensions.notifyreceiptmessage.ObjectFactory.class, gov.niem.niem.nonauthoritative_code._2.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface CourtRecordMDEPort {

    @WebMethod(operationName = "RecordFiling", action = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0/CourtRecordMDEPort/RecordFilingRequest")
    @WebResult(name = "MessageReceiptMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:MessageReceiptMessage-4.0", partName = "MessageReceiptMessage")
    public oasis.names.tc.legalxml_courtfiling.schema.xsd.messagereceiptmessage_4.MessageReceiptMessageType recordFiling(

        @WebParam(partName = "RecordFilingRequestMessage", name = "RecordFilingRequestMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0")
        oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4.RecordFilingRequestMessageType recordFilingRequestMessage
    );

    @WebMethod(operationName = "NotifyService", action = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0/CourtRecordMDEPort/NotifyServiceRequest")
    @WebResult(name = "MessageReceiptMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:MessageReceiptMessage-4.0", partName = "MessageReceiptMessage")
    public oasis.names.tc.legalxml_courtfiling.schema.xsd.messagereceiptmessage_4.MessageReceiptMessageType notifyService(

        @WebParam(partName = "NotifyServiceMessage", name = "NotifyServiceMessage", targetNamespace = "urn:tyler:ecf:extensions:NotifyServiceMessage")
        tyler.ecf.extensions.notifyservicemessage.NotifyServiceMessageType notifyServiceMessage
    );

    @WebMethod(operationName = "CreateCase", action = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0/CourtRecordMDEPort/CreateCaseRequest")
    @WebResult(name = "MessageReceiptMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:MessageReceiptMessage-4.0", partName = "MessageReceiptMessage")
    public oasis.names.tc.legalxml_courtfiling.schema.xsd.messagereceiptmessage_4.MessageReceiptMessageType createCase(

        @WebParam(partName = "CreateCaseMessage", name = "CreateCaseMessage", targetNamespace = "urn:tyler:ecf:extensions:CreateCaseMessage")
        tyler.ecf.extensions.createcasemessage.CreateCaseMessageType createCaseMessage
    );

    @WebMethod(operationName = "NotifyReceiptComplete", action = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0/CourtRecordMDEPort/NotifyReceiptCompleteRequest")
    @WebResult(name = "MessageReceiptMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:MessageReceiptMessage-4.0", partName = "MessageReceiptMessage")
    public oasis.names.tc.legalxml_courtfiling.schema.xsd.messagereceiptmessage_4.MessageReceiptMessageType notifyReceiptComplete(

        @WebParam(partName = "NotifyReceiptMessage", name = "NotifyReceiptMessage", targetNamespace = "urn:tyler:ecf:extensions:NotifyReceiptMessage")
        tyler.ecf.extensions.notifyreceiptmessage.NotifyReceiptMessageType notifyReceiptMessage
    );

    @WebMethod(operationName = "GetCase", action = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0/CourtRecordMDEPort/GetCaseRequest")
    @WebResult(name = "CaseResponseMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CaseResponseMessage-4.0", partName = "CaseResponseMessage")
    public oasis.names.tc.legalxml_courtfiling.schema.xsd.caseresponsemessage_4.CaseResponseMessageType getCase(

        @WebParam(partName = "CaseQueryMessage", name = "CaseQueryMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CaseQueryMessage-4.0")
        oasis.names.tc.legalxml_courtfiling.schema.xsd.casequerymessage_4.CaseQueryMessageType caseQueryMessage
    );

    @WebMethod(operationName = "GetServiceAttachCaseList", action = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0/CourtRecordMDEPort/GetServiceAttachCaseListRequest")
    @WebResult(name = "ServiceAttachCaseListResponseMessage", targetNamespace = "urn:tyler:ecf:extensions:ServiceAttachCaseListResponseMessage", partName = "ServiceAttachCaseListResponseMessage")
    public tyler.ecf.extensions.serviceattachcaselistresponsemessage.ServiceAttachCaseListResponseMessageType getServiceAttachCaseList(

        @WebParam(partName = "ServiceAttachCaseListQueryMessage", name = "ServiceAttachCaseListQueryMessage", targetNamespace = "urn:tyler:ecf:extensions:ServiceAttachCaseListQueryMessage")
        tyler.ecf.extensions.serviceattachcaselistquerymessage.ServiceAttachCaseListQueryMessageType serviceAttachCaseListQueryMessage
    );

    @WebMethod(operationName = "GetCaseList", action = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0/CourtRecordMDEPort/GetCaseListRequest")
    @WebResult(name = "CaseListResponseMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CaseListResponseMessage-4.0", partName = "CaseListResponseMessage")
    public oasis.names.tc.legalxml_courtfiling.schema.xsd.caselistresponsemessage_4.CaseListResponseMessageType getCaseList(

        @WebParam(partName = "CaseListQueryMessage", name = "CaseListQueryMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CaseListQueryMessage-4.0")
        oasis.names.tc.legalxml_courtfiling.schema.xsd.caselistquerymessage_4.CaseListQueryMessageType caseListQueryMessage
    );

    @WebMethod(operationName = "GetDocument", action = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0/CourtRecordMDEPort/GetDocumentRequest")
    @WebResult(name = "DocumentResponseMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:DocumentResponseMessage-4.0", partName = "DocumentResponseMessage")
    public oasis.names.tc.legalxml_courtfiling.schema.xsd.documentresponsemessage_4.DocumentResponseMessageType getDocument(

        @WebParam(partName = "DocumentQueryMessage", name = "DocumentQueryMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:DocumentQueryMessage-4.0")
        oasis.names.tc.legalxml_courtfiling.schema.xsd.documentquerymessage_4.DocumentQueryMessageType documentQueryMessage
    );

    @WebMethod(operationName = "GetServiceInformation", action = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0/CourtRecordMDEPort/GetServiceInformationRequest")
    @WebResult(name = "ServiceInformationResponseMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:ServiceInformationResponseMessage-4.0", partName = "ServiceInformationResponseMessage")
    public oasis.names.tc.legalxml_courtfiling.schema.xsd.serviceinformationresponsemessage_4.ServiceInformationResponseMessageType getServiceInformation(

        @WebParam(partName = "ServiceInformationQueryMessage", name = "ServiceInformationQueryMessage", targetNamespace = "urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:ServiceInformationQueryMessage-4.0")
        oasis.names.tc.legalxml_courtfiling.schema.xsd.serviceinformationquerymessage_4.ServiceInformationQueryMessageType serviceInformationQueryMessage
    );

    @WebMethod(operationName = "GetServiceInformationHistory", action = "urn:oasis:names:tc:legalxml-courtfiling:wsdl:WebServicesProfile-Definitions-4.0/CourtRecordMDEPort/GetServiceInformationHistoryRequest")
    @WebResult(name = "ServiceInformationHistoryResponseMessage", targetNamespace = "urn:tyler:ecf:extensions:ServiceInformationHistoryResponseMessage", partName = "ServiceInformationHistoryResponseMessage")
    public tyler.ecf.extensions.serviceinformationhistoryresponsemessage.ServiceInformationHistoryResponseMessageType getServiceInformationHistory(

        @WebParam(partName = "ServiceInformationHistoryQueryMessage", name = "ServiceInformationHistoryQueryMessage", targetNamespace = "urn:tyler:ecf:extensions:ServiceInformationHistoryQueryMessage")
        tyler.ecf.extensions.serviceinformationhistoryquerymessage.ServiceInformationHistoryQueryMessageType serviceInformationHistoryQueryMessage
    );
}

package https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0wsdl.courtrecordmde;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceFeature;
import jakarta.xml.ws.Service;

/**
 * This class was generated by Apache CXF 4.0.1
 * 2023-05-31T17:28:32.644-04:00
 * Generated source version: 4.0.1
 *
 */
@WebServiceClient(name = "CourtRecordMDEService",
                  wsdlLocation = "classpath:wsdl/stage/illinois-ecf5-CourtPolicyMDEService.wsdl",
                  targetNamespace = "https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0WSDL/CourtRecordMDE")
public class CourtRecordMDEService extends Service {

    public final static QName SERVICE = new QName("https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0WSDL/CourtRecordMDE", "CourtRecordMDEService");
    public final static QName CourtRecordMDE = new QName("https://docs.oasis-open.org/legalxml-courtfiling/ns/v5.0WSDL/CourtRecordMDE", "CourtRecordMDE");

    public CourtRecordMDEService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public CourtRecordMDEService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CourtRecordMDEService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public CourtRecordMDEService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns CourtRecordMDE
     */
    @WebEndpoint(name = "CourtRecordMDE")
    public CourtRecordMDE getCourtRecordMDE() {
        return super.getPort(CourtRecordMDE, CourtRecordMDE.class);
    }

    /**
     *
     * @param features
     *     A list of {@link jakarta.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CourtRecordMDE
     */
    @WebEndpoint(name = "CourtRecordMDE")
    public CourtRecordMDE getCourtRecordMDE(WebServiceFeature... features) {
        return super.getPort(CourtRecordMDE, CourtRecordMDE.class, features);
    }

}
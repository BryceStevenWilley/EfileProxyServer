package ecfv5.tyler.efm.wsdl.webservicesprofile_implementation_5_0;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import ecfv5.https.docs_oasis_open_org.legalxml_courtfiling.ns.v5_0.wsdl.filingassemblymde.FilingAssemblyMDE;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.5.0-SNAPSHOT-3e40a2387febaf00f47fe893d97ffdf63aaa4626
 * 2021-11-08T17:36:27.201-05:00
 * Generated source version: 3.5.0-SNAPSHOT
 *
 */
@WebServiceClient(name = "FilingAssemblyMDEService",
                  wsdlLocation = "file:FilingAssemblyMDE.wsdl",
                  targetNamespace = "urn:tyler:efm:wsdl:WebServicesProfile-Implementation-5.0")
public class FilingAssemblyMDEService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("urn:tyler:efm:wsdl:WebServicesProfile-Implementation-5.0", "FilingAssemblyMDEService");
    public final static QName FilingAssemblyMDEPort = new QName("urn:tyler:efm:wsdl:WebServicesProfile-Implementation-5.0", "FilingAssemblyMDEPort");
    static {
        URL url = null;
        try {
            url = new URL("file:FilingAssemblyMDE.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(FilingAssemblyMDEService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:FilingAssemblyMDE.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public FilingAssemblyMDEService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public FilingAssemblyMDEService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FilingAssemblyMDEService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public FilingAssemblyMDEService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public FilingAssemblyMDEService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public FilingAssemblyMDEService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns FilingAssemblyMDE
     */
    @WebEndpoint(name = "FilingAssemblyMDEPort")
    public FilingAssemblyMDE getFilingAssemblyMDEPort() {
        return super.getPort(FilingAssemblyMDEPort, FilingAssemblyMDE.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns FilingAssemblyMDE
     */
    @WebEndpoint(name = "FilingAssemblyMDEPort")
    public FilingAssemblyMDE getFilingAssemblyMDEPort(WebServiceFeature... features) {
        return super.getPort(FilingAssemblyMDEPort, FilingAssemblyMDE.class, features);
    }

}

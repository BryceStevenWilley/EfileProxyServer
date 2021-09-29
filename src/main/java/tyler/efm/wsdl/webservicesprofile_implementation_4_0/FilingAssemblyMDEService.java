package tyler.efm.wsdl.webservicesprofile_implementation_4_0;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;
import oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4_0.FilingAssemblyMDEPort;

/**
 * This class was generated by Apache CXF 3.5.0-SNAPSHOT-3e40a2387febaf00f47fe893d97ffdf63aaa4626
 * 2021-07-21T16:03:48.174-04:00
 * Generated source version: 3.5.0-SNAPSHOT
 *
 */
@WebServiceClient(name = "FilingAssemblyMDEService",
                  wsdlLocation = "file:/home/litlab/eclipse-workspace/EfileProxyServer/src/main/resources/wsdl/filingreview/ECF-4.0-FilingAssemblyMDEService.wsdl",
                  targetNamespace = "urn:tyler:efm:wsdl:WebServicesProfile-Implementation-4.0")
public class FilingAssemblyMDEService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("urn:tyler:efm:wsdl:WebServicesProfile-Implementation-4.0", "FilingAssemblyMDEService");
    public final static QName FilingAssemblyMDEPort = new QName("urn:tyler:efm:wsdl:WebServicesProfile-Implementation-4.0", "FilingAssemblyMDEPort");
    static {
        URL url = FilingAssemblyMDEService.class.getClassLoader().getResource("wsdl/filingreview/ECF-4.0-FilingAssemblyMDEService.wsdl");
        if (url == null) {
          org.slf4j.LoggerFactory.getLogger(FilingAssemblyMDEService.class.getName()).info(
             "Can not initialize the default wsdl from {0}", "classpath:wsdl/filingreview/ECF-4.0-FilingAssemblyMDEService.wsdl");
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
     *     returns FilingAssemblyMDEPort
     */
    @WebEndpoint(name = "FilingAssemblyMDEPort")
    public FilingAssemblyMDEPort getFilingAssemblyMDEPort() {
        return super.getPort(FilingAssemblyMDEPort, FilingAssemblyMDEPort.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns FilingAssemblyMDEPort
     */
    @WebEndpoint(name = "FilingAssemblyMDEPort")
    public FilingAssemblyMDEPort getFilingAssemblyMDEPort(WebServiceFeature... features) {
        return super.getPort(FilingAssemblyMDEPort, FilingAssemblyMDEPort.class, features);
    }

}

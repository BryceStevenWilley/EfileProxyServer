package tyler.efm.services;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.5.2
 * 2022-05-04T11:19:19.077-04:00
 * Generated source version: 3.5.2
 *
 */
@WebServiceClient(name = "EfmUserService",
                  wsdlLocation = "classpath:wsdl/EFMUserServiceSingle.svc.wsdl",
                  targetNamespace = "urn:tyler:efm:services")
public class EfmUserService extends Service {

    public final static QName SERVICE = new QName("urn:tyler:efm:services", "EfmUserService");
    public final static QName BasicHttpBindingIEfmUserService = new QName("urn:tyler:efm:services", "BasicHttpBinding_IEfmUserService");
    public final static QName BasicHttpBindingIEfmUserService1 = new QName("urn:tyler:efm:services", "BasicHttpBinding_IEfmUserService1");
    public EfmUserService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public EfmUserService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EfmUserService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public EfmUserService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns IEfmUserService
     */
    @WebEndpoint(name = "BasicHttpBinding_IEfmUserService")
    public IEfmUserService getBasicHttpBindingIEfmUserService() {
        return super.getPort(BasicHttpBindingIEfmUserService, IEfmUserService.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IEfmUserService
     */
    @WebEndpoint(name = "BasicHttpBinding_IEfmUserService")
    public IEfmUserService getBasicHttpBindingIEfmUserService(WebServiceFeature... features) {
        return super.getPort(BasicHttpBindingIEfmUserService, IEfmUserService.class, features);
    }


    /**
     *
     * @return
     *     returns IEfmUserService
     */
    @WebEndpoint(name = "BasicHttpBinding_IEfmUserService1")
    public IEfmUserService getBasicHttpBindingIEfmUserService1() {
        return super.getPort(BasicHttpBindingIEfmUserService1, IEfmUserService.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IEfmUserService
     */
    @WebEndpoint(name = "BasicHttpBinding_IEfmUserService1")
    public IEfmUserService getBasicHttpBindingIEfmUserService1(WebServiceFeature... features) {
        return super.getPort(BasicHttpBindingIEfmUserService1, IEfmUserService.class, features);
    }

}

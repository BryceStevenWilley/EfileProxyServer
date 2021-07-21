
package oasis.names.tc.legalxml_courtfiling.wsdl.webservicesprofile_definitions_4_0;

import javax.xml.ws.Endpoint;

/**
 * This class was generated by Apache CXF 3.5.0-SNAPSHOT-3e40a2387febaf00f47fe893d97ffdf63aaa4626
 * 2021-07-21T14:23:16.391-04:00
 * Generated source version: 3.5.0-SNAPSHOT
 *
 */

public class FilingReviewMDEPort_FilingReviewMDEPort_Server{

    protected FilingReviewMDEPort_FilingReviewMDEPort_Server() throws java.lang.Exception {
        System.out.println("Starting Server");
        Object implementor = new FilingReviewMDEPortImpl();
        String address = "https://Illinois-stage.tylerhost.net/efm/FilingReviewMDEPort.svc";
        Endpoint.publish(address, implementor);
    }

    public static void main(String args[]) throws java.lang.Exception {
        new FilingReviewMDEPort_FilingReviewMDEPort_Server();
        System.out.println("Server ready...");

        Thread.sleep(5 * 60 * 1000);
        System.out.println("Server exiting");
        System.exit(0);
    }
}

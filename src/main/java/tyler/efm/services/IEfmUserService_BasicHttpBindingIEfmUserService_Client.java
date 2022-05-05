
package tyler.efm.services;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;

/**
 * This class was generated by Apache CXF 3.5.2
 * 2022-05-04T11:19:18.977-04:00
 * Generated source version: 3.5.2
 *
 */
public final class IEfmUserService_BasicHttpBindingIEfmUserService_Client {

    private static final QName SERVICE_NAME = new QName("urn:tyler:efm:services", "EfmUserService");

    private IEfmUserService_BasicHttpBindingIEfmUserService_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = EfmUserService.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) {
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        EfmUserService ss = new EfmUserService(wsdlURL, SERVICE_NAME);
        IEfmUserService port = ss.getBasicHttpBindingIEfmUserService();

        {
        System.out.println("Invoking authenticateUser...");
        tyler.efm.services.schema.authenticaterequest.AuthenticateRequestType _authenticateUser_authenticateRequest = null;
        tyler.efm.services.schema.authenticateresponse.AuthenticateResponseType _authenticateUser__return = port.authenticateUser(_authenticateUser_authenticateRequest);
        System.out.println("authenticateUser.result=" + _authenticateUser__return);


        }
        {
        System.out.println("Invoking updateUser...");
        tyler.efm.services.schema.updateuserrequest.UpdateUserRequestType _updateUser_updateUserRequest = null;
        tyler.efm.services.schema.updateuserresponse.UpdateUserResponseType _updateUser__return = port.updateUser(_updateUser_updateUserRequest);
        System.out.println("updateUser.result=" + _updateUser__return);


        }
        {
        System.out.println("Invoking getNotificationPreferences...");
        tyler.efm.services.schema.notificationpreferencesresponse.NotificationPreferencesResponseType _getNotificationPreferences__return = port.getNotificationPreferences();
        System.out.println("getNotificationPreferences.result=" + _getNotificationPreferences__return);


        }
        {
        System.out.println("Invoking getUser...");
        tyler.efm.services.schema.getuserrequest.GetUserRequestType _getUser_getUserRequest = null;
        tyler.efm.services.schema.getuserresponse.GetUserResponseType _getUser__return = port.getUser(_getUser_getUserRequest);
        System.out.println("getUser.result=" + _getUser__return);


        }
        {
        System.out.println("Invoking selfResendActivationEmail...");
        tyler.efm.services.schema.selfresendactivationemailrequest.SelfResendActivationEmailRequestType _selfResendActivationEmail_selfResendActivationEmailRequest = null;
        tyler.efm.services.schema.baseresponse.BaseResponseType _selfResendActivationEmail__return = port.selfResendActivationEmail(_selfResendActivationEmail_selfResendActivationEmailRequest);
        System.out.println("selfResendActivationEmail.result=" + _selfResendActivationEmail__return);


        }
        {
        System.out.println("Invoking resetPassword...");
        tyler.efm.services.schema.resetpasswordrequest.ResetPasswordRequestType _resetPassword_resetPasswordRequest = null;
        tyler.efm.services.schema.resetpasswordresponse.ResetPasswordResponseType _resetPassword__return = port.resetPassword(_resetPassword_resetPasswordRequest);
        System.out.println("resetPassword.result=" + _resetPassword__return);


        }
        {
        System.out.println("Invoking getPasswordQuestion...");
        tyler.efm.services.schema.getpasswordquestionrequest.GetPasswordQuestionRequestType _getPasswordQuestion_getPasswordQuestionRequest = null;
        tyler.efm.services.schema.passwordquestionresponse.PasswordQuestionResponseType _getPasswordQuestion__return = port.getPasswordQuestion(_getPasswordQuestion_getPasswordQuestionRequest);
        System.out.println("getPasswordQuestion.result=" + _getPasswordQuestion__return);


        }
        {
        System.out.println("Invoking changePassword...");
        tyler.efm.services.schema.changepasswordrequest.ChangePasswordRequestType _changePassword_changePasswordRequest = null;
        tyler.efm.services.schema.changepasswordresponse.ChangePasswordResponseType _changePassword__return = port.changePassword(_changePassword_changePasswordRequest);
        System.out.println("changePassword.result=" + _changePassword__return);


        }
        {
        System.out.println("Invoking updateNotificationPreferences...");
        tyler.efm.services.schema.updatenotificationpreferencesrequest.UpdateNotificationPreferencesRequestType _updateNotificationPreferences_updateNotificationPreferencesRequest = null;
        tyler.efm.services.schema.baseresponse.BaseResponseType _updateNotificationPreferences__return = port.updateNotificationPreferences(_updateNotificationPreferences_updateNotificationPreferencesRequest);
        System.out.println("updateNotificationPreferences.result=" + _updateNotificationPreferences__return);


        }

        System.exit(0);
    }

}

package com.chinatour.controller.admin;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chinatour.Constant;
import com.chinatour.service.AdminService;
import com.chinatour.webService.OAuthInfoProvider;
import com.chinatour.webService.impl.OAuthInfoProviderImpl;
import com.intuit.ia.connection.IAPlatformClient;
import com.intuit.ia.exception.OAuthException;
import com.intuit.ipp.exception.FMSException;

@Controller
@RequestMapping("/admin/oauth")
public class OAuthController {
	@Resource(name = "oAuthInfoProviderImpl")
    private OAuthInfoProvider oAuthInfoProvider = new OAuthInfoProviderImpl();
	
	@Autowired
	private AdminService adminService;

	@RequestMapping(value={"/request_token"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  public void requestOAuthToken(HttpServletResponse response)
	    throws IOException, FMSException
	  {
	    IAPlatformClient client = new IAPlatformClient();
	    try{
	      Map requestTokenAndSecret = client.getRequestTokenAndSecret(Constant.OAUTHCONSUMERKEY, Constant.OAUTHCONSUMERSECRET);

	      String requestToken = (String)requestTokenAndSecret.get("requestToken");
	      String requestTokenSecret = (String)requestTokenAndSecret.get("requestTokenSecret");

	      String companyId = Constant.OLDCOMPANYID;
	      if (this.adminService.getCurrent().getDeptId().equals(Constant.WJOFFICE)) {
	        companyId = Constant.NEWCOMPANYID;
	      }

	      this.oAuthInfoProvider.setRequestTokenValuesForCompany(companyId, requestToken, requestTokenSecret);

	      String authURL = client.getOauthAuthorizeUrl(requestToken);

	      response.sendRedirect(authURL);
	    }
	    catch (OAuthException e) {
	      throw new RuntimeException(e);
	    }
	  }

    @RequestMapping(value = "/request_token_ready", method = RequestMethod.GET)
    public void requestTokenReady(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        IAPlatformClient client = new IAPlatformClient();
        final String verifierCode = request.getParameter("oauth_verifier");
        final String realmID = request.getParameter("realmId");
        final String requestToken = request.getParameter("oauth_token");

        final com.chinatour.entity.CompanyRequestTokenSecret companyRequestTokenSecret = oAuthInfoProvider.getCompanyRequestTokenSecret(requestToken);

        try {
            final Map<String, String> oAuthAccessToken = client.getOAuthAccessToken(verifierCode, requestToken, companyRequestTokenSecret.getRequestTokenSecret(),
                    Constant.OAUTHCONSUMERKEY, Constant.OAUTHCONSUMERSECRET);

            final String accessToken = oAuthAccessToken.get("accessToken");
            final String accessTokenSecret = oAuthAccessToken.get("accessTokenSecret");

            oAuthInfoProvider.setAccessTokenForCompany(companyRequestTokenSecret.getAppCompanyId(), realmID,
                    accessToken, accessTokenSecret);
            //response.sendRedirect(getProtocolHostnameAndPort(request) + "/toQuickBook/close.html");
            response.sendRedirect(new StringBuilder().append(getProtocolHostnameAndPort(request)).append("/toQuickBook/close.html").toString());
        } catch (OAuthException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getProtocolHostnameAndPort(final HttpServletRequest request) {
        String protocol = request.getProtocol().split("/")[0].toLowerCase();
        String hostname = request.getServerName();
        int port = request.getServerPort();

        //StringBuilder result = new StringBuilder(protocol + "://" + hostname);
        StringBuilder result = new StringBuilder(new StringBuilder().append(protocol).append("://").append(hostname).toString());
        if (port != 80) {
            result.append(":").append(port);
        }

        return result.toString();
    }
}

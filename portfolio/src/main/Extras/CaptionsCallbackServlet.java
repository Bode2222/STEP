package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube.Builder;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CaptionListResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;

//Reference client_secret securely?

@WebServlet("/youtubeCaptionsCallback")
public class CaptionsCallbackServlet extends AbstractAuthorizationCodeCallbackServlet{
    private static final String CLIENT_SECRETS= "client_secret.json";
    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");

    private static final String APPLICATION_NAME = "YoutubeCaptionsGetter";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static NetHttpTransport httpTransport;

    private static String allErrors;


    @Override
  protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
      throws ServletException, IOException {
    resp.sendRedirect("/");
  }

  @Override
  protected void onError(
      HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
      throws ServletException, IOException {
    // handle error
  }

  @Override
  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
    url.setRawPath("/youtubeCaptionsCallback");
    //return url.build();
    return "https://8080-58c3049d-eb91-43aa-9b98-a6ad54fb2346.us-east1.cloudshell.dev/youtubeCaptionsCallback";
  }

  @Override
  protected AuthorizationCodeFlow initializeFlow() throws IOException {
    try{
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }
    catch(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        allErrors += sw.toString() + "\n";
    }

    File tempFile = new File("/home/olabodeige/Capstone_Project/portfolio/src/main/java/com/google/sps/servlets/client_secret.json");
    InputStream in = new FileInputStream(tempFile);
    if(in == null){
        allErrors += "File not found\n";
    }
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    return new GoogleAuthorizationCodeFlow.Builder(
        httpTransport, JSON_FACTORY, clientSecrets,SCOPES).setAccessType("offline").build();
  }

  @Override
  protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
    // return user ID
    return "TemporaryUserID";
  }
}
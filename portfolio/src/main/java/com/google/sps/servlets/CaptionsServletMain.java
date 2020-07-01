package com.google.sps.servlets;
//package com.journaldev.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;


@WebServlet("/YoutubeCaptions")
public class CaptionsServletMain extends HttpServlet {
    private static final String GET_URL_BASE_URL = "http://video.google.com/timedtext?lang=en&v=";
    private static String videoId = "npTC6b5-yvM";
	private static String GET_URL;
    
    /*
    * Makes a get request to retrieve english captions of specified youtube video
    */
    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        videoId = request.getParameter("videoId");
        
        GET_URL = GET_URL_BASE_URL + videoId;
		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();

        //If the get request worked, read it into a string. It should come as an xml file
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer stringResponse = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				stringResponse.append(inputLine);
			}
			in.close();

			// print result
			response.getWriter().println(stringResponse.toString());
		} 
        
        else {
            //Get request failed.
			response.getWriter().println("GET request failed");
		}

	}
}
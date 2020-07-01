package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.StringWriter;
import java.io.PrintWriter;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

@WebServlet("/VideoMetadata")
public class VideoMetadataServlet extends HttpServlet {
    private static String videoId = "-KqjH7mWggg";
    private static final String DEVELOPER_KEY = "AIzaSyBOUFTwKaYgeEpRNZvw9tt-T1QKufhbeoM";

    private static final String APPLICATION_NAME = "API code samples";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    
    /*
    * Rquests information about a video from youtube via YouTube Data v3 API
    */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            videoId = request.getParameter("videoId");
            YouTube youtubeService = getService();
            // Define and execute the API request
            YouTube.Videos.List videoRequest = youtubeService.videos()
                .list("snippet");
            VideoListResponse videoResponse = videoRequest.setKey(DEVELOPER_KEY)
                .setId(videoId)
                .execute();
            response.getWriter().println(videoResponse);
        }
        catch(Exception e){
            //Print errors to console
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
        }
	}

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }


}
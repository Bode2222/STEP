// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

	ArrayList<String> comments = new ArrayList<String>(1);

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //Get comments from datastore
    Query query = new Query("Comment").addSort("text", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
        String currentComment = (String) entity.getProperty("text");
        comments.add(currentComment);
    }

    //Convert comments to json
    String json = convertToJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = request.getParameter("text-input");
    comments.add(text);

    //Add comment to datastore
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", text);

    datastore.put(commentEntity);

    // Respond with the result.
    response.setContentType("text/html;");
    response.getWriter().println("Your last comment was: ");
    response.getWriter().println(comments.get(comments.size()-1));
  }

  private String convertToJson(ArrayList<String> comments) {
    comments.set(0, "First comment");
    String json = "{";
    json += "\"comments\":[";
    if(comments.size() > 0){
        for(int i = 0; i < comments.size(); i++){
            json += "\"";
            json += comments.get(i);
            json += "\"";
            if(i != comments.size()-1) json += ", ";
        }
        json += "]";
        json += "}";
    }
    
    return json;
  }
}

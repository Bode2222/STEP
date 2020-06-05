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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

	String[] comments = {"XXX_6942069_XXX was here", "first", "That pro is bad at this game, my bronze games are much more intense!"};

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //Convert comments to json
      String json = convertToJson();
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  private String convertToJson() {
    String json = "{";
    json += "\"comments\":[";
    for(int i = 0; i < 3; i++){
        json += "\"";
        json += comments[i];
        json += "\"";
        if(i != 3-1) json += ", ";
    }
    json += "]";
    json += "}";
    
    return json;
  }
}

package com.wibidata.hungry.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.wibidata.core.client.LocalWibiTableReader;
import com.wibidata.core.client.Wibi;
import com.wibidata.core.client.WibiDataRequest;
import com.wibidata.core.client.WibiDataRequestException;
import com.wibidata.core.client.WibiRowData;
import com.wibidata.core.client.WibiRowScanner;
import com.wibidata.core.client.WibiTable;
import com.wibidata.core.client.WibiTableReader;
import com.wibidata.core.client.avro.Node;
import com.wibidata.hungry.WibiContextListener;
import com.wibidata.hungry.model.Cuisine;

/**
 * The home page servlet.
 */
public class HomePageServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    request.setCharacterEncoding("UTF-8");

    final Wibi wibi = (Wibi) getServletContext().getAttribute(WibiContextListener.WIBI_ATTRIBUTE);

    // TODO: Step 7. Read the dishes by cuisine.
    List<Cuisine> cuisines = new ArrayList<Cuisine>();
    request.setAttribute("cuisines", cuisines);

    // If the user is logged in, read their recent ratings, favorite
    // ingredients, and recommendations.
    if (null != request.getSession().getAttribute("login")) {
      final String login = request.getSession().getAttribute("login").toString();

      // TODO: Step 12. Display the user's favorite ingredients.

      // TODO: Step 16. Display the user's recommended dishes.
    }

    request.getRequestDispatcher("/WEB-INF/view/HomePage.jsp").forward(request, response);
  }
}

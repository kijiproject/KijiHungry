package com.wibidata.hungry.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.mindrot.jbcrypt.BCrypt;

import com.wibidata.core.client.EntityId;
import com.wibidata.core.client.LocalWibiTableReader;
import com.wibidata.core.client.Wibi;
import com.wibidata.core.client.WibiDataRequest;
import com.wibidata.core.client.WibiDataRequestException;
import com.wibidata.core.client.WibiRowData;
import com.wibidata.core.client.WibiTable;
import com.wibidata.core.client.WibiTableReader;
import com.wibidata.hungry.WibiContextListener;

public class SignInServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.setCharacterEncoding("UTF-8");
    request.getRequestDispatcher("/WEB-INF/view/SignIn.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    request.setCharacterEncoding("UTF-8");

    final Wibi wibi = (Wibi) getServletContext().getAttribute(WibiContextListener.WIBI_ATTRIBUTE);

    final String login = request.getParameter("login");
    final String password = request.getParameter("password");

    if (null == login || login.isEmpty()
        || null == password || password.isEmpty()) {
      request.setAttribute("login", login);
      request.setAttribute("password", password);
      request.setAttribute("error", "All fields are required.");
      request.getRequestDispatcher("/WEB-INF/view/SignIn.jsp").forward(request, response);
      return;
    }

    final WibiTable userTable = WibiTable.open(wibi, "wibi_hungry_user");
    final EntityId entityId = userTable.getEntityId(login);
    final WibiTableReader reader = new LocalWibiTableReader(userTable);
    try {
      // Authenticate the user.
      WibiDataRequest dataRequest = new WibiDataRequest()
          .addColumn(new WibiDataRequest.Column("info", "password"))
          .addColumn(new WibiDataRequest.Column("info", "name"));
      WibiRowData row = reader.get(entityId, dataRequest);
      if (!row.containsColumn("info", "password")
          || !BCrypt.checkpw(password, row.getStringValue("info", "password").toString())) {
        request.setAttribute("login", login);
        request.setAttribute("error", "Invalid login or password.");
        request.getRequestDispatcher("/WEB-INF/view/SignIn.jsp").forward(request, response);
        return;
      }
      request.setAttribute("name", row.getStringValue("info", "name").toString());
    } catch (WibiDataRequestException e) {
      throw new IOException(e);
    } finally {
      IOUtils.closeQuietly(reader);
      IOUtils.closeQuietly(userTable);
    }

    // Set the login cookie for the user.
    request.getSession().setAttribute("login", login);
    request.getRequestDispatcher("/WEB-INF/view/SignedIn.jsp").forward(request, response);
  }
}

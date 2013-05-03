package com.wibidata.hungry.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.mindrot.jbcrypt.BCrypt;

import com.wibidata.core.client.EntityId;
import com.wibidata.core.client.Wibi;
import com.wibidata.core.client.WibiDataRequest;
import com.wibidata.core.client.WibiDataRequestException;
import com.wibidata.core.client.WibiRowData;
import com.wibidata.core.client.WibiTable;
import com.wibidata.core.client.WibiTableReader;
import com.wibidata.core.client.LocalWibiTableReader;
import com.wibidata.core.client.WibiTableWriter;
import com.wibidata.core.client.LocalWibiTableWriter;

import com.wibidata.hungry.WibiContextListener;

public class CreateUserServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.setCharacterEncoding("UTF-8");
    request.getRequestDispatcher("/WEB-INF/view/CreateUser.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    final Wibi wibi = (Wibi) getServletContext().getAttribute(WibiContextListener.WIBI_ATTRIBUTE);

    request.setCharacterEncoding("UTF-8");

    final String name = request.getParameter("name");
    final String login = request.getParameter("login");
    final String password = request.getParameter("password");

    if (null == name || name.isEmpty()
        || null == login || login.isEmpty()
        || null == password || password.isEmpty()) {
      request.setAttribute("name", name);
      request.setAttribute("login", login);
      request.setAttribute("password", password);
      request.setAttribute("error", "All fields are required.");
      request.getRequestDispatcher("/WEB-INF/view/CreateUser.jsp").forward(request, response);
      return;
    }

    final WibiTable userTable = WibiTable.open(wibi, "wibi_hungry_user");
    final EntityId entityId = userTable.getEntityId(login);
    final WibiTableReader reader = new LocalWibiTableReader(userTable);
    try {
      try {
        // Check if the user already exists first.
        WibiDataRequest dataRequest = new WibiDataRequest()
            .addColumn(new WibiDataRequest.Column("info", "login"));
        WibiRowData row = reader.get(entityId, dataRequest);
        if (row.containsColumn("info", "login")) {
          request.setAttribute("name", name);
          request.setAttribute("login", login);
          request.setAttribute("password", password);
          request.setAttribute("error", "That login is already in use, please choose another.");
          request.getRequestDispatcher("/WEB-INF/view/CreateUser.jsp").forward(request, response);
          return;
        }
      } catch (WibiDataRequestException e) {
        throw new IOException(e);
      } finally {
        IOUtils.closeQuietly(reader);
      }

      WibiTableWriter writer = new LocalWibiTableWriter(userTable);
      try {
        // Create the user.
        writer.put(entityId, "info", "name", name);
        writer.put(entityId, "info", "login", login);
        writer.put(entityId, "info", "password", BCrypt.hashpw(password, BCrypt.gensalt()));
      } catch (InterruptedException e) {
        throw new IOException(e);
      } finally {
        IOUtils.closeQuietly(writer);
      }
    } finally {
      IOUtils.closeQuietly(userTable);
    }

    // Set the login cookie for the user.
    request.getSession().setAttribute("login", login);
    request.getRequestDispatcher("/WEB-INF/view/UserCreated.jsp").forward(request, response);
  }
}

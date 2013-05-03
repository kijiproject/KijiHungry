package com.wibidata.hungry.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.wibidata.core.client.EntityId;
import com.wibidata.core.client.LocalWibiTableReader;
import com.wibidata.core.client.Wibi;
import com.wibidata.core.client.WibiDataRequest;
import com.wibidata.core.client.WibiDataRequestException;
import com.wibidata.core.client.WibiRowData;
import com.wibidata.core.client.WibiTable;
import com.wibidata.core.client.WibiTableReader;
import com.wibidata.hungry.WibiContextListener;

public class SignOutServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.setCharacterEncoding("UTF-8");
    request.getSession().removeAttribute("login");
    request.getRequestDispatcher("/WEB-INF/view/SignOut.jsp").forward(request, response);
  }
}

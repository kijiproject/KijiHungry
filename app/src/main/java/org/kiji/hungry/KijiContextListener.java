package com.wibidata.hungry;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wibidata.core.client.Wibi;
import com.wibidata.core.client.WibiConfiguration;

/**
 * A servlet context listener that creates a Wibi connection for use during
 * the web application's lifetime.
 */
public class WibiContextListener implements ServletContextListener {
  private static final Logger LOG = LoggerFactory.getLogger(WibiContextListener.class);

  /** The attribute in the servlet context that stores the Wibi connection. */
  public static final String WIBI_ATTRIBUTE = "wibi";

  @Override
  public void contextInitialized(ServletContextEvent event) {
    final ServletContext servletContext = event.getServletContext();

    try {
      final WibiConfiguration conf = new WibiConfiguration(
          HBaseConfiguration.create(), WibiConfiguration.DEFAULT_INSTANCE_NAME);
      LOG.info("Opening a wibi connection...");
      final Wibi wibi = Wibi.open(conf);
      LOG.info("Opened.");
      servletContext.setAttribute(WIBI_ATTRIBUTE, wibi);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    final ServletContext servletContext = event.getServletContext();

    final Wibi wibi = (Wibi) servletContext.getAttribute(WIBI_ATTRIBUTE);
    servletContext.removeAttribute(WIBI_ATTRIBUTE);

    LOG.info("Closing the wibi connection...");
    IOUtils.closeQuietly(wibi);
    LOG.info("Closed.");
  }
}

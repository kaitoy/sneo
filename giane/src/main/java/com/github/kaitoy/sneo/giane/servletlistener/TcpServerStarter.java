/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.servletlistener;

import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.h2.tools.Server;

@Deprecated
public class TcpServerStarter implements ServletContextListener {

  public static final String PORT = "9092";

  private Server server;

  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      this.server
        = Server.createTcpServer(
            "-tcpPort", PORT,
            "-baseDir", "."
          );
      server.start();
    } catch (SQLException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    if (server != null && server.isRunning(true)) {
      server.stop();
    }
    server = null;
  }

}

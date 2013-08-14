/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.h2;

import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.h2.tools.Server;

public class PgServerStarter implements ServletContextListener {

  public static final String PORT = "54321";

  private Server server;

  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      this.server
        = Server.createPgServer(
            "-pgPort", PORT,
            "-baseDir", ".",
            "-pgAllowOthers"
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

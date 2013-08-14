/*_##########################################################################
  _##
  _##  Copyright (C) 2012 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.jetty;

import java.net.URL;
import java.security.ProtectionDomain;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class GianeStarter {

  public static void main(String[] args) throws Exception {
    Server server = new Server(8080);

    WebAppContext context = new WebAppContext();
    context.setServer(server);
    context.setContextPath("/giane");
    //context.setWelcomeFiles(new String[] {"index.action"});

    ProtectionDomain protectionDomain = GianeStarter.class.getProtectionDomain();
    URL location = protectionDomain.getCodeSource().getLocation();
    context.setWar(location.toExternalForm());

    server.setHandler(context);
    server.start();
    server.join();
  }

}

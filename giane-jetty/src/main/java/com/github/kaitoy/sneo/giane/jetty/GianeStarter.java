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
    int httpPort = 8080;

    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      try {
        if ("--httpPort".equals(arg)) {
          httpPort = Integer.parseInt(args[++i]);
        } else {
          System.err.println("Invalid option: " + arg);
          System.exit(1);
        }
      } catch (Exception e) {
        System.err.println("An error occurred in processing an option: " + arg);
        System.exit(1);
      }
    }

    Server server = new Server(httpPort);

    WebAppContext context = new WebAppContext();
    context.setServer(server);
    context.setContextPath("/giane");

    ProtectionDomain protectionDomain = GianeStarter.class.getProtectionDomain();
    URL location = protectionDomain.getCodeSource().getLocation();
    context.setWar(location.toExternalForm());

    server.setHandler(context);
    server.start();
    server.join();
  }

}

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
import com.github.kaitoy.sneo.util.ConsoleBlocker;
import com.github.kaitoy.sneo.util.Constants;

public class GianeStarter {

  public static void main(String[] args) throws Exception {
    int httpPort = 8080;

    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      try {
        if ("--httpPort".equals(arg)) {
          httpPort = Integer.parseInt(args[++i]);
        }
        else if ("--jmx.httpPort".equals(arg)) {
          Integer jmxHttpPort = Integer.parseInt(args[++i]);
          System.setProperty(Constants.JMX_HTTP_PORT_KEY, jmxHttpPort.toString());
        }
        else if ("--jmx.rmiPort".equals(arg)) {
          Integer jmxRmiPort = Integer.parseInt(args[++i]);
          System.setProperty(Constants.JMX_RMI_PORT_KEY, jmxRmiPort.toString());
        }
        else {
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
    ConsoleBlocker.block("** Hit Enter key to stop Giane **");
    server.stop();
    server.destroy();
  }

}

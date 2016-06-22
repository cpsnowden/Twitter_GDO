package com.cps15;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebApp{

    public static void main(String[] args) throws Exception {

        String webAppDirLocation = "src/main/webapp";
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        Server server = new Server(Integer.valueOf(webPort));

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setDescriptor(webAppDirLocation + "/WEB-INF/web.xml");
        webapp.setResourceBase(webAppDirLocation);

        server.setHandler(webapp);

        server.start();
        server.join();

    }
}
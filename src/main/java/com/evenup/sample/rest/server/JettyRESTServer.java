package com.evenup.sample.rest.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * A stand-alone HTTP server.  It can be instantiated and run elsewhere,
 * or started with {@link #main(String[])}.
 * 
 * Copyright 2014 EvenUp, Inc.
 *
 * THIS CODE IS INTENDED AS AN EXAMPLE ONLY.  IT HAS NOT BEEN TESTED AND 
 * SHOULD NOT BE USED IN A PRODUCTION ENVIRONMENT.
 * 
 * THE  CODE IS  PROVIDED "AS  IS",  WITHOUT WARRANTY  OF ANY  KIND, EXPRESS  
 * OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE WARRANTIES  OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *
 * @author Kevin G. McManus
 *
 */
public class JettyRESTServer implements Runnable {

    private int port;
    private Server server;

    public JettyRESTServer(final int port, final BlockingQueue<String> messageQ) {
        this.setPort(port);
        // FIXME figure out the new jersey DI framework...
        EventCallbackResource.setMessageQ(messageQ);
    }
    
    public void run() {
        try {
            // This is all just plumbing.  It starts an HTTP server
            // and looks in com.evenup.sample.rest.server
            // for EventCallbackResource, with the path "events".
            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/");
            webapp.setWar("/");
            ServletContainer restServlet = new ServletContainer();
            final ServletHolder servletHolder = new ServletHolder(restServlet);
            servletHolder.setInitParameter("jersey.config.server.provider.packages", "com.evenup.sample.rest.server");
            webapp.addServlet(servletHolder, "/*");
            server = new Server(getPort());
            server.setHandler(webapp);
            server.start();
            server.join();
        } catch (Exception e) {
            // FIXME
            System.out.println(e.getMessage());
        }
    }

    public void stop() {
        try {
            if (server != null) {
                server.stop();
            }
        } catch (Throwable t) {
            System.err.println("Unable to stop Jetty server.");
        }
    }

    public boolean isRunning() {
        if (server == null) {
            return false;
        }
        return server.isRunning();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws ParseException, InterruptedException {
        final Options options = new Options();
        options.addOption("p", "port", true, "post to listen on");
        
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse( options, args);
        int port = Integer.parseInt(cmd.getOptionValue("port", "9000")); 

        final LinkedBlockingQueue<String> messageQ = new LinkedBlockingQueue<String>();
        StdOutMessageQueueListener listener = new StdOutMessageQueueListener(messageQ);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
        try {
            new JettyRESTServer(port, messageQ).run();
        } finally {
            listener.setStop(true);
            listenerThread.join();
        }
    }
}

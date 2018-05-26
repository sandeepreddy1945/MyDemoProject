package com.app.chart.fx;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.app.chart.rest.CORSFilter;
import com.app.chart.rest.EntryPoint;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JettyServerMain {

	public static void main(String[] args) throws Exception {

		// startServer();
	}

	private Server server;

	/**
	 * @throws Exception
	 * @throws InterruptedException
	 * 
	 */
	public JettyServerMain() {
		// initialize the server.
		initializeServer();
		log.info("Server Initialized ");
	}

	private void initializeServer() {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server = new Server(8020);

		server.setHandler(context);
		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", EntryPoint.class.getCanonicalName());
		jerseyServlet.setInitParameter("javax.ws.rs.container.ContainerResponseFilter",
				CORSFilter.class.getCanonicalName());

	}

	public void startServer() throws Exception {
		server.start();
		//server.join();
		log.info("Server Started on Port 8020");

	}

	public void stopServer() throws Exception {
		server.stop();
		log.info("Server Stopped.");
	}
}
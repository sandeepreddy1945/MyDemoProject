package com.app.chart.fx;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.app.chart.rest.CORSFilter;
import com.app.chart.rest.EntryPoint;

public class JettyServerMain {

	public static void main(String[] args) throws Exception {

		startServer();
	}

	private static void startServer() throws Exception, InterruptedException {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		Server server = new Server(8020);

		server.setHandler(context);
		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", EntryPoint.class.getCanonicalName());
		jerseyServlet.setInitParameter("javax.ws.rs.container.ContainerResponseFilter",
				CORSFilter.class.getCanonicalName());
		server.start();
		server.join();
	}
}
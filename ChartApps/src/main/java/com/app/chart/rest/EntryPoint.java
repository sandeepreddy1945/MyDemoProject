package com.app.chart.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This is important that ever response header contains the all_orginis added in order
 * to avoid problems caused by CORS.
 * 
 * @author Sandeep Reddy Battula
 *
 */
@Path("/")
public class EntryPoint {

	private static final String ALL_ORIGINS = "*";
	private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public Response test() {
		return Response.ok().entity("Test Example").header(ACCESS_CONTROL_ALLOW_ORIGIN, ALL_ORIGINS).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchEmployeeData() {

		return Response.ok().entity("For Now Its Now Yet Completely Implemented. Development Work Still In Progress")
				.header(ACCESS_CONTROL_ALLOW_ORIGIN, ALL_ORIGINS).build();
	}
}
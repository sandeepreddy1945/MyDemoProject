package com.app.chart.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/entry-point")
public class EntryPoint {

	private static final String ALL_ORIGINS = "*";
	private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public Response test() {
		return Response.ok()
	               .entity("Test Example")
	               .header(ACCESS_CONTROL_ALLOW_ORIGIN, ALL_ORIGINS)
	               .build();
	}
}
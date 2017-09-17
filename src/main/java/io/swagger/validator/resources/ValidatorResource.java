package io.swagger.validator.resources;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.responses.ApiResponse;
import io.swagger.validator.models.ValidationResponse;
import io.swagger.validator.services.ValidatorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/")
public class ValidatorResource {
    ValidatorService service = new ValidatorService();

    @GET
    @Operation(summary = "Validates a spec based on a URL")
    @ApiResponse(content = {})
    @Produces({"image/png"})
    public Response validateByUrl(
            @Context HttpServletRequest request,
            @Context HttpServletResponse response,
            @Parameter(description = "url of spec to validate") @QueryParam("url") String url) throws WebApplicationException {
        try {
            service.validateByUrl(request, response, url);
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/debug")
    @Produces({"application/json"})
    @Operation(summary = "Validates a spec based on a URL",
            responses = {
                    @ApiResponse(description = "Pets matching criteria",
                        content = @Content(schema = @Schema(implementation = ValidationResponse.class))
            )})
    @ApiResponse(content = {})
    public Response debugByUrl(
            @Context HttpServletRequest request,
            @Context HttpServletResponse response,
            @Parameter(description = "url of spec to validate") @QueryParam("url") String url) throws WebApplicationException {
        try {
            return Response.ok().entity(service.debugByUrl(request, response, url)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }

    @POST
    @Path("/debug")
    @Produces({"application/json"})
    @Operation(summary = "Validates a spec based on message body",
            responses = {
                    @ApiResponse(description = "Pets matching criteria",
                            content = @Content(schema = @Schema(implementation = ValidationResponse.class))
                    )})
    @ApiResponse(content = {})
    public Response debugByContent(
            @Context HttpServletRequest request,
            @Context HttpServletResponse response,
            @Parameter(description = "spec contents") String spec) throws WebApplicationException {
        try {
            return Response.ok().entity(service.debugByContent(request, response, spec)).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }
    }
}
package com.cps15.api.resources;

import com.codahale.metrics.annotation.Timed;
import com.cps15.api.data.Analytics;
import com.cps15.api.data.AnalyticsRequest;
import com.cps15.service.AnalyticsService.AnalyticsManager;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by ChrisSnowden on 25/06/2016.
 */
public class AnalyticsResource {

    private String datasetID;
    private AnalyticsManager analyticsManager;

    public AnalyticsResource(String datasetID, AnalyticsManager analyticsManager) {

        this.datasetID = datasetID;
        this.analyticsManager = analyticsManager;

    }


    @GET
    @PermitAll
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnalytics(){

        return Response.ok(analyticsManager.getAnalyticsByDSID(datasetID)).build();

    }


    @POST
    @PermitAll
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAnalytics(AnalyticsRequest analyticsRequest){

        Analytics analytics = analyticsManager.processRequest(analyticsRequest, datasetID);

        return (analytics!=null)?Response.ok().entity(analytics).build():Response.serverError().build();



    }


    @GET
    @Path("/{id}")
    @PermitAll
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnalyticsByID(@PathParam("id") String id) {

        return Response.ok(analyticsManager.getAnalyticsById(id, datasetID)).build();

    }


    @GET
    @Path("/{id}/data")
    @PermitAll
    @Timed
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAnalyticsDataByID(@PathParam("id") String id) {

        return Response.ok(analyticsManager.getDataByID(id, datasetID)).build();
    }

    @DELETE
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAnalytics(@PathParam("id") String id) {

        if (analyticsManager.deleteAnalytics(id, datasetID)) {
            return Response.ok().build();
        } else {
            return Response.noContent().build();
        }
    }


}

package com.cps15.api.resources;

import com.codahale.metrics.annotation.Timed;
import com.cps15.api.data.*;
import com.cps15.service.AnalyticsService.AnalyticsManager;
import com.cps15.service.DataService.DatasetManager;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
@Path("/API/dataset")
@RolesAllowed({"APP", "ADMIN", "USER"})
public class DatasetResource {

    private AnalyticsManager analyticsManager;
    private DatasetManager datasetManager;

    @Context
    UriInfo uriInfo;

    public DatasetResource(DatasetManager datasetManager, AnalyticsManager analyticsManager) {

        this.datasetManager = datasetManager;
        this.analyticsManager = analyticsManager;

    }

    @GET
    @PermitAll
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataStream(@PathParam("id") String id) {

        DatasetInfo datasetInfo = datasetManager.getDataset(id);
        if (null == datasetInfo) {
            return Response.noContent().build();
        } else {
            return Response.ok(datasetInfo).build();
        }
    }


    @DELETE
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStream(@PathParam("id") String id) {

        if (datasetManager.deleteDataset(id)) {
            return Response.ok().build();
        } else {
            return Response.noContent().build();
        }
    }


    @GET
    @PermitAll
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public List<DatasetInfo> getDataStreams(@QueryParam("status") String status) {

        if (null != status) {
            return datasetManager.getDatasetsByStatus(Status.getValidStatus(status));
        }
        return datasetManager.getDatasets();
    }


    @POST
    @Timed
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDataStream(DatasetRequest datasetRequest) {

        DatasetInfo datasetInfo = datasetManager.addDataset(datasetRequest);
        if (datasetInfo == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Unexpected limit type " + datasetRequest.getLimitType())
                    .build();
        }
        return Response.created(uriInfo.getAbsolutePathBuilder().path(datasetInfo.getId()).build()).entity(datasetInfo).build();
    }


    @Path("/{id}/analytics")
    @PermitAll
    public AnalyticsResource getAnalytics(@PathParam("id") String id) {

        return new AnalyticsResource(id, analyticsManager);

    }


    @PUT
    @Timed
    @RolesAllowed("ADMIN")
    @Path("/{id}/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeStatus(@PathParam("id") String id, Status status) {

        if (!datasetManager.toggleStatus(id, status)) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        return Response.ok().build();
    }


    @GET
    @Timed
    @PermitAll
    @Path("/{id}/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getStatus(@PathParam("id") String id) {

        Status status = datasetManager.getStatus(id);
        if (status == null) {
            return Response.noContent().build();
        } else {
            return Response.ok().entity(status).build();
        }
    }

}
package com.cps15.api.resources;

import com.codahale.metrics.annotation.Timed;
import com.cps15.api.data.DataFilter;
import com.cps15.api.data.DataStreamRequest;
import com.cps15.api.data.Status;
import com.cps15.api.persistence.DataFilterDAO;
import com.cps15.api.persistence.DataStreamRequestDAO;
import com.cps15.service.DataService.TwitterStreamManager;
import com.cps15.service.Database.DatabaseManager;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
@Path("/api/dataCollections/dataFilter")
@RolesAllowed({"APP","ADMIN","USER"})
public class DataStreamResource {

    private DataFilterDAO dataFilterDAO;
    private DataStreamRequestDAO dataStreamRequestDAO;
    private TwitterStreamManager twitterStreamManager;

    @Context
    UriInfo uriInfo;

    public DataStreamResource(DataFilterDAO dataFilterDAO, DataStreamRequestDAO dataStreamRequestDAO) {
        this.dataFilterDAO = dataFilterDAO;
        this.dataStreamRequestDAO = dataStreamRequestDAO;
        this.twitterStreamManager = new TwitterStreamManager(dataFilterDAO, new DatabaseManager("TwitterDataCollections"));
    }

    @GET
    @PermitAll
    @Timed
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataStream(@PathParam("id") String id) {

        DataFilter dataFilter = dataFilterDAO.findByID(id);
        if(null == dataFilter) {
            return Response.noContent().build();
        } else {
            return Response.ok(dataFilter).build();
        }
    }

    @DELETE
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStream(@PathParam("id") String id){

        DataFilter dataFilter = dataFilterDAO.findByID(id);
        if(null == dataFilter) {
            return Response.noContent().build();
        } else if(!twitterStreamManager.deleteCollection(dataFilter)) {
            return Response.serverError().build();
        } else {
            return Response.ok().build();
        }
    }

    @GET
    @PermitAll
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public List<DataFilter> getDataStreams() {

        return dataFilterDAO.dataStreams();

    }

    @POST
    @Timed
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDataStream(DataStreamRequest dataStreamRequest) {

        dataStreamRequestDAO.addDataStreamRequest(dataStreamRequest);

        DataFilter dataFilter;

        try {
            dataFilter = new DataFilter(dataStreamRequest);
        } catch (InvalidParameterException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Unexpected limit type " + dataStreamRequest.getLimitType())
                    .build();
        }

        dataFilterDAO.addDataStream(dataFilter);
        twitterStreamManager.startFilter(dataFilter);

        return Response.created(uriInfo.getAbsolutePathBuilder().path(dataFilter.getId()).build()).entity(dataFilter).build();
    }

    @PUT
    @Timed
    @RolesAllowed("ADMIN")
    @Path("/{id}/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeStatus(@PathParam("id") String id, Status status) {

        DataFilter dataFilter = dataFilterDAO.findByID(id);
        if(null == dataFilter) {
            return Response.noContent().build();
        }

        if(status.getStatus() == Status.STATUS.STOPPED) {
            return twitterStreamManager.stopFilter(dataFilter) ? Response.ok().build() : Response.status(Response.Status.CONFLICT).build();
        } else if(status.getStatus() == Status.STATUS.ORDERED) {
            return twitterStreamManager.restartFilter(dataFilter) ?  Response.ok().build() : Response.status(Response.Status.CONFLICT).build();
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

        DataFilter dataFilter = dataFilterDAO.findByID(id);
        if(null == dataFilter) {
            return Response.noContent().build();
        }

        return Response.ok().entity(new Status(dataFilter.getStatus())).build();

    }

}

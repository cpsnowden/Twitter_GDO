package com.cps15.api.resources;

import com.codahale.metrics.annotation.Timed;
import com.cps15.api.data.DataStream;
import com.cps15.api.data.DataStreamRequest;
import com.cps15.api.data.Status;
import com.cps15.api.persistence.DataStreamDAO;
import com.cps15.api.persistence.DataStreamRequestDAO;
import com.cps15.service.DataService.DataServiceManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
@Path("/dataCollections/dataStream")
@RolesAllowed({"APP","ADMIN","USER"})
public class DataStreamResource {

    private DataStreamDAO dataStreamDAO;
    private DataStreamRequestDAO dataStreamRequestDAO;
    private DataServiceManager dataServiceManager;

    @Context
    UriInfo uriInfo;

    public DataStreamResource(DataStreamDAO dataStreamDAO, DataStreamRequestDAO dataStreamRequestDAO) {
        this.dataStreamDAO = dataStreamDAO;
        this.dataStreamRequestDAO = dataStreamRequestDAO;
        this.dataServiceManager = new DataServiceManager(dataStreamDAO);
    }

    @GET
    @PermitAll
    @Timed
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DataStream getDataStream(@PathParam("id") String id) {

        return dataStreamDAO.findByID(id);
    }

    @GET
    @PermitAll
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public List<DataStream> getDataStreams() {

        return dataStreamDAO.dataStreams();

    }

    @POST
    @Timed
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDataStream(DataStreamRequest dataStreamRequest) {

        dataStreamRequestDAO.addDataStreamRequest(dataStreamRequest);

        DataStream dataStream;
        try {
            dataStream = new DataStream(dataStreamRequest);
        } catch (InvalidParameterException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Unexpected limit type " + dataStreamRequest.getLimitType()).build();
        }

        dataStreamDAO.addDataStream(dataStream);
        dataServiceManager.addDataStream(dataStream);

        return Response.created(uriInfo.getAbsolutePathBuilder().path(dataStream.getId()).build()).entity(dataStream).build();
    }




    @PUT
    @Timed
    @RolesAllowed("ADMIN")
    @Path("/{id}/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeStatus(@PathParam("id") String id, Status status) {

        DataStream dataStream = getDataStream(id);

//        boolean confict = dataStream.getStatus() == status.getStatus();
//        confict &= dataStream.getStatus() == Status.STATUS.ERROR;
//
//        if(confict) {
//            return Response.status(Response.Status.CONFLICT).build();
//        }

        if(status.getStatus() == Status.STATUS.STOPPED) {
            return dataServiceManager.stopDataService(dataStream) ? Response.ok().build() : Response.status(Response.Status.CONFLICT).build();
        } else if(status.getStatus() == Status.STATUS.ORDERED) {
            return dataServiceManager.attemptRestartDataService(dataStream, true) ?  Response.ok().build() : Response.status(Response.Status.CONFLICT).build();
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

        DataStream dataStream = getDataStream(id);
        return Response.ok().entity(new Status(dataStream.getStatus())).build();

    }

}

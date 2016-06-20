package com.cps15.api.resources;

import com.codahale.metrics.annotation.Timed;
import com.cps15.api.data.DataStream;
import com.cps15.api.data.DataStreamRequest;
import com.cps15.api.persistence.DataStreamDAO;
import com.cps15.api.persistence.DataStreamRequestDAO;
import com.cps15.service.DataService.DataServiceManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
@Path("/dataStream")
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
    @Timed
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DataStream getDataStream(@PathParam("id") String id) {

        return dataStreamDAO.findByID(id);
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public List<DataStream> getDataStreams() {

        return dataStreamDAO.dataStreams();

    }

    @POST
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDataStream(DataStreamRequest dataStreamRequest) {

        dataStreamRequestDAO.addDataStreamRequest(dataStreamRequest);
        URI createdURI =  uriInfo.getAbsolutePathBuilder().path("1").build();

        DataStream dataStream = new DataStream(dataStreamRequest);

        dataStreamDAO.addDataStream(dataStream);
        dataServiceManager.addDataStream(dataStream);

        return Response.created(createdURI).entity(dataStream).build();
    }
}

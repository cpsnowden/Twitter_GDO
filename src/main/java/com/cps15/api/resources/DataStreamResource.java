package com.cps15.api.resources;

import com.codahale.metrics.annotation.Timed;
import com.cps15.api.data.DataStream;
import com.cps15.api.data.DataStreamRequest;
import com.cps15.service.DataService.DataServiceManager;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;


import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
@Path("/dataStream")
public class DataStreamResource {

    private JacksonDBCollection<DataStreamRequest, String> requestCollection;
    private JacksonDBCollection<DataStream, String> streamCollection;
    private DataServiceManager dataServiceManager;

    @Context
    UriInfo uriInfo;

    public DataStreamResource(JacksonDBCollection<DataStreamRequest, String> requestCollection,
                              JacksonDBCollection<DataStream, String> streamCollection,
                              DataServiceManager dataServiceManager) {
        this.requestCollection = requestCollection;
        this.streamCollection = streamCollection;
        this.dataServiceManager = dataServiceManager;
    }

    @GET
    @Timed
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DataStream getDataStream(@PathParam("id") String id) {

        return streamCollection.findOneById(id);
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public List<DataStream> getDataStreams() {
        List<DataStream> dataStreams = new ArrayList<>();
        DBCursor<DataStream> cursor = streamCollection.find();
        while(cursor.hasNext()) {
            dataStreams.add(cursor.next());
        }

        return dataStreams;
    }

    @POST
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDataStream(DataStreamRequest dataStreamRequest) {
        requestCollection.insert(dataStreamRequest);
        URI createdURI =  uriInfo.getAbsolutePathBuilder().path("1").build();

        DataStream dataStream = new DataStream(dataStreamRequest.getDescription(),
                dataStreamRequest.getTags());

        streamCollection.insert(dataStream);
        dataServiceManager.addDataStream(dataStream);

        return Response.created(createdURI).entity(dataStream).build();
    }
}

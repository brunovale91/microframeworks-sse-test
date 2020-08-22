package org.swarmbit

import io.smallrye.mutiny.Multi
import org.jboss.resteasy.annotations.SseElementType
import java.time.Duration
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


@Path("/quarkus")
class StreamResource {

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.TEXT_PLAIN)
    @Path("/stream")
    fun stream(): Multi<Long> {
        return Multi
                .createFrom()
                .ticks()
                .every(Duration.ofSeconds(5))
    }
}
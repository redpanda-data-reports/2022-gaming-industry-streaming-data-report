package com.redpanda.gaming.matchmaker.frontend;

import com.redpanda.gaming.matchmaker.frontend.model.MatchResponse;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.mutiny.Multi;

@Path("/match")
public class MatchmakerResource {

  @Channel("match-requests")
  Emitter<String> matchRequestEmitter;

  @Channel("match-responses")
  Multi<MatchResponse> results;

  /**
   * Endpoint to generate a new match request id and send it to "match-requests" Redpanda topic using the emitter.
   */
  @POST
  @Path("/request")
  @Produces(MediaType.TEXT_PLAIN)
  public String createRequest() {
    UUID uuid = UUID.randomUUID();
    matchRequestEmitter.send(uuid.toString());
    return uuid.toString();
  }

  /**
   * Endpoint retrieving the "quotes" Kafka topic and sending the items to a server sent event.
   */
  @GET
  @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
  public Multi<MatchResponse> stream() {
    return results.log();
  }
}
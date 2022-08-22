package com.redpanda.gaming.personalization;

import com.redpanda.gaming.personalization.model.Ad;
import io.smallrye.mutiny.Multi;
import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;


@Path("/ads")
public class AdResource {

  @Channel("ads")
  Multi<Ad> ads;

  /**
   * Endpoint retrieving the "ads" Redpanda topic and sending the items to a server sent event.
   */
  @GET
  @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
  public Multi<Ad> stream() {
    return ads.log();
  }
}
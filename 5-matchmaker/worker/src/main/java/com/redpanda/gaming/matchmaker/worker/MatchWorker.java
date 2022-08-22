package com.redpanda.gaming.matchmaker.worker;

import com.redpanda.gaming.matchmaker.frontend.model.MatchResponse;
import io.smallrye.reactive.messaging.annotations.Blocking;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;


@ApplicationScoped
public class MatchWorker {

  @Incoming("match-requests")
  @Outgoing("match-responses")
  @Blocking
  public MatchResponse process(String playerId) throws InterruptedException {
    // simulate an external API invocatio here
    Thread.sleep(800);

    //Fomulate a mock response with some gamer tags
    List<String> players = new ArrayList<>();
    players.add("EatBullets");
    players.add("PR0_GGRAM3D");
    players.add("MindlessKilling");
    players.add("Shoot2Kill");
    players.add("LunaStar");

    MatchResponse response = new MatchResponse();
    response.setPlayers(players);
    return response;
  }
}

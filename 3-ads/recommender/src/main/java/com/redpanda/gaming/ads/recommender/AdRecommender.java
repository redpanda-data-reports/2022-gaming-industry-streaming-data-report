package com.redpanda.gaming.ads.recommender;

import com.redpanda.gaming.ads.recommender.model.Ad;
import com.redpanda.gaming.ads.recommender.model.GamingCompletedEvent;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;


@ApplicationScoped
public class AdRecommender {

  private static final String GAMING_EVENTS_TOPIC = "gaming.results";
  private static final String AD_RECOMMENDATIONS_TOPIC = "ads";

  private static final int AD_TRIGGER_THRESHOLD = 5;

  @Produces
  public Topology buildTopology() {
    StreamsBuilder builder = new StreamsBuilder();

    ObjectMapperSerde<GamingCompletedEvent> eventSerDe = new ObjectMapperSerde<>(GamingCompletedEvent.class);
    ObjectMapperSerde<Ad> adSerDe = new ObjectMapperSerde<>(Ad.class);

    builder.stream(GAMING_EVENTS_TOPIC, Consumed.with(Serdes.String(), eventSerDe))
        .filter((key, value) -> value.result.equalsIgnoreCase("LOSS"))
        .groupByKey()
        .count()
        .filter((key,count) -> count > 3)
        .toStream()
        .mapValues((key, value) -> {
          return new Ad(key,
              "Want to win next time?",
              "Try buying the laser shooter weapon to easily secure a win next time."
          );
        })
        .to(AD_RECOMMENDATIONS_TOPIC, Produced.with(Serdes.String(), adSerDe));

    return builder.build();
  }
}
package com.redpanda.gaming.ads.recommender.model;

public class GamingCompletedEvent {
  public String playerId;
  public String result;

  public GamingCompletedEvent(String playerId, String result) {
    this.playerId = playerId;
    this.result = result;
  }
}

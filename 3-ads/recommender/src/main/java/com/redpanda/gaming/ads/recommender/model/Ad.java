package com.redpanda.gaming.ads.recommender.model;

public class Ad {
  public String playerId;
  public String title;
  public String content;

  public Ad(String playerId, String title, String content) {
    this.playerId = playerId;
    this.title = title;
    this.content = content;
  }
}

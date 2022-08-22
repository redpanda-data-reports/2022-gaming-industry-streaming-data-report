package com.redpanda.gaming.matchmaker.frontend.model;

import java.util.List;


public class MatchResponse {

  private List<String> players;

  public MatchResponse(List<String> players) {
    this.players = players;
  }

  public MatchResponse() {
  }

  public List<String> getPlayers() {
    return players;
  }

  public void setPlayers(List<String> players) {
    this.players = players;
  }
}
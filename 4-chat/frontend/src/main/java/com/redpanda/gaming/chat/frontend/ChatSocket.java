package com.redpanda.gaming.chat.frontend;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;


@ServerEndpoint("/chat/{username}")
@ApplicationScoped
public class ChatSocket {

  Map<String, Session> sessions = new ConcurrentHashMap<>();

  @Channel("chats-out")
  Emitter<String> emitter;

  @OnOpen
  public void onOpen(Session session, @PathParam("username") String username) {
    sessions.put(username, session);
  }

  @OnClose
  public void onClose(Session session, @PathParam("username") String username) {
    sessions.remove(username);
    broadcast("User " + username + " left");
  }

  @OnError
  public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
    sessions.remove(username);
    broadcast("User " + username + " left on error: " + throwable);
  }

  @OnMessage
  public void onMessage(String message, @PathParam("username") String username) {
    String decoratedMessage;
    if (message.equalsIgnoreCase("_ready_")) {
      decoratedMessage = "User " + username + " joined";
    } else {
      decoratedMessage = ">> " + username + ": " + message;
    }
    emitter.send(decoratedMessage);
    System.out.println("Sent to Redpanda : " + decoratedMessage);
  }

  @Incoming("chats-in")
  public void onRedpandaMessage(String message) {
    System.out.println("Received from Redpanda: " + message);
    broadcast(">> " + message);
  }

  private void broadcast(String message) {
    sessions.values().forEach(s -> {
      s.getAsyncRemote().sendObject(message, result ->  {
        if (result.getException() != null) {
          System.out.println("Unable to send message: " + result.getException());
        }
      });
    });
  }

}
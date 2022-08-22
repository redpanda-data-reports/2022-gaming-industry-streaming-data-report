package com.redpanda.gaming.chat.moderator;

import io.smallrye.reactive.messaging.annotations.Blocking;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class ChatModerator {

  @Incoming("chats-out")
  @Outgoing("chats-in")
  @Blocking
  public String moderate(String message) throws InterruptedException {
    //You can write your moderation logic here. For example, check the message for profanity and mask those with *
    //Currently, this method returns the incoming message as it is.
    return message;
  }
}
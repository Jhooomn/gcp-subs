package com.mutualser.app.subscribers;

import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import java.util.function.Consumer;

public interface SubConsumer {

  String subscription();
  
  void subscribe();

  Consumer<BasicAcknowledgeablePubsubMessage> consumer();
}

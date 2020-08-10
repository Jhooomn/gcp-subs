package com.mutualser.app.subscribers;

import java.util.function.Consumer;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.common.collect.ImmutableMap;
import com.google.pubsub.v1.PubsubMessage;
import com.mutualser.app.exception.ExcepcionNegocio;
import com.mutualser.app.exception.ExcepcionTecnica;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SubConsumerDlq<T> implements SubConsumer {

  private final PubSubTemplate pubSubTemplate;

  private final String SUBSCRIPTION;

  private final String DLQ;

  public SubConsumerDlq(PubSubTemplate pubSubTemplate, String subscription, String dlq) {
    this.pubSubTemplate = pubSubTemplate;
    SUBSCRIPTION = subscription;
    DLQ = dlq;
  }

  @Override
  public String subscription() {
    return SUBSCRIPTION;
  }

  @Override
  public void subscribe() {
    pubSubTemplate.subscribe(this.subscription(), this.consumer());

  }

  public Consumer<BasicAcknowledgeablePubsubMessage> consumer() {
    return basicAcknowledgeablePubsubMessage -> consume(basicAcknowledgeablePubsubMessage);
  }

  private void consume(BasicAcknowledgeablePubsubMessage acknowledgeablePubsubMessage) {
    PubsubMessage message = acknowledgeablePubsubMessage.getPubsubMessage();
    String messageData = message.getData().toStringUtf8();
    try {
      procesar(convertir(messageData));
      acknowledgeablePubsubMessage.ack();
    } catch (ExcepcionTecnica et) {
      log.info("Error técnico: mensaje reintentable");
      acknowledgeablePubsubMessage.nack();
    } catch (ExcepcionNegocio en) {
      envioDlq(messageData, en, acknowledgeablePubsubMessage);
    }
  }

  private void envioDlq(String mensaje, ExcepcionNegocio en,
      BasicAcknowledgeablePubsubMessage acknowledgeable) {
    log.info("Error negocio: mensaje a dlq");
    this.pubSubTemplate.publish(DLQ, mensaje, ImmutableMap.of("ERROR", en.getMessage()));
    acknowledgeable.ack();

  }

  public abstract void procesar(T mensaje);

  public abstract T convertir(String mensaje);


}

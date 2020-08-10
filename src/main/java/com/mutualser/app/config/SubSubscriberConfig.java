package com.mutualser.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.mutualser.app.subscribers.SubConsumer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SubSubscriberConfig {

	@Autowired
	private final PubSubTemplate pubSubTemplate;
	
	@Autowired
    private final SubConsumer pubSubConsumer;
    
    public SubSubscriberConfig(PubSubTemplate pubSubTemplate, SubConsumer pubSubConsumer) {
        this.pubSubTemplate = pubSubTemplate;
        this.pubSubConsumer = pubSubConsumer;
    }
    
    /**
     * Se llama solo cuando la aplicación está lista para recibir solicitudes.
     * Pasa una implementación del consumidor al suscribirse a un tema Pub / Sub.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void subscribe() {
        log.info("Suscribiendo {} a {}", pubSubConsumer.getClass().getSimpleName(),
        		pubSubConsumer.subscription());
        
        pubSubTemplate.subscribe(pubSubConsumer.subscription(), pubSubConsumer.consumer());
    }
}

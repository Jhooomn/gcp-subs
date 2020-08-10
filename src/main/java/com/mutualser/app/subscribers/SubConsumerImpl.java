package com.mutualser.app.subscribers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mutualser.app.dto.EntityDTO;
import com.mutualser.app.exception.ExcepcionNegocio;
import com.mutualser.app.repositorio.EntityRepo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SubConsumerImpl extends SubConsumerDlq<EntityDTO> {
  
  @Autowired
  private ObjectMapper mapper;
  
  @Autowired
  EntityRepo repository;
  
  private static final String SUBSCRIPTION = "leerCoras";
  
  private static final String DLQ = "camel-poc-dlq";
  
  public SubConsumerImpl(PubSubTemplate pubSubTemplate) {
   super(pubSubTemplate, SUBSCRIPTION, DLQ);
  }

  @Override
  public EntityDTO convertir(String mensaje) {
    try {
      Map<String, Object> body = mapper.readValue(mensaje, new TypeReference<HashMap<String, Object>>() {});
      EntityDTO entity = mapper.convertValue(body.get("prescripcion"), EntityDTO.class);
      entity.setEntityId(String.valueOf(System.currentTimeMillis()));
      return entity;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new ExcepcionNegocio(e.getMessage());
    }
  }

  @Override
  public void procesar(EntityDTO entity) {
    log.info("guardando: ", entity.toString());
    repository.save(entity);
  } 
}

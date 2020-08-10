package com.mutualser.app.dto;

import java.util.HashMap;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Field;
import org.springframework.data.annotation.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "entities")
public class EntityDTO {

  @Id
  @Field(name = "entity_id")
  private String entityId;

  private String numero;
  private String fecha;
  private String hora;
  private String codigoEps;
  private String tipoTranscripcion;
  private String estado;
  private HashMap<?,?> body;

}

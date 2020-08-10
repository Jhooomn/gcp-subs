package com.mutualser.app.repositorio;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import com.mutualser.app.dto.EntityDTO;

public interface EntityRepo extends DatastoreRepository <EntityDTO, String>{

}

package com.mutualser.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.data.datastore.repository.config.EnableDatastoreRepositories;

@SpringBootApplication
@EnableDatastoreRepositories
public class GoogleSubscritionApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoogleSubscritionApplication.class, args);
	}

}

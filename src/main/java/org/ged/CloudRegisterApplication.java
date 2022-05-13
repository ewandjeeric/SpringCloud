package org.ged;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CloudRegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudRegisterApplication.class, args);
	}

}

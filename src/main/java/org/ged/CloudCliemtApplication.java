package org.ged;

import java.util.stream.Stream;

import org.ged.entities.Company;
import org.ged.repository.CompanyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudCliemtApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudCliemtApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CompanyRepository companyrepository, RepositoryRestConfiguration repositoryrestconfig) {
		repositoryrestconfig.exposeIdsFor(Company.class);
		return args -> {
			Stream.of("A", "B", "C").forEach(cn -> {
				companyrepository.save(new Company(null, cn, 100 + Math.random() * 900));
			});
			companyrepository.findAll().forEach(System.out::println);
		};
	}

}

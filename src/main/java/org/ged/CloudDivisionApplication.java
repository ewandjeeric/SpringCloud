package org.ged;

import org.ged.entities.Division;
import org.ged.repository.DivisionRepository;
import org.ged.rest.entities.Company;
import org.ged.rest.repo.CompanyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class CloudDivisionApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudDivisionApplication.class, args);
	}

	@Bean
	CommandLineRunner start(DivisionRepository divisionrepository, CompanyRepository companyrepository) {
		return arg -> {

			Company c1 = companyrepository.findCompanyById(1L);

			Division d1 = new Division(null, "Electronic", "Division de creation de piece electronique", c1.getId(),
					c1);
			Division d2 = new Division(null, "Ecran", "Division des ecrans", c1.getId(), c1);
			divisionrepository.save(d1);
			divisionrepository.save(d2);
		};
	}

	@Bean
	public HttpTraceRepository httpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	}

}

package org.ged.rest.repo;

import org.ged.rest.entities.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "COMPANY-SERVICE", url = "${client.post.baseUrl}", configuration = Company.class)
public interface CompanyRepository {

	@GetMapping("/companies/{id}")
	public Company findCompanyById(@PathVariable(name = "id") Long id);

}

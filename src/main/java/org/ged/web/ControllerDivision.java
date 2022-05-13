package org.ged.web;

import org.ged.entities.Division;
import org.ged.repository.DivisionRepository;
import org.ged.rest.repo.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerDivision {
	@Autowired
	DivisionRepository drepo;
	@Autowired
	CompanyRepository crepo;

	@GetMapping("/ddivision/{id}")
	@PostAuthorize("hasAuthority('USER')")
	public Division division(@PathVariable(name = "id") Long id) {
		Division d = drepo.findById(id).orElse(null);
		d.setCompany(crepo.findCompanyById(d.getCompanyID()));
		return d;
	}

}

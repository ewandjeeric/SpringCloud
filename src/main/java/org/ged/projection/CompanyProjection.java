package org.ged.projection;

import org.ged.entities.Company;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "dcompany", types = Company.class)
public interface CompanyProjection {
	public Long getId();

	public String getName();

	public double getPrice();
}

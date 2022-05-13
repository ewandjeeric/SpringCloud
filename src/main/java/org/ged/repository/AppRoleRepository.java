package org.ged.repository;

import org.ged.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
	public AppRole findByRole(String role);

}

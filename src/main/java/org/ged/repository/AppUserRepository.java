package org.ged.repository;

import org.ged.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	@Query("select u from AppUser u where u.username = ?1")
	public AppUser findByUsername(String username);
}

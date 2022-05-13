package org.ged.repository;

import org.ged.entities.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource
public interface DivisionRepository extends JpaRepository<Division, Long> {

}

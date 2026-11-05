
package com.nys.nys_backend.repository;

import com.nys.nys_backend.entity.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetenceRepository
        extends JpaRepository<Competence, Long> {
}

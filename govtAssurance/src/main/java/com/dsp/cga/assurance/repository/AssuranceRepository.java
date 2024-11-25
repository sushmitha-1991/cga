package com.dsp.cga.assurance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.entity.Assurance;
@Repository

public interface AssuranceRepository extends JpaRepository<Assurance, Long> {

	@Query("SELECT a FROM Assurance a WHERE a.rajyaSabhaSessionNo IS NOT NULL AND a.lokSabhaSessionNo IS NULL")
	List<Assurance> findAssuranceWithRajyaSabhaSessionNotNullAndLokSabhaSessionNull();


	//Optional<GovtAssurance> findActiveAndNonDeletedGovtAssurance();

}

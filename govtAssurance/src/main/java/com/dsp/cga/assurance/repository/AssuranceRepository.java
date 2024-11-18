package com.dsp.cga.assurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dsp.cga.assurance.entity.Assurance;


public interface AssuranceRepository extends JpaRepository<Assurance, Long> {

	//Optional<GovtAssurance> findActiveAndNonDeletedGovtAssurance();

}

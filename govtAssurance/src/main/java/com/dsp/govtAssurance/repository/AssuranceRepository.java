package com.dsp.govtAssurance.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.dsp.govtAssurance.entity.Assurance;


public interface AssuranceRepository extends JpaRepository<Assurance, Long> {

	//Optional<GovtAssurance> findActiveAndNonDeletedGovtAssurance();

}

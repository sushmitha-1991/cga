package com.dsp.cga.assurance.clubbedassurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.clubbedassurance.entity.ClubbedAssurance;
@Repository
public interface ClubbedAssuranceRepository extends JpaRepository<ClubbedAssurance, Long> {

	Optional<ClubbedAssurance> findByAssuranceId(Long assuranceId);

	List<ClubbedAssurance> findClubbedAssuranceByAssuranceId(Long assuranceId);

}

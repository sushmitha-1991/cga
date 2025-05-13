package com.dsp.cga.assurance.clubbedassurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.clubbedassurance.entity.ClubbedAssurance;
@Repository
public interface ClubbedAssuranceRepository extends JpaRepository<ClubbedAssurance, Long>, JpaSpecificationExecutor<ClubbedAssurance> {

	Optional<ClubbedAssurance> findByAssuranceId(Long assuranceId);

	List<ClubbedAssurance> findClubbedAssuranceByAssuranceId(Long assuranceId);
	
//	 @Query(value = "SELECT * FROM clubbed_assurance WHERE assurance_id = :assuranceId", nativeQuery = true)
//	    List<ClubbedAssurance> findAllByAssuranceId(@Param("assuranceId") Long assuranceId);

	List<ClubbedAssurance> findAllByAssuranceId(Long id);

}

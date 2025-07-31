package com.dsp.cga.assurance.clubbedassurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.clubbedassurance.entity.ClubbedAssurance;
import com.dsp.cga.assurance.extensionrequest.entity.ExtensionRequest;
@Repository
public interface ClubbedAssuranceRepository extends JpaRepository<ClubbedAssurance, Long>, JpaSpecificationExecutor<ClubbedAssurance> {

	Optional<ClubbedAssurance> findByAssuranceId(Long assuranceId);

	List<ClubbedAssurance> findClubbedAssuranceByAssuranceId(Long assuranceId);
	
//	 @Query(value = "SELECT * FROM clubbed_assurance WHERE assurance_id = :assuranceId", nativeQuery = true)
//	    List<ClubbedAssurance> findAllByAssuranceId(@Param("assuranceId") Long assuranceId);

	List<ClubbedAssurance> findAllByAssuranceId(Long id);

	@Query(value = """
		    SELECT COUNT(*) 
		    FROM cga.clubbed_assurances ca
		    JOIN cga.assurance a ON ca.assurance_id = a.id
		    WHERE a.ministry_id = :ministryId
		      AND a.rajya_sabha_session_no IS NOT NULL
		    """, nativeQuery = true)
		Long countByMinistryIdAndRajyaSabha(@Param("ministryId") Long ministryId);

		@Query(value = """
		    SELECT COUNT(*) 
		    FROM cga.clubbed_assurances ca
		    JOIN cga.assurance a ON ca.assurance_id = a.id
		    WHERE a.ministry_id = :ministryId
		      AND a.lok_sabha_session_no IS NOT NULL
		    """, nativeQuery = true)
		Long countByMinistryIdAndLokSabha(@Param("ministryId") Long ministryId);

		@Query(value = """
			    SELECT COUNT(*) 
			    FROM cga.clubbed_assurances ca
			    JOIN cga.assurance a ON ca.assurance_id = a.id
			    WHERE a.ministry_id = :ministryId
			    """, nativeQuery = true)
			Long countByMinistryId(@Param("ministryId") Long ministryId);

  
		@Query("""
			    FROM ClubbedAssurance e
			    WHERE e.isActive = true 
			      AND e.assuranceId = :assuranceId 
			      AND (
			          (e.fromMinistry = true AND e.statusForCga = 'Accepted') 
			          OR 
			          (e.fromMinistry = false)
			      )
			""")		
		ClubbedAssurance getAcceptedRequest(@Param("assuranceId") Long assuranceId);		

}

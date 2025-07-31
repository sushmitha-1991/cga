package com.dsp.cga.assurance.droppingrequest.repository;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.droppingrequest.entity.DroppingRequest;

@Repository
public interface DroppingRequestRepository extends JpaRepository<DroppingRequest, Long>, JpaSpecificationExecutor
<DroppingRequest> {

	Optional<DroppingRequest> findByAssuranceId(Long id);

	@Query(value = """
		    SELECT COUNT(*) FROM cga.dropping_requests d
		    JOIN cga.assurance a ON d.assurance_id = a.id
		    WHERE d.assurance_id = :assuranceId
		    """, nativeQuery = true)
		Long countByAssuranceId(@Param("assuranceId") Long assuranceId);
		
	
	@Query(value = """
		    SELECT COUNT(*) FROM cga.dropping_requests d
		    JOIN cga.assurance a ON d.assurance_id = a.id
		    WHERE a.ministry_id = :ministryId
		      AND a.rajya_sabha_session_no IS NOT NULL
		    """, nativeQuery = true)
		Long countByMinistryIdAndRajyaSabha(@Param("ministryId") Long ministryId);

		@Query(value = """
		    SELECT COUNT(*) FROM cga.dropping_requests d
		    JOIN cga.assurance a ON d.assurance_id = a.id
		    WHERE a.ministry_id = :ministryId
		      AND a.lok_sabha_session_no IS NOT NULL
		    """, nativeQuery = true)
		Long countByMinistryIdAndLokSabha(@Param("ministryId") Long ministryId);


		@Query(value = """
			    SELECT COUNT(*) 
			    FROM cga.dropping_requests dr
			    JOIN cga.assurance a ON dr.assurance_id = a.id
			    WHERE a.ministry_id = :ministryId
			    """, nativeQuery = true)
			Long countByMinistryId(@Param("ministryId") Long ministryId);



		@Query("""
			    FROM DroppingRequest e
			    WHERE e.isActive = true 
			      AND e.assuranceId = :assuranceId 
			      AND (
			          (e.fromMinistry = true AND e.statusForCga = 'Accepted') 
			          OR 
			          (e.fromMinistry = false)
			      )
			""")	
		DroppingRequest getAcceptedDroppingRequest(@Param("assuranceId") Long assuranceId);	




}

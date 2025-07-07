package com.dsp.cga.assurance.implementationrequest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;

@Repository
public interface ImplementationRequestRepository extends JpaRepository<ImplementationRequest, Long>, JpaSpecificationExecutor<ImplementationRequest> {

	Optional<ImplementationRequest> findByAssuranceId(Long assuranceId);


	@Query(value = """
		    SELECT COUNT(*) FROM cga.implementation_requests i
		    JOIN cga.assurance a ON i.assurance_id = a.id
		    WHERE a.ministry_id = :ministryId
		      AND a.rajya_sabha_session_no IS NOT NULL
		    """, nativeQuery = true)
		Long countByMinistryIdAndRajyaSabha(@Param("ministryId") Long ministryId);

		@Query(value = """
		    SELECT COUNT(*) FROM cga.implementation_requests i
		    JOIN cga.assurance a ON i.assurance_id = a.id
		    WHERE a.ministry_id = :ministryId
		      AND a.lok_sabha_session_no IS NOT NULL
		    """, nativeQuery = true)
		Long countByMinistryIdAndLokSabha(@Param("ministryId") Long ministryId);


		@Query(value = """
			    SELECT COUNT(*) 
			    FROM cga.implementation_requests i
			    JOIN cga.assurance a ON i.assurance_id = a.id
			    WHERE a.ministry_id = :ministryId
			    """, nativeQuery = true)
			Long countByMinistryId(@Param("ministryId") Long ministryId);


		List<ImplementationRequest> findByStatusForMinistryAndReturnFrom(String status, String returnFrom);
		
		List<ImplementationRequest> findByStatusForMopa(String status);



		List<ImplementationRequest> findAllByOrderByUpdatedAtDesc();





	

}

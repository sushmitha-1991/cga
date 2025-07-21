package com.dsp.cga.assurance.extensionrequest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.entity.ExtensionRequest;
@Repository
public interface ExtensionRequestRepository extends JpaRepository<ExtensionRequest, Long>, JpaSpecificationExecutor<ExtensionRequest> {

	@Query("SELECT e FROM ExtensionRequest e WHERE e.assuranceId = :assuranceId")
	List<ExtensionRequest> findByAssuranceId(@Param("assuranceId") Long assuranceId);

	Optional<ExtensionRequest> findByAssuranceIdAndExtensionNo(Long assuranceId, String extensionNo);



	@Query(value = """
		    SELECT COUNT(*) FROM cga.extension_requests e
		    JOIN cga.assurance a ON e.assurance_id = a.id
		    WHERE a.ministry_id = :ministryId
		      AND a.rajya_sabha_session_no IS NOT NULL
		    """, nativeQuery = true)
		Long countByMinistryIdAndRajyaSabha(@Param("ministryId") Long ministryId);

		@Query(value = """
		    SELECT COUNT(*) FROM cga.extension_requests e
		    JOIN cga.assurance a ON e.assurance_id = a.id
		    WHERE a.ministry_id = :ministryId
		      AND a.lok_sabha_session_no IS NOT NULL
		    """, nativeQuery = true)
		Long countByMinistryIdAndLokSabha(@Param("ministryId") Long ministryId);

		@Query(value = """
			    SELECT COUNT(*) FROM (
			        SELECT e.id
			        FROM cga.extension_requests e
			        JOIN cga.assurance a ON e.assurance_id = a.id
			        WHERE a.ministry_id = :ministryId
			    ) AS sub
			    """, nativeQuery = true)
			Long countByMinistryId(@Param("ministryId") Long ministryId);



		@Query(value = """
			    SELECT er.* FROM cga.extension_requests er 
			    left join cga.assurance a on er.assurance_id = er.id
			    order by er.ext_requested_till_date desc 
			    limit 1
			    
			    """, nativeQuery = true)
			ExtensionRequest getLatest(@Param("assuranceId") Long assuranceId);
	 

}

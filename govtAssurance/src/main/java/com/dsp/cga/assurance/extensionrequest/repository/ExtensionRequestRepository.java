package com.dsp.cga.assurance.extensionrequest.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.entity.ExtensionRequest;

import jakarta.transaction.Transactional;
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
		      AND e.is_active = true
		    """, nativeQuery = true)
		Long countByMinistryIdAndRajyaSabha(@Param("ministryId") Long ministryId);

		@Query(value = """
		    SELECT COUNT(*) FROM cga.extension_requests e
		    JOIN cga.assurance a ON e.assurance_id = a.id
		    WHERE a.ministry_id = :ministryId
		      AND a.lok_sabha_session_no IS NOT NULL
		      AND e.is_active = true
		    """, nativeQuery = true)
		Long countByMinistryIdAndLokSabha(@Param("ministryId") Long ministryId);

		@Query(value = """
			    SELECT COUNT(*) FROM (
			        SELECT e.id
			        FROM cga.extension_requests e
			        JOIN cga.assurance a ON e.assurance_id = a.id
			        WHERE a.ministry_id = :ministryId 
			        	and e.is_active = true 
			    ) AS sub
			    """, nativeQuery = true)
			Long countByMinistryId(@Param("ministryId") Long ministryId);



		@Query(value = """
			    SELECT er.* FROM cga.extension_requests er 
			    left join cga.assurance a on er.assurance_id = er.id
			    where er.is_active = true
			    order by er.ext_requested_till_date desc 
			    limit 1
			    
			    """, nativeQuery = true)
			ExtensionRequest getLatest(@Param("assuranceId") Long assuranceId);
	 
		
		@Query("""
			    FROM ExtensionRequest e
			    WHERE e.isActive = true 
			      AND e.assuranceId = :assuranceId 
			      AND (
			          (e.fromMinistry = true AND e.statusForCga = 'Accepted') 
			          OR 
			          (e.fromMinistry = false)
			      )
			""")		
		List<ExtensionRequest> getAcceptedRequests(@Param("assuranceId") Long assuranceId);		
		
		@Query(value = """
			    SELECT er.assurance_id FROM cga.extension_requests er 
			    where er.is_active = true
			    and assurance_id = :assuranceId			    
			    """, nativeQuery = true)		
		List<Long> getExtensionIdList(@Param("assuranceId") Long assuranceId);

		@Modifying
		@Transactional
		@Query(value = "UPDATE cga.extension_requests set is_active = false, is_deleted = true where id = :id", 
						nativeQuery = true)
		void deleteExtenstion(@Param("id") Long id);
}

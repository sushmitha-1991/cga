package com.dsp.cga.assurance.otherRequests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.otherRequests.entity.OtherRequests;

@Repository
public interface OtherRequestRepository extends JpaRepository<OtherRequests, Long>, JpaSpecificationExecutor<OtherRequests> {

	@Query(value = """
		    SELECT COUNT(*) 
		    FROM cga.other_requests ca
		    JOIN cga.assurance a ON ca.assurance_id = a.id
		    WHERE a.ministry_id = :ministryId
		      AND a.rajya_sabha_session_no IS NOT NULL
		    """, nativeQuery = true)
		Long countByMinistryIdAndRajyaSabha(@Param("ministryId") Long ministryId);
	
	@Query(value = """
		    SELECT COUNT(*) 
		    FROM cga.other_requests ca
		    JOIN cga.assurance a ON ca.assurance_id = a.id
		    WHERE a.ministry_id = :ministryId
		      AND a.lok_sabha_session_no IS NOT NULL
		    """, nativeQuery = true)
		Long countByMinistryIdAndLokSabha(@Param("ministryId") Long ministryId);



}

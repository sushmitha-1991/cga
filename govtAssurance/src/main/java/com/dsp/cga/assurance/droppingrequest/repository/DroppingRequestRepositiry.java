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
public interface DroppingRequestRepositiry extends JpaRepository<DroppingRequest, Long>, JpaSpecificationExecutor
<DroppingRequest> {

	Optional<DroppingRequest> findByAssuranceId(Long id);

//	@Query("SELECT COUNT(a) FROM AssuranceDropping a WHERE " +
//	           "(:ministryId IS NULL OR a.ministry.id = :ministryId) AND " +
//	           "(:houseId IS NULL OR a.houseTypeId = :houseId) AND " +
//	           "(:sessionNo IS NULL OR " +
//	           "( :houseId = 1 AND a.rajyaSabhaSessionNo = :sessionNo ) OR " +
//	           "( :houseId = 2 AND a.lokSabhaSessionNo = :sessionNo ))")
//	    Long countByFilters(@Param("sessionNo") Long sessionNo,
//	                        @Param("ministryId") Long ministryId,
//	                        @Param("houseId") Long houseId);



}

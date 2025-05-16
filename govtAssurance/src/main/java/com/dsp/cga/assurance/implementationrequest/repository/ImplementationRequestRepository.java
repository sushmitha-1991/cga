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

//	@Query("SELECT COUNT(i) FROM ImplementationRequest i WHERE " +
//	           "(:ministryId IS NULL OR i.ministry.id = :ministryId) AND " +
//	           "(:houseId IS NULL OR i.houseTypeId = :houseId) AND " +
//	           "(:sessionNo IS NULL OR " +
//	           "( :houseId = 1 AND i.rajyaSabhaSessionNo = :sessionNo ) OR " +
//	           "( :houseId = 2 AND i.lokSabhaSessionNo = :sessionNo ))")
//	    Long countByFilters(@Param("sessionNo") Long sessionNo,
//	                        @Param("ministryId") Long ministryId,
//	                        @Param("houseId") Long houseId);


	

}

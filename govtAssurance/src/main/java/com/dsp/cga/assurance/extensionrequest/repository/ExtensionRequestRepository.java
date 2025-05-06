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

	

}

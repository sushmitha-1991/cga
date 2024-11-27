package com.dsp.cga.assurance.implementationrequest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;

@Repository
public interface ImplementationRequestRepository extends JpaRepository<ImplementationRequest, Long> {

	Optional<ImplementationRequest> findByAssuranceId(Long assuranceId);


}

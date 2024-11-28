package com.dsp.cga.assurance.extensionrequest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.entity.ExtensionRequest;
@Repository
public interface ExtensionRequestRepository extends JpaRepository<ExtensionRequest, Long> {

	Optional<ExtensionRequest> findByAssuranceId(Long id);

}

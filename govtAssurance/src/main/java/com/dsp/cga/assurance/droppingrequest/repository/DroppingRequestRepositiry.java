package com.dsp.cga.assurance.droppingrequest.repository;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.droppingrequest.entity.DroppingRequest;

@Repository
public interface DroppingRequestRepositiry extends JpaRepository<DroppingRequest, Long>, JpaSpecificationExecutor
<DroppingRequest> {

	Optional<DroppingRequest> findByAssuranceId(Long id);



}

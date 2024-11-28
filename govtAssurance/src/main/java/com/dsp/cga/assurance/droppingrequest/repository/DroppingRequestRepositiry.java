package com.dsp.cga.assurance.droppingrequest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.droppingrequest.entity.DroppingRequest;

@Repository
public interface DroppingRequestRepositiry extends JpaRepository<DroppingRequest, Long> {

	Optional<DroppingRequest> findByAssuranceId(Long id);

}

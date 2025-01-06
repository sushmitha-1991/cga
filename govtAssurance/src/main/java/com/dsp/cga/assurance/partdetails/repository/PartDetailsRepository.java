package com.dsp.cga.assurance.partdetails.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.partdetails.entity.PartDetails;


@Repository
public interface PartDetailsRepository extends JpaRepository<PartDetails, Long> {

	Optional<PartDetails> findByAssuranceId(Long assuranceId);

}

package com.dsp.cga.assurance.supplementry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;

@Repository
public interface SupplementaryRepository extends JpaRepository<Supplementary, Long> {

	Optional<Supplementary> findByAssuranceId(Long assuranceId);
	
	


}

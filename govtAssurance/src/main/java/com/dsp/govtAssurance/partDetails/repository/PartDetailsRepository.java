package com.dsp.govtAssurance.partDetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.govtAssurance.partDetails.entity.PartDetails;

@Repository
public interface PartDetailsRepository extends JpaRepository<PartDetails, Long> {

}

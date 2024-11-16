package com.dsp.govtAssurance.supplementry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.govtAssurance.supplementry.entity.Supplementary;

@Repository
public interface SupplementaryRepository extends JpaRepository<Supplementary, Long> {

}

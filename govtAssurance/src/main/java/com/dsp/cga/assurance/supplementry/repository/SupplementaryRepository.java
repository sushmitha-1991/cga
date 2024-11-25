package com.dsp.cga.assurance.supplementry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.supplementry.entity.Supplementary;

@Repository
public interface SupplementaryRepository extends JpaRepository<Supplementary, Long> {

}

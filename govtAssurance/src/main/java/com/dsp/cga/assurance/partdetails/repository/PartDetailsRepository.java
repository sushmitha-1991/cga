package com.dsp.cga.assurance.partdetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.partdetails.entity.PartDetails;


@Repository
public interface PartDetailsRepository extends JpaRepository<PartDetails, Long> {

}

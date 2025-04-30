package com.dsp.cga.assurance.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.entity.Ministries;


@Repository
public interface MinistryRepository extends JpaRepository<Ministries, Long> {

	List<Ministries> findAllByOrderByMinistryNameAsc();

	Ministries findByMinistryCode(Integer ministryCode);

}

package com.dsp.cga.assurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.entity.MastersRajyaSabha;





@Repository
public interface MastersRSSRepository extends JpaRepository<MastersRajyaSabha,Long>{
     @Query(value = "SELECT id FROM master.rajya_sabha_session_master ORDER BY created_at DESC LIMIT 1", 
     nativeQuery = true)
    Long findLatestByCreatedAt();
     
     @Query(value = "SELECT session_number FROM master.rajya_sabha_session_master WHERE id = :id", nativeQuery = true)
     String findSessionNumberById(@Param("id") Long id);
     
}

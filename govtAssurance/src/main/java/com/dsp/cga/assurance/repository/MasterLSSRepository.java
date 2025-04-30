package com.dsp.cga.assurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.entity.MastersLokSabhaSession;


@Repository
public interface MasterLSSRepository extends JpaRepository<MastersLokSabhaSession,Long>{
@Query(value = "SELECT id FROM master.lok_sabha_session_masters ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Long findLatestByCreatedAt();

@Query("SELECT l.lokSabhaNumber FROM MastersLokSabha l WHERE l.id = :lokSabhaId")
String findLokSabhaNumberById(@Param("lokSabhaId") Long lokSabhaId);

@Query("SELECT s.sessionNumber FROM MastersLokSabhaSession s WHERE s.id = :sessionId")
String findSessionNumberById(@Param("sessionId") Long sessionId);


}

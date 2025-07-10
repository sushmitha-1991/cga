package com.dsp.cga.repository;

import com.dsp.cga.entity.Houses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HousesRepository extends JpaRepository<Houses, Long> {

	@Query("SELECT h.houseName FROM Houses h WHERE h.id = :id")
	String findHouseNameById(@Param("id") Long id); 
}

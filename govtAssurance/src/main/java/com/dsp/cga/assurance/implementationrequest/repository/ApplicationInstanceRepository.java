package com.dsp.cga.assurance.implementationrequest.repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.implementationrequest.entity.ApplicationInstance;



@Repository
public interface ApplicationInstanceRepository extends JpaRepository<ApplicationInstance, Long> {

	@Query("SELECT ai FROM ApplicationInstance ai WHERE " + "ai.lokSabhaSession = :lokSabhaSession AND "
			+ "ai.lokSabhaSessionNumber = :lokSabhaSessionNumber AND " + "ai.applicationId = :applicationId AND "
			+ "ai.draft = true")
	List<ApplicationInstance> findByLokSabhaFilters(@Param("lokSabhaSession") String lokSabhaSession,
			@Param("lokSabhaSessionNumber") String lokSabhaSessionNumber, @Param("applicationId") Long applicationId);

	@Query("SELECT ai FROM ApplicationInstance ai WHERE " + "ai.rajyaSabhaSession = :rajyaSabhaSession AND "
			+ "ai.applicationId = :applicationId AND " + "ai.draft = true")
	List<ApplicationInstance> findByRajyaSabhaFilters(@Param("rajyaSabhaSession") String rajyaSabhaSession,
			@Param("applicationId") Long applicationId);

}

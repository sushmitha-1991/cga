package com.dsp.cga.assurance.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.entity.Assurance;

@Repository

public interface AssuranceRepository extends JpaRepository<Assurance, Long> {

	@Query("SELECT a FROM Assurance a WHERE a.rajyaSabhaSessionNo IS NOT NULL AND a.lokSabhaSessionNo IS NULL")
	List<Assurance> findAssuranceWithRajyaSabhaSessionNotNullAndLokSabhaSessionNull();

//	@Query("SELECT a FROM Assurance a " +
//		       "WHERE a.rajyaSabhaSessionNo IS NOT NULL " +
//		       "AND a.lokSabhaSessionNo IS NULL " +
//		       "AND (:fromDate IS NULL OR a.assuranceDate >= :fromDate) " +
//		       "AND (:toDate IS NULL OR a.assuranceDate <= :toDate) " +
//		       "AND (:ministryId IS NULL OR a.ministryId = :ministryId) " +
//		       "AND (:memberId IS NULL OR a.memberId = :memberId)")
//		List<Assurance> findFilteredRajyaSabhaAssurances(
//		        @Param("fromDate") LocalDate fromDate,
//		        @Param("toDate") LocalDate toDate,
//		        @Param("ministryId") Long ministryId,
//		        @Param("memberId") Long memberId);

	@Query("SELECT a.assuranceNo FROM Assurance a WHERE a.id = :assuranceId")
	String findAssuranceNoById(@Param("assuranceId") Long assuranceId);
	// Optional<GovtAssurance> findActiveAndNonDeletedGovtAssurance();

	@Query("SELECT a FROM Assurance a WHERE a.rajyaSabhaSessionNo IS NOT NULL "
			+ "AND (a.lokSabhaSessionNo IS NULL) " + "AND (a.assuranceDate >= :fromDate or :fromDate is null)"
			+ "AND (a.assuranceDate <= :toDate or :toDate is null)" + "AND (a.ministryId = :ministryId or :ministryId is null) " + "AND (a.memberId = :memberId or :memberId is null)")
	List<Assurance> findFilteredAssurances(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
			@Param("ministryId") Long ministryId, @Param("memberId") Long memberId);

	@Query("SELECT a FROM Assurance a WHERE a.rajyaSabhaSessionNo IS NULL AND a.lokSabhaSessionNo IS NOT NULL")
	List<Assurance> findAssuranceWithRajyaSabhaSessionNullAndLokSabhaSessionNotNull();

	@Query("SELECT a FROM Assurance a WHERE a.rajyaSabhaSessionNo IS NULL "
			+ "AND (a.lokSabhaSessionNo IS NOT NULL) " + "AND (a.assuranceDate >= :fromDate or :fromDate is null)"
			+ "AND (a.assuranceDate <= :toDate or :toDate is null)" + "AND (a.ministryId = :ministryId or :ministryId is null) " + "AND (a.memberId = :memberId or :memberId is null)")
	List<Assurance> findFilteredAssurancesLs(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
			@Param("ministryId") Long ministryId, @Param("memberId") Long memberId);  
	
	
//	 @Query ("SELECT a FROM Assurance a WHERE a.rajyaSabhaSessionNo is not NULL"
//	 		+ " AND a.lokSabhaSessionNo IS null"
//	 		+ " AND (a.assuranceDate >= :fromDate OR :fromDate is null)"
//	 		+ " AND (a.assuranceDate <= :toDate OR :toDate is null)" 
//            + " AND (:ministryId IS NULL OR a.ministryId = :ministryId) " 
//            + " AND (:memberId IS NULL OR a.memberId = :memberId)")
//    
//	 List<Assurance> findFilteredAssurancesLs(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
//				@Param("ministryId") Long ministryId, @Param("memberId") Long memberId); 


	
	

}


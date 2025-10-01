package com.dsp.cga.assurance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.entity.Assurance;

@Repository

public interface AssuranceRepository extends JpaRepository<Assurance, Long>, AssuranceRepositoryCustom , JpaSpecificationExecutor<Assurance>{

//	@Query("SELECT a FROM Assurance a " +
//		       "WHERE a.rajyaSabhaSessionNo IS NOT NULL AND a.lokSabhaSessionNo IS NULL " +
//		       "ORDER BY a.createdAt DESC")
//		List<Assurance> findAssuranceWithRajyaSabhaSessionNotNullAndLokSabhaSessionNull();
	
	@Query("SELECT a FROM Assurance a " +
		       "WHERE a.rajyaSabhaSessionNo IS NOT NULL AND a.lokSabhaSessionNo IS NULL " +
		       "ORDER BY a.createdAt DESC")
		List<Assurance> findAssuranceWithRajyaSabhaSessionNotNullAndLokSabhaSessionNull();
	
//  *************************  adding pagination to the above query *******************************
	@Query("SELECT a FROM Assurance a " +
		       "WHERE a.rajyaSabhaSessionNo IS NOT NULL AND a.lokSabhaSessionNo IS NULL " 
		       +"AND (:sessionNo IS NULL or a.rajyaSabhaSessionNo =:sessionNo)"
				+"AND (:ministryId is null or a.ministryId = :ministryId)"
			    +"AND (:memberId is null or a.memberId = :memberId)"
			    +"AND (CAST(:fromDate AS date) is null or a.assuranceDate >= :fromDate)"
			    +"AND (CAST(:toDate AS date) is null or a.assuranceDate <= :toDate)"
		       +"ORDER BY a.createdAt DESC")
	Page<Assurance> findAssuranceWithRajyaSabhaSessionNotNullAndLokSabhaSessionNullV2(Pageable pageable,@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
			@Param("ministryId") Long ministryId, @Param("memberId") Long memberId,@Param("sessionNo") Long sessionNo);

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

	@Query("SELECT a FROM Assurance a " +
		       "WHERE a.rajyaSabhaSessionNo IS NULL AND a.lokSabhaSessionNo IS NOT NULL " +
		       "ORDER BY a.createdAt DESC")
		List<Assurance> findAssuranceWithRajyaSabhaSessionNullAndLokSabhaSessionNotNull();
	
//  *************************  adding pagination to the above query *******************************
	@Query("SELECT a FROM Assurance a " +
		       "WHERE a.rajyaSabhaSessionNo IS NULL AND a.lokSabhaSessionNo IS NOT NULL " 
			+"AND (:lokSabhaNo IS NULL or a.lokSabhaNo =:lokSabhaNo)"
		    +"AND (:sessionNo IS NULL or a.lokSabhaSessionNo =:sessionNo)"
			+"AND (:ministryId is null or a.ministryId = :ministryId)"
		    +"AND (:memberId is null or a.memberId = :memberId)"
		    +"AND (CAST(:fromDate AS date) is null or a.assuranceDate >= :fromDate)"
		    +"AND (CAST(:toDate AS date) is null or a.assuranceDate <= :toDate)"
		    +"ORDER BY a.createdAt DESC")
	Page<Assurance> findAssuranceWithRajyaSabhaSessionNullAndLokSabhaSessionNotNullV2(Pageable pageable,@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
			@Param("ministryId") Long ministryId, @Param("memberId") Long memberId,@Param("lokSabhaNo") Long lokSabhaNo,@Param("sessionNo") Long sessionNo);

	@Query("SELECT a FROM Assurance a WHERE a.rajyaSabhaSessionNo IS NULL "
			+ "AND (a.lokSabhaSessionNo IS NOT NULL) " + "AND (a.assuranceDate >= :fromDate or :fromDate is null)"
			+ "AND (a.assuranceDate <= :toDate or :toDate is null)" + "AND (a.ministryId = :ministryId or :ministryId is null) " + "AND (a.memberId = :memberId or :memberId is null)")
	List<Assurance> findFilteredAssurancesLs(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
			@Param("ministryId") Long ministryId, @Param("memberId") Long memberId);



	Optional<Assurance> findByAssuranceNo(String assuranceNo);


	@Query("SELECT COUNT(a) FROM Assurance a WHERE a.ministryId = :ministryId AND a.lokSabhaSessionNo IS NOT NULL AND a.dueDateOfImplementation IS NOT NULL")

	Long countByMinistryIdWithDueDate(Long ministryId);



	List<Assurance> findByDueDateOfImplementationNotNull();







	Long countByRajyaSabhaSessionNo(Long rajyaSabhaSessionId);



//	Optional<Assurance> findByLokSabhaNoAndLokSabhaSessionNoAndSourceIdAndQuestionNoAndAssuranceTypeId(Long lokSabhaNo,
//			Long lokSabhaSessionNo, Long sourceId, String questionNo, Long assuranceTypeId);



	Optional<Assurance> findByLokSabhaNoAndLokSabhaSessionNoAndSourceIdAndQuestionNo(Long lokSabhaNo,
			Long lokSabhaSessionNo, Long sourceId, String questionNo);



	Optional<Assurance> findByQuestionNo(String questionNo);

	Optional<List<Assurance>> findAllByQuestionNo(String questionNo);

//	Optional<Assurance> findByLokSabhaNoAndLokSabhaSessionNoAndSourceIdAndQuestionNoAndCategoryCode(Long lokSabhaNo,
//			Long lokSabhaSessionNo, Long sourceId, String questionNo, String categoryCode);



	//List<Assurance> findAll(Object example);

	
	
	
//	 @Query ("SELECT a FROM Assurance a WHERE a.rajyaSabhaSessionNo is not NULL"
//	 		+ " AND a.lokSabhaSessionNo IS null"
//	 		+ " AND (a.assuranceDate >= :fromDate OR :fromDate is null)"
//	 		+ " AND (a.assuranceDate <= :toDate OR :toDate is null)" 
//            + " AND (:ministryId IS NULL OR a.ministryId = :ministryId) " 
//            + " AND (:memberId IS NULL OR a.memberId = :memberId)")
//    
//	 List<Assurance> findFilteredAssurancesLs(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
//				@Param("ministryId") Long ministryId, @Param("memberId") Long memberId); 


	@Query(value = """
			select
			case
				when ca.house_id = 1 then rs.session_number
				when ca.house_id = 2 then ls.session_number
			end as sessionNo,
			case
				when ca.house_id = 1 then rs.id
				when ca.house_id = 2 then ls.id
			end as sessionNoId,
			lsm.lok_sabha_number as lokSabhaNo,
			lsm.id as lokSabhaNoId,
			count(ca.id) as count
			from cga.assurance ca
			left join master.rajya_sabha_session_master rs on ca.rajya_sabha_session_no = rs.id and ca.house_id = 1
			left join master.lok_sabha_session_masters ls on ca.lok_sabha_session_no = ls.id and ca.house_id = 2
			left join master.lok_sabha_masters lsm on ca.lok_sabha_no =lsm.id and ca.house_id = 2
			left join master.ministries mm on mm.id=ca.ministry_id
			left join members.personal_details mpd on mpd.id=ca.member_id
			left join master.codes mc on mc.id=ca.status_id
			left join master.codes mc1 on mc1.id=ca.category_id
			where ca.house_id=:houseId
			and(
			(:houseId =2 and ca.lok_sabha_session_no is not null
			and (:lokSabhaNoId is null or ca.lok_sabha_no=:lokSabhaNoId)
			and (:sessionNoId is null or ca.lok_sabha_session_no=:sessionNoId))
			or
			(:houseId =1 and ca.rajya_sabha_session_no is not null
			and (:sessionNoId is null or ca.rajya_sabha_session_no=:sessionNoId))
			)
			and (:ministryId is null or ca.ministry_id=:ministryId)
			and (:memberId is null or ca.member_id=:memberId)
			and (:statusId is null or ca.status_id=:statusId)
			and (:categoryId is null or ca.category_id=:categoryId)
			group by
			case
				when ca.house_id = 1 then rs.session_number
				when ca.house_id = 2 then ls.session_number
			end,
			case
				when ca.house_id = 1 then rs.id
				when ca.house_id = 2 then ls.id
			end,
			lokSabhaNo,lokSabhaNoId
			""", nativeQuery = true)
	List<Object[]> getSessionWiseAssurancesCount(@Param("houseId") Long houseId,@Param("lokSabhaNoId") Long lokSabhaNoId, 
			@Param("sessionNoId") Long sessionNoId,@Param("ministryId") Long ministryId,@Param("memberId") Long memberId,
			@Param("statusId") Long statusId,@Param("categoryId") Long categoryId);

//	@Query(value = """
//			select
//			mm.id as ministryId,
//			mm.ministry_name as ministry,
//			count(ca.id) as count
//			from cga.assurance ca
//			left join master.ministries mm on mm.id=ca.ministry_id
//			where ca.house_id=:houseId
//			and (:ministryId is null or ca.ministry_id=:ministryId)
//			group by
//			ministry,ministryId
//			""", nativeQuery = true)
//	List<Object[]> getMinistryWiseAssurancesCount(@Param("houseId") Long houseId,@Param("ministryId") Long ministryId);
//	
//	@Query(value = """
//			select
//			mpd.id as memberId,
//			mpd.full_name as memberName,
//			count(ca.id) as count
//			from cga.assurance ca
//			left join members.personal_details mpd on mpd.id=ca.member_id
//			where ca.house_id=:houseId
//			and (:memberId is null or ca.member_id=:memberId)
//			group by
//			memberName,memberId
//			""", nativeQuery = true)
//	List<Object[]> getMemberWiseAssurancesCount(@Param("houseId") Long houseId,@Param("memberId") Long memberId);
//	
//	@Query(value = """
//			select
//			mc.id as statusId,
//			mc.code as statusName,
//			count(ca.id) as count
//			from cga.assurance ca
//			left join master.codes mc on mc.id=ca.status_id
//			where ca.house_id=:houseId
//			and (:statusId is null or ca.status_id=:statusId)
//			group by
//			statusName,statusId
//			""", nativeQuery = true)
//	List<Object[]> getStatusWiseAssurancesCount(@Param("houseId") Long houseId,@Param("statusId") Long statusId);
//	
//	@Query(value = """
//			select
//			mc.id as categoryId,
//			mc.code as categoryName,
//			count(ca.id) as count
//			from cga.assurance ca
//			left join master.codes mc on mc.id=ca.category_id
//			where ca.house_id=:houseId
//			and (:categoryId is null or ca.category_id=:categoryId)
//			group by
//			categoryName,categoryId
//			""", nativeQuery = true)
//	List<Object[]> getCategoryWiseAssurancesCount(@Param("houseId") Long houseId,@Param("categoryId") Long categoryId);
	
	@Query(value = """
						select
			ca.question_no as questionNo,
			ca.assurance_no as assuranceNo,
			mc1.code as category,
			case
				when ca.house_id = 1 then rs.session_number
				when ca.house_id = 2 then ls.session_number
			end as sessionNo,
			lsm.lok_sabha_number as lokSabhaNo,
			ca.assurance_date as assuranceDate,
			mpd.full_name as member,
			mm.ministry_name as ministry,
			md.department_name as department,
			ca.created_at as questionDate,
			ca.subject as subject,
			mc.code as questionType,
			mc2.code as status,
			ca.details_of_implementation as implementationDetails,
			ca.remarks as remarks
			from cga.assurance ca
			left join master.codes mc on mc.id=ca.assurance_type_id
			left join master.codes mc1 on mc1.id=ca.category_id
			left join master.codes mc2 on mc2.id=ca.status_id
			left join members.personal_details mpd on mpd.id=ca.member_id
			left join master.ministries mm on mm.id=ca.ministry_id
			left join master.departments md on md.id=ca.department_id
			left join master.rajya_sabha_session_master rs on ca.rajya_sabha_session_no = rs.id and ca.house_id = 1
			left join master.lok_sabha_session_masters ls on ca.lok_sabha_session_no = ls.id and ca.house_id = 2
			left join master.lok_sabha_masters lsm on ca.lok_sabha_no =lsm.id and ca.house_id = 2
			where ca.house_id=:houseId
			and(
			(:houseId =2 and ca.lok_sabha_session_no is not null
			and (:lokSabhaNoId is null or ca.lok_sabha_no=:lokSabhaNoId)
			and (:sessionNoId is null or ca.lok_sabha_session_no=:sessionNoId))
			or
			(:houseId =1 and ca.rajya_sabha_session_no is not null
			and (:sessionNoId is null or ca.rajya_sabha_session_no=:sessionNoId))
			)
			and (cast(:fromDate as DATE) is null or ca.assurance_date >=cast(:fromDate as DATE))
			and (cast(:toDate as DATE) is null or ca.assurance_date <cast(:toDate as DATE))
			and (:categoryId is null or ca.category_id=:categoryId)
			and (:memberId is null or ca.member_id=:memberId)
			and (:ministryId is null or ca.ministry_id=:ministryId)
			and (:departmentId is null or ca.department_id=:departmentId)
			and (:questionTypeId is null or ca.assurance_type_id=:questionTypeId)
			and (:statusId is null or ca.status_id=:statusId)
			""", nativeQuery = true)
	List<Object[]> getSessionWiseAssurancesDetails(@Param("houseId") Long houseId,@Param("lokSabhaNoId") Long lokSabhaNoId, 
			@Param("sessionNoId") Long sessionNoId,@Param("fromDate") LocalDate fromDate,@Param("toDate") LocalDate toDate,
			@Param("categoryId") Long categoryId,@Param("memberId") Long memberId,@Param("ministryId") Long ministryId,
			@Param("departmentId") Long departmentId,@Param("questionTypeId") Long questionTypeId,@Param("statusId") Long statusId);
	
	@Query(value = """
						select
			count(ca.id) as totalAssurances
			from cga.assurance ca
			where ca.house_id=:houseId
			and(
			(:houseId =2 and ca.lok_sabha_session_no is not null
			and (:lokSabhaNoId is null or ca.lok_sabha_no=:lokSabhaNoId)
			and (:sessionNoId is null or ca.lok_sabha_session_no=:sessionNoId))
			or
			(:houseId =1 and ca.rajya_sabha_session_no is not null
			and (:sessionNoId is null or ca.rajya_sabha_session_no=:sessionNoId))
			)
			and (:ministryId is null or ca.ministry_id=:ministryId)
			and (:memberId is null or ca.member_id=:memberId)
			and (:statusId is null or ca.status_id=:statusId)
			and (:categoryId is null or ca.category_id=:categoryId)
			""", nativeQuery = true)
	String getTotalAssurancesCountByHouseId(@Param("houseId") Long houseId,@Param("lokSabhaNoId") Long lokSabhaNoId,
			@Param("sessionNoId") Long sessionNoId,@Param("ministryId") Long ministryId,@Param("memberId") Long memberId,
			@Param("statusId") Long statusId,@Param("categoryId") Long categoryId);
}


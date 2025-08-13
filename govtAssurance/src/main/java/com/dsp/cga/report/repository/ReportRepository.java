package com.dsp.cga.report.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.clubbedassurance.entity.ClubbedAssurance;

@Repository
public interface ReportRepository extends JpaRepository<ClubbedAssurance, Long> {

	@Query(value = """
			select
			    ca.assurance_no AS "assuranceNo",
			    mc.code AS "questionType",
			    ca.question_no AS "questionNo",
			    ca.subject AS "subject",
			    mm.ministry_name AS "ministry",
			    cir.laying_date AS "layingDate",
			    TO_CHAR(cir.created_at, 'YYYY-MM-DD HH24:MI:SS') AS "createdOn"
			from cga.implementation_requests cir
			join cga.assurance ca on ca.id = cir.assurance_id
			join master.ministries mm on mm.id = ca.ministry_id
			join master.codes mc on mc.id = ca.assurance_type_id
			WHERE cir.created_at >= :fromDate 
			AND cir.created_at <= :toDate
			""", nativeQuery = true)
	List<Object[]> fetchImplementationReports(LocalDateTime fromDate,
			 LocalDateTime toDate);
	
//	@Param("toDate")
//	where cir.created_at between :fromDate and :toDate
	@Query(value = "SELECT cir.id FROM cga.implementation_requests cir WHERE cir.assurance_id = 457", nativeQuery = true)
	List<Long> testId();
	
	@Query(value = "SELECT current_database(), current_schema()", nativeQuery = true)
	Object[] findCurrentDbAndSchema();
}

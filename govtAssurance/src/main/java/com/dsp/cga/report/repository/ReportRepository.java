package com.dsp.cga.report.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.clubbedassurance.entity.ClubbedAssurance;
import com.dsp.cga.report.dto.ExtensionReportResponseDto;
import com.dsp.cga.report.dto.ImplementationReportResponseDto;

@Repository
public interface ReportRepository extends JpaRepository<ClubbedAssurance, Long> {

	@Query(value = """
			select
			    ca.assurance_no AS assuranceNo,
			    mc.code AS questionType,
			    ca.question_no AS questionNo,
			    ca.subject AS subject,
			    mm.ministry_name AS ministry,
			    cir.laying_date AS layingDate,
			    cir.created_at AS updatedOn
			from cga.implementation_requests cir
			join cga.assurance ca on ca.id = cir.assurance_id
			join master.ministries mm on mm.id = ca.ministry_id
			join master.codes mc on mc.id = ca.assurance_type_id
			WHERE 
			cir.created_at >= COALESCE(CAST(:fromDate AS DATE), cir.created_at)
			--(:fromDate is null or cir.created_at >= (:fromDate)::date)
			 AND
			 cir.created_at < COALESCE(CAST(:toDate AS DATE), cir.created_at + interval '100 years')
			--(:toDate is null or cir.created_at < (:toDate)::date)
			order by cir.created_at desc
			""", nativeQuery = true)
	List<Object[]> fetchImplementationReports(@Param("fromDate") LocalDate fromDate,@Param("toDate") LocalDate toDate);

	@Query(value = """
						select
			ca.assurance_no as assuranceNo,
			mc.code as questionType,
			ca.question_no as questionNo,
			--question date not confirmed
			ca.subject as subject,
			mm.ministry_name as ministry,
			cer.extension_no as extensionNo,
			cer.ext_requested_till_date as extReqDate,
			cer.ext_granted_till_date as extGrantDate,
			cer.ministry_ref_no as ministryRefNo,
			cer.ministry_ref_date as ministryRefDate,
			cer.remarks as extensionRemarks,
			cer.created_at as createdOn
			from cga.extension_requests cer
			join cga.assurance ca on ca.id=cer.assurance_id
			join master.codes mc on mc.id=ca.assurance_type_id
			join master.ministries mm on mm.id=ca.ministry_id
			where cer.created_at >= :fromDate
			and cer.created_at < :toDate
			order by cer.created_at desc
			""", nativeQuery = true)
	List<ExtensionReportResponseDto> fetchExtensionReports(@Param("fromDate") LocalDate fromDate,@Param("toDate") LocalDate toDate);
}

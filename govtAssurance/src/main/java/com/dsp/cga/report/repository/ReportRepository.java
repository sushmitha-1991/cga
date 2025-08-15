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
	List<Object[]> fetchImplementationReports(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

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
			cer.updated_at as updatedOn
			from cga.extension_requests cer
			join cga.assurance ca on ca.id=cer.assurance_id
			join master.codes mc on mc.id=ca.assurance_type_id
			join master.ministries mm on mm.id=ca.ministry_id
			where
			cer.ext_granted_till_date is not null
			and cer.updated_at < current_date
			order by cer.updated_at desc
			""", nativeQuery = true)
	List<Object[]> fetchExtensionReports();

	@Query(value = """
			select
			ca.assurance_no as assuranceNo,
			mc.code as questionType,
			ca.question_no as questionNo,
			--question date not confirmed
			ca.subject as subject,
			mm.ministry_name as ministry,
			cdr.ministry_ref_no as ministryRefNo,
			cdr.ministry_ref_date as ministryRefDate,
			cdr.remarks as reason,
			cdr.meeting_date as meetingDate,
			--committee decision doubt
			cdr.updated_at as updatedOn
			from cga.dropping_requests cdr
			join cga.assurance ca on ca.id=cdr.assurance_id
			join master.codes mc on mc.id=ca.assurance_type_id
			join master.ministries mm on mm.id=ca.ministry_id
			where cdr.drop_request_date is not null
			order by cdr.updated_at desc
			""", nativeQuery = true)
	List<Object[]> fetchDroppedAssurancesReports();
	
	@Query(value = """
									select rs.session_number as sessionNo,
			mc.code as sourceName,
			mpd.full_name as member,
			string_agg(pd.full_name,', ' order by pd.full_name) as clubbedMember,
			mm.ministry_name as ministry,
			md.department_name as department,
			ca.assurance_no as assuranceNo,
			ca.file_no fileNo,
			mc1.code as questionType,
			ca.question_no AS questionNo,
			--question date not confirmed
			ca.subject AS subject,
			--extent doubt
			mc2.code as pendingDueTo
			from cga.assurance ca
			join master.codes mc on mc.id=ca.source_id
			join master.codes mc1 on mc1.id=ca.assurance_type_id
			join master.codes mc2 on mc2.id=ca.pending_due_to
			join cga.clubbed_members ccm on ccm.assurance_id=ca.id
			join members.personal_details pd on pd.id=ccm.clubbed_member
			join members.personal_details mpd on mpd.id=ca.member_id
			JOIN master.rajya_sabha_session_master rs
			ON ca.rajya_sabha_session_no = rs.id
			join master.ministries mm on mm.id=ca.ministry_id
			join master.departments md on md.id=ca.department_id
			where pending_due_to is not null and ca.updated_at < current_date
			group by sessionNo,sourceName,member,ministry,department,assuranceNo,fileNo,
			questionType,questionNo,subject,pendingDueTo
			""", nativeQuery = true)
	List<Object[]> fetchPendingAssurancesLists();
	
	@Query(value = """
						select
			mm.ministry_name as ministry,
			case
			        when ca.house_id = 1 then rs.session_number
			        when ca.house_id = 2 then ls.session_number
			    end as sessionNo,
			mpd.full_name as member,
			ca.assurance_no as assuranceNo,
			ca.question_no as questionNo,
			mc.code as questionType,
			--question date not confirmed
			ca.subject as subject,
			--extent doubt
			mc1.code as pendingDueTo
			from master.ministries mm
			join cga.assurance ca on mm.id=ca.ministry_id
			join members.personal_details mpd on mpd.id=ca.member_id
			join master.houses hm on ca.house_id = hm.id
			left join master.rajya_sabha_session_master rs on ca.rajya_sabha_session_no = rs.id and ca.house_id = 1
			left join master.lok_sabha_session_masters ls on ca.lok_sabha_session_no = ls.id and ca.house_id = 2
			join master.codes mc on mc.id=ca.assurance_type_id
			join master.codes mc1 on mc1.id=ca.pending_due_to
			where ca.pending_due_to is not null and ca.updated_at < current_date
			group by mm.ministry_name,sessionNo,member,assuranceNo,questionNo,questionType,subject,pendingDueTo
			order by mm.ministry_name
			""", nativeQuery = true)
	List<Object[]> fetchMinistryWisePendingAssurancesLists();
	
	@Query(value = """
						select
			(mm.ministry_name ||' / '||md.department_name) as ministryOrDepartment,
			count(ca.id) as pendingAssurances,
			count(*) filter (where current_date - ca.updated_at < interval '2 years') as lessThanTwoYears,
			count(*) filter (where current_date - ca.updated_at >= interval '2 years' and
			current_date - ca.updated_at < interval '5 years') as betweenTwoAndFiveYears,
			count(*) filter (where current_date - ca.updated_at >= interval '5 years' and
			current_date - ca.updated_at < interval '10 years') as betweenFiveAndTenYears,
			count(*) filter (where current_date - ca.updated_at >= interval '10 years') as moreThanTenYears
			from cga.assurance ca
			join master.ministries mm on mm.id=ca.ministry_id
			join master.departments md on md.id=ca.department_id
			where ca.status_id in(57,766) and ca.pending_due_to is not null and ca.updated_at < current_date
			group by mm.ministry_name,md.department_name
			order by mm.ministry_name,md.department_name			
			""", nativeQuery = true)
	List<Object[]> fetchConsolidatedMinistryWisePendingAssurancesLists();
	
	
	@Query(value = """
						select
			(mm.ministry_name ||' / '||md.department_name) as ministryOrDepartment,
			count(ca.id) as totalAssurances,
			count(*) filter (where status_id in (56,767)) as totalAssurancesFimp,
			count(*) filter (where status_id in (56,767) and current_date - ca.updated_at < interval '2 years') as lessThanTwoYears,
			count(*) filter (where status_id in (56,767) and current_date - ca.updated_at >= interval '2 years' and
			current_date - ca.updated_at < interval '5 years') as betweenTwoAndFiveYears,
			count(*) filter (where status_id in (56,767) and current_date - ca.updated_at >= interval '5 years' and
			current_date - ca.updated_at < interval '10 years') as betweenFiveAndTenYears,
			count(*) filter (where status_id in (56,767) and current_date - ca.updated_at >= interval '10 years') as moreThanTenYears
			from cga.assurance ca
			join master.ministries mm on mm.id=ca.ministry_id
			join master.departments md on md.id=ca.department_id
			group by mm.ministry_name,md.department_name
			order by mm.ministry_name,md.department_name	
			""", nativeQuery = true)
	List<Object[]> fetchConsolidatedMinistryWiseFimpAssurancesLists();
}

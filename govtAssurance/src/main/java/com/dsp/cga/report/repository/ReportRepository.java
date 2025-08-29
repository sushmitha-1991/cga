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
			    ca.created_at as questionDate,
			    ca.subject AS subject,
			    mm.ministry_name AS ministry,
			    cir.laying_date AS layingDate,
			    case
			        when ca.house_id = 1 then rs.session_number
			        when ca.house_id = 2 then ls.session_number
			    end as sessionNo,
			    lsm.lok_sabha_number as lokSabhaNo,
			    case
			    	when ca.house_id = 1 then mc1.code
			        when ca.house_id = 2 then mc2.code
			    end as committeeDecision,
				--mc1.code as committeeDecision,
				cir.reason as reason,
			    cir.created_at AS updatedOn
			from cga.implementation_requests cir
			join cga.assurance ca on ca.id = cir.assurance_id
			join master.ministries mm on mm.id = ca.ministry_id
			join master.codes mc on mc.id = ca.assurance_type_id
			join master.codes mc1 on mc1.id=cir.committee_decision_id
			left join master.codes mc2 on mc2.id=ca.status_id
			left join master.rajya_sabha_session_master rs on ca.rajya_sabha_session_no = rs.id and ca.house_id = 1
			left join master.lok_sabha_session_masters ls on ca.lok_sabha_session_no = ls.id and ca.house_id = 2
			left join master.lok_sabha_masters lsm on ca.lok_sabha_no =lsm.id and ca.house_id = 2
			WHERE
			ca.house_id=:houseId
			and 
			cir.created_at >= COALESCE(CAST(:fromDate AS DATE), cir.created_at)
			--(:fromDate is null or cir.created_at >= (:fromDate)::date)
			 AND
			 cir.created_at < COALESCE(CAST(:toDate AS DATE), cir.created_at + interval '100 years')
			--(:toDate is null or cir.created_at < (:toDate)::date)
			order by cir.created_at desc
			""", nativeQuery = true)
	List<Object[]> fetchImplementationReports(@Param("houseId") Long houseId,@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

	@Query(value = """
						select
			ca.assurance_no as assuranceNo,
			mc.code as questionType,
			ca.question_no as questionNo,
			ca.created_at as questionDate,
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
			ca.house_id=:houseId
			and cer.ext_granted_till_date is not null
			and cer.updated_at < current_date
			order by cer.updated_at desc
			""", nativeQuery = true)
	List<Object[]> fetchExtensionReports(@Param("houseId") Long houseId);

	@Query(value = """
			select
			ca.assurance_no as assuranceNo,
			mc.code as questionType,
			ca.question_no as questionNo,
			ca.created_at as questionDate,
			ca.subject as subject,
			mm.ministry_name as ministry,
			cdr.ministry_ref_no as ministryRefNo,
			cdr.ministry_ref_date as ministryRefDate,
			cdr.remarks as reason,
			cdr.meeting_date as meetingDate,
			cdr.updated_at as updatedOn
			from cga.dropping_requests cdr
			join cga.assurance ca on ca.id=cdr.assurance_id
			join master.codes mc on mc.id=ca.assurance_type_id
			join master.ministries mm on mm.id=ca.ministry_id
			where ca.house_id=:houseId 
			and cdr.drop_request_date is not null
			order by cdr.updated_at desc
			""", nativeQuery = true)
	List<Object[]> fetchDroppedAssurancesReports(@Param("houseId") Long houseId);
	
	@Query(value = """
									select
			case
			        when ca.house_id = 1 then rs.session_number
			        when ca.house_id = 2 then ls.session_number
			    end as sessionNo,
			    lsm.lok_sabha_number as lokSabhaNo,
			mc.code as sourceName,
			mpd.full_name as member,
			string_agg(pd.full_name,', ' order by pd.full_name) as clubbedMember,
			mm.ministry_name as ministry,
			md.department_name as department,
			ca.assurance_no as assuranceNo,
			ca.file_no fileNo,
			mc1.code as questionType,
			ca.question_no AS questionNo,
			ca.created_at as questionDate,
			ca.subject AS subject,
			cer.ext_granted_till_date AS extent,
			mc2.code as pendingDueTo
			from cga.assurance ca
			join master.codes mc on mc.id=ca.source_id
			join master.codes mc1 on mc1.id=ca.assurance_type_id
			join master.codes mc2 on mc2.id=ca.pending_due_to
			join cga.clubbed_members ccm on ccm.assurance_id=ca.id
			join members.personal_details pd on pd.id=ccm.clubbed_member
			join members.personal_details mpd on mpd.id=ca.member_id
			join master.ministries mm on mm.id=ca.ministry_id
			join master.departments md on md.id=ca.department_id
			left join cga.extension_requests cer on cer.assurance_id=ca.id
			left join master.rajya_sabha_session_master rs on ca.rajya_sabha_session_no = rs.id and ca.house_id = 1
			left join master.lok_sabha_session_masters ls on ca.lok_sabha_session_no = ls.id and ca.house_id = 2
			left join master.lok_sabha_masters lsm on ca.lok_sabha_no =lsm.id and ca.house_id = 2
			where ca.house_id=:houseId
			and pending_due_to is not null and ca.updated_at < current_date
			group by sessionNo,lokSabhaNo,sourceName,member,ministry,department,assuranceNo,fileNo,
			questionType,questionNo,questionDate,ca.subject,extent,pendingDueTo
			""", nativeQuery = true)
	List<Object[]> fetchPendingAssurancesLists(@Param("houseId") Long houseId);
	
	@Query(value = """
						select
			mm.ministry_name as ministry,
			case
			        when ca.house_id = 1 then rs.session_number
			        when ca.house_id = 2 then ls.session_number
			    end as sessionNo,
			    lsm.lok_sabha_number as lokSabhaNo,
			mpd.full_name as member,
			ca.assurance_no as assuranceNo,
			ca.question_no as questionNo,
			mc.code as questionType,
			ca.created_at as questiondate,
			ca.subject as subject,
			cer.ext_granted_till_date AS extent,
			mc1.code as pendingDueTo
			from master.ministries mm
			join cga.assurance ca on mm.id=ca.ministry_id
			join members.personal_details mpd on mpd.id=ca.member_id
			left join master.rajya_sabha_session_master rs on ca.rajya_sabha_session_no = rs.id and ca.house_id = 1
			left join master.lok_sabha_session_masters ls on ca.lok_sabha_session_no = ls.id and ca.house_id = 2
			left join master.lok_sabha_masters lsm on ca.lok_sabha_no =lsm.id and ca.house_id = 2
			join master.codes mc on mc.id=ca.assurance_type_id
			join master.codes mc1 on mc1.id=ca.pending_due_to
			left join cga.extension_requests cer on cer.assurance_id=ca.id
			where ca.house_id=:houseId
			and ca.pending_due_to is not null and ca.updated_at < current_date
			group by mm.ministry_name,sessionNo,lokSabhaNo,member,assuranceNo,questionNo,questionType,questionDate,ca.subject,extent,pendingDueTo
			order by mm.ministry_name
			""", nativeQuery = true)
	List<Object[]> fetchMinistryWisePendingAssurancesLists(@Param("houseId") Long houseId);
	
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
			where ca.house_id=:houseId 
			and ca.status_id in(57,766) and ca.pending_due_to is not null and ca.updated_at < current_date
			group by mm.ministry_name,md.department_name
			order by mm.ministry_name,md.department_name			
			""", nativeQuery = true)
	List<Object[]> fetchConsolidatedMinistryWisePendingAssurancesLists(@Param("houseId") Long houseId);
	
	
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
			where ca.house_id=:houseId
			group by mm.ministry_name,md.department_name
			order by mm.ministry_name,md.department_name	
			""", nativeQuery = true)
	List<Object[]> fetchConsolidatedMinistryWiseFimpAssurancesLists(@Param("houseId") Long houseId);
	
	@Query(value = """
						select
			mm.ministry_name as ministry,
			count(ca.id) as totalAssurances,
			count(*) filter (where status_id in (57,766)) as totalPendingAssurances,
			count(*) filter (where status_id in (56,767)) as fullyImplementedAssurances,
			count(*) filter (where status_id in (770)) as droppedAssurances
			from cga.assurance ca
			join master.ministries mm on mm.id=ca.ministry_id
			where ca.house_id=:houseId and ca.updated_at < current_date
			group by ministry
			order by ministry
			""", nativeQuery = true)
	List<Object[]> fetchConsolidatedMinistryWiseAssurancesDetails(@Param("houseId") Long houseId);
	
	@Query(value = """
									select
			    ca.assurance_no AS assuranceNo,
			    mc.code AS questionType,
			    ca.question_no AS questionNo,
			    ca.created_at as questionDate,
			    ca.subject AS subject,
			    mm.ministry_name AS ministry,
			    count(distinct ccam.mapped_clubbed_assurance_id) as totalClubbedAssurances,
			    case when count(ccam.mapped_clubbed_assurance_id)=0 then null
			         else string_agg(
			                rn || '. Assurance given in reply to USQ/SQ No. '
			                || coalesce(sub.question_no::text, '__')
			                || ' dated ' || coalesce(to_char(sub.created_at, 'DD-Mon-YYYY'), '__')
			                || ' regarding ' || coalesce(sub.subject, '__'),
			                '
			                
			                '
			                order by rn
			              )
			    end as clubbedAssurancesList
			from cga.assurance ca
			left join cga.clubbed_assurances cca
			       on cca.assurance_id = ca.id
			left join cga.clubbed_assurance_mapping ccam
			       on ccam.clubbed_assurance_id = cca.id
			left join (
			    select a1.*,
			           row_number() over (partition by cca.assurance_id order by a1.created_at, a1.id) as rn,
			           cca.assurance_id as parent_id
			    from cga.clubbed_assurance_mapping ccam
			    join cga.clubbed_assurances cca on cca.id = ccam.clubbed_assurance_id
			    join cga.assurance a1 on a1.id = ccam.mapped_clubbed_assurance_id
			) sub on sub.parent_id = ca.id and sub.id = ccam.mapped_clubbed_assurance_id
			join master.codes mc on mc.id = ca.assurance_type_id
			join master.ministries mm on mm.id = ca.ministry_id
			where ca.house_id=:houseId
			group by ca.id, assuranceNo, questionType, questionNo, questionDate, ca.subject, ministry
			order by ca.id desc
			""", nativeQuery = true)
	List<Object[]> fetchClubbingDetails(@Param("houseId") Long houseId);
	
	
	@Query(value = """
						select
			ca.assurance_no as assuranceNo,
			mc.code as questionType,
			ca.question_no as questionNo,
			ca.created_at as questionDate,
			ca.subject as subject,
			ca.updated_at as deleteDate
			from cga.assurance ca
			join master.codes mc on mc.id=ca.assurance_type_id
			where ca.house_id=:houseId and ca.is_deleted='true'
			""", nativeQuery = true)
	List<Object[]> fetchDeletedAssurancesReport(@Param("houseId") Long houseId);
	
	@Query(value = """
						select
			ca.assurance_no as assuranceNo,
			ca.question_no as questionNo,
			ca.created_at as questionDate,
			ca.subject as subject,
			mm.ministry_name as ministry,
			ca.date_of_implementation as implementationDate,
			cir.laying_date as layingDate,
			cdr.actual_drop_date as dropDate,
			ca.updated_at as deleteDate,
			cdr.ministry_ref_no as ministryRefNo
			from cga.assurance ca
			join cga.implementation_requests cir on cir.assurance_id=ca.id
			join cga.dropping_requests cdr on cdr.assurance_id=ca.id
			join master.ministries mm on mm.id=ca.ministry_id
			where ca.house_id=:houseId
			order by ca.id desc
			""", nativeQuery = true)
	List<Object[]> fetchAssuranceRegisterReport(@Param("houseId") Long houseId);
	
	@Query(value = """
						WITH date_params AS (
			    SELECT
			        date_trunc('quarter', CAST(:inputDate AS date))::date AS qtr_start,
			        (date_trunc('quarter', CAST(:inputDate AS date)) + interval '3 month - 1 day')::date AS qtr_end,
			        CAST(:inputDate AS date) AS ref_date
			)
			SELECT
			    mm.ministry_name as ministry,

			    -- Pending at start of quarter
			    COUNT(*) FILTER (
			        WHERE ca.status_id in (57,766)
			          AND ca.updated_at < dp.qtr_start
			    ) AS pendingStartQrtr,

			    -- Added during quarter
			    COUNT(*) FILTER (
			        WHERE ca.updated_at BETWEEN dp.qtr_start AND dp.qtr_end
			    ) AS addedQrtr,

			    -- Dropped during quarter
			    COUNT(*) FILTER (
			        WHERE ca.status_id in (770)
			          AND ca.updated_at BETWEEN dp.qtr_start AND dp.qtr_end
			    ) AS droppedQrtr,

			    -- Laid during quarter
			    COUNT(*) FILTER (
			        WHERE ca.updated_at BETWEEN dp.qtr_start AND dp.qtr_end
			    ) AS laidQrtr,

			    -- Ageing buckets (calculated as of :givenDate)
			    COUNT(*) FILTER (
			        WHERE ca.status_id in (57,766)
			          AND AGE(dp.ref_date, ca.updated_at) <= interval '6 months'
			    ) AS lessThanSixMonths,

			    COUNT(*) FILTER (
			        WHERE ca.status_id in (57,766)
			          AND AGE(dp.ref_date, ca.updated_at) > interval '6 months'
			          AND AGE(dp.ref_date, ca.updated_at) <= interval '12 months'
			    ) AS betweenSixAndTwelveMonths,

			    COUNT(*) FILTER (
			        WHERE ca.status_id in (57,766)
			          AND AGE(dp.ref_date, ca.updated_at) > interval '12 months'
			          AND AGE(dp.ref_date, ca.updated_at) <= interval '24 months'
			    ) AS betweenTwelveAndTwentyFourMonths,

			    COUNT(*) FILTER (
			        WHERE ca.status_id in (57,766)
			          AND AGE(dp.ref_date, ca.updated_at) > interval '24 months'
			    ) AS moreThanTwentyFourMonths,

			    -- Total pending at given date
			    COUNT(*) FILTER (
			        WHERE ca.status_id in (57,766)
			          AND ca.updated_at <= dp.ref_date
			    ) AS totalPending

			FROM cga.assurance ca
			join master.ministries mm on mm.id=ca.ministry_id
			CROSS JOIN date_params dp
			where ca.house_id=:houseId
			GROUP BY mm.ministry_name
			ORDER BY mm.ministry_name
			""", nativeQuery = true)
	List<Object[]> fetchQuarterlyReport(@Param("houseId") Long houseId,@Param("inputDate") LocalDate inputDate);
}

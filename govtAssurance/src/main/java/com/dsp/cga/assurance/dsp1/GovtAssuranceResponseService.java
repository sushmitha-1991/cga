package com.dsp.cga.assurance.dsp1;

import java.util.List;
import java.util.Map;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GovtAssuranceResponseService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public GovtAssuranceResponseDTO getGovtAssuranceList(
            String mpCode,
            String loksabha,
            String session,
            String subject,
            String member,
            String ministry,
            String qType,
            String status,
            String catg,
            LocalDate dateFrom,
            LocalDate dateTo,
            String  rsSession,
            Long sessionFrom,
            Long sessionTo
    ) {
    	
        StringBuilder sql = new StringBuilder("""
            SELECT
                a.assurance_no AS assuNo,
                lm.lok_sabha_number::int AS loksabha,
                lsm.session_number_int AS session,
                a.assurance_date AS date,
                m.ministry_name AS ministry,
                CONCAT(pd.first_name, ' ', pd.last_name) AS member,
                a.subject AS subject,
                md.department_name AS department,
                cat.code AS category,
                stat.code AS status,
                a.report_no AS impRecordNo,
                a.remarks AS remarks,
                a.text_of_assurance AS textOfAssurance,
                '' AS url,
                '' AS answerHtml,
                '' AS questionHtml,
                qt.code AS qtype,
                a.question_no AS qno,
                rssm.session_number AS rsSession
            FROM cga.assurance a
            LEFT JOIN master.ministries m ON m.id = a.ministry_id
            LEFT JOIN master.departments md ON md.id = a.department_id
            LEFT JOIN members.personal_details pd ON pd.id = a.member_id
            LEFT JOIN master.codes cat ON cat.id = a.category_id
            LEFT JOIN master.codes stat ON stat.id = a.status_id
            LEFT JOIN master.codes qt ON qt.id = a.question_type
            LEFT JOIN master.lok_sabha_session_masters lsm ON lsm.id = a.lok_sabha_session_no
            LEFT JOIN master.lok_sabha_masters lm ON lm.id = a.lok_sabha_no
            LEFT JOIN master.rajya_sabha_session_master rssm 
                ON rssm.id = a.rajya_sabha_session_no 
            WHERE a.is_active = true AND a.is_deleted = false
              AND pd.mp_code = :mpCode
        """);

        Map<String, Object> paramMap = new HashMap<>();
       paramMap.put("mpCode", mpCode);
        
        if (loksabha != null) {
            sql.append(" AND lm.lok_sabha_number = :loksabha");
            paramMap.put("loksabha", loksabha);
        }
        if (session != null) {
            sql.append(" AND lsm.session_number_int = :session");
            paramMap.put("session", session);
        }
       
        if (member != null && !member.isBlank()) {
            sql.append(" AND LOWER(CONCAT(pd.first_name, ' ', pd.last_name)) LIKE LOWER(:member)");
            paramMap.put("member", "%" + member + "%");
        }
        if (ministry != null && !ministry.isBlank()) {
            sql.append(" AND LOWER(m.ministry_name) LIKE LOWER(:ministry)");
            paramMap.put("ministry", "%" + ministry + "%");
        }
        if (qType != null && !qType.isBlank()) {
            sql.append(" AND qt.code = :qType");
            paramMap.put("qType", qType);
        }
        if (subject != null && !subject.isBlank()) {
            sql.append(" AND LOWER(a.subject) LIKE LOWER(:subject)");
            paramMap.put("subject", "%" + subject + "%");
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND stat.code = :status");
            paramMap.put("status", status);
        }
        if (catg != null && !catg.isBlank()) {
            sql.append(" AND cat.code = :catg");
            paramMap.put("catg", catg);
        }
        if (dateFrom != null) {
            sql.append(" AND a.assurance_date >= :dateFrom");
            paramMap.put("dateFrom", Date.valueOf(dateFrom));
        }
        if (dateTo != null) {
            sql.append(" AND a.assurance_date <= :dateTo");
            paramMap.put("dateTo", Date.valueOf(dateTo));
        }
        if (rsSession != null) {
            sql.append(" AND rssm.session_number = :rsSession");
            paramMap.put("rsSession", rsSession);
        }
        if (sessionFrom != null) {
            sql.append(" AND lsm.session_number_int >= :sessionFrom");
            paramMap.put("sessionFrom", sessionFrom);
        }
        if (sessionTo != null) {
            sql.append(" AND lsm.session_number_int <= :sessionTo");
            paramMap.put("sessionTo", sessionTo);
        }

        List<GovtAssuranceResponseDTO.Record> records = jdbcTemplate.query(sql.toString(), paramMap, (rs, rowNum) -> {
            GovtAssuranceResponseDTO.Record record = new GovtAssuranceResponseDTO.Record();
            record.setAssuNo(rs.getString("assuNo"));
            record.setLoksabha(rs.getInt("loksabha"));
            record.setSession(rs.getInt("session"));
            record.setDate(rs.getString("date"));
            record.setMinistry(rs.getString("ministry"));
            record.setMember(rs.getString("member"));
            record.setSubject(rs.getString("subject"));
            record.setDepartment(rs.getString("department"));
            record.setCategory(rs.getString("category"));
            record.setStatus(rs.getString("status"));
            record.setImpRecordNo(rs.getString("impRecordNo"));
            record.setRemarks(rs.getString("remarks"));
            record.setTextOfAssurance(rs.getString("textOfAssurance"));
            record.setUrl(""); // static
            record.setAnswerHtml(""); // static
            record.setQuestionHtml(""); // static
            record.setQtype(rs.getString("qtype"));
            record.setQno(rs.getInt("qno"));
            record.setRsSession(rs.getInt("rsSession"));
            return record;
        });

        GovtAssuranceResponseDTO dto = new GovtAssuranceResponseDTO();
        dto.setRecords(records);

        GovtAssuranceResponseDTO.Metadata metadata = new GovtAssuranceResponseDTO.Metadata();
        metadata.setCurrentPageNumber(1);
        metadata.setPerPageSize(records.size());
        metadata.setTotalElements(records.size());
        metadata.setTotalPages(1);

        dto.set_metadata(metadata);

        return dto;
    }
}

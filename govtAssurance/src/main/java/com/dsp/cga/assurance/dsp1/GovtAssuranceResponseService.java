package com.dsp.cga.assurance.dsp1;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GovtAssuranceResponseService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public GovtAssuranceResponseDTO getGovtAssuranceList(String mpCode) {

        String sql = """
            SELECT
                a.assurance_no AS assuNo,
                lm.lok_sabha_number::int AS loksabha,
                lsm.session_number_int AS session,
                a.assurance_date AS date,
                mm.ministry_name AS ministry,
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
                a.question_no AS qno
            FROM cga.assurance a
            LEFT JOIN master.ministries mm ON mm.id = a.ministry_id
            LEFT JOIN master.departments md ON md.id = a.department_id
            LEFT JOIN members.personal_details pd ON pd.id = a.member_id
            LEFT JOIN master.codes cat ON cat.id = a.category_id
            LEFT JOIN master.codes stat ON stat.id = a.status_id
            LEFT JOIN master.codes qt ON qt.id = a.question_type
            LEFT JOIN master.lok_sabha_session_masters lsm ON lsm.id = a.lok_sabha_session_no
            LEFT JOIN master.lok_sabha_masters lm ON lm.id = a.lok_sabha_no
            WHERE a.is_active = true AND a.is_deleted = false
              AND pd.mp_code = :mpCode
        """;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mpCode", mpCode);

        List<GovtAssuranceResponseDTO.Record> records = jdbcTemplate.query(sql, paramMap, (rs, rowNum) -> {
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
            return record;
        });

        GovtAssuranceResponseDTO dto = new GovtAssuranceResponseDTO();
        dto.setRecords(records);

        // Set metadata
        GovtAssuranceResponseDTO.Metadata metadata = new GovtAssuranceResponseDTO.Metadata();
        metadata.setCurrentPageNumber(1);  // static for now
        metadata.setPerPageSize(records.size()); // or set to 10 if paginated
        metadata.setTotalElements(records.size());
        metadata.setTotalPages(1);  // static for now, can calculate if using pagination

        dto.set_metadata(metadata);

        return dto;
    }
}

package com.dsp.cga.assurance.implementationrequest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssuranceWithImplementationRequestDTO {

    // Assurance-related
    private String assuranceNo;
    private Long sessionNo;
    private String questionNo;
    private String subject;

    private Long ministryId;
    private Long departmentId;
    private Long memberId;
    private Long houseId;

    // ImplementationRequest-related
    private Long implementationTypeId;
    private String partId;
    private String supplementaryId;
    private LocalDate layingDate;
    private String reason;

    private List<Map<String, Object>> implementationReportFiles;
    private List<Map<String, Object>> partReportFiles;
}

package com.dsp.cga.assurance.implementationrequest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.dto.AssuranceTypeDTO;
import com.dsp.cga.assurance.dto.CommitteeDecisionDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImplementationRequestDTO {
	
	    private Long id;
	    private Long assuranceId;
	    private Long committeeDecisionId;
	    private LocalDate layingDate;
	    private String statementNo;
	    private Long supplementaryId;
	    private Long partId;
	    private String reason;
	    private CommitteeDecisionDTO committeeDecisionDTO;
	    private List<Map<String, Object>> implementationReportFiles;
	    private List<Map<String, Object>> partReportFiles;
	    private List<Map<String, Object>> otherFiles;
	    private Boolean draftStatus; // Make sure this is handled correctly in your application logic
	}



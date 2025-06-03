package com.dsp.cga.assurance.implementationrequest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.dto.AssuranceTypeDTO;
import com.dsp.cga.assurance.dto.CommitteeDecisionDTO;
import com.dsp.cga.assurance.dto.ImplementationTypeDTO;

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
	    private String supplementaryId;
	    private String partId;
	    private String reason;
	    private String assuranceNo;
	    private Long implementationTypeId;
	    private Long session;
	    private LocalDate dueDateOfImplementation;
	    private String detailsOfImplementation;
	    private LocalDate dateOfImplementation;
	    private String status;
	    private String remarks;
	    private String statusForMinistry;
	    private Long applicationId;
	    private String applicationNo;
	    private String sendTo;
	    private CommitteeDecisionDTO committeeDecisionDTO;
	    private ImplementationTypeDTO implementationTypeDTO;
	    private List<Map<String, Object>> implementationReportFiles;
	    private List<Map<String, Object>> partReportFiles;
	    private List<Map<String, Object>> otherFiles;
	    private Boolean draftStatus; // Make sure this is handled correctly in your application logic
	}



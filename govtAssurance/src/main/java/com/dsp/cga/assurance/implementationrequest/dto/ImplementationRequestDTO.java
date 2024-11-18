package com.dsp.cga.assurance.implementationrequest.dto;

import java.time.LocalDate;

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
	    private String implementationReportFiles;
	    private String partReportFiles;
	    private String otherFiles;
	    private Boolean draftStatus;

}

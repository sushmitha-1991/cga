package com.dsp.cga.assurance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.clubbedassurance.dto.AssuranceIdNoPairDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonRequestResponseDTO {
	private Long id;
	private String requestType;
	private Long assuranceId;
	
	private Boolean fromMinistry;

	private String status;
	private LocalDate date;
	private String memberName;
	private String extensionNo;

	private LocalDate requestedDate;
	private List<Map<String, Object>> extensionFiles;
   // private List<Map<String, Object>> droppingFiles;

	private LocalDateTime createdAt;

	private Long statusId;

	private String droppingType; // Code is a reference to the 'master' microservice

	private String supplementaryId;

	private String partId;

	private String assuranceNo;

	private LocalDate dropRequestDate;

	private LocalDate actualDropDate;

	private Long type;

	private Boolean issueLetterMinistry;

	private LocalDate communicationDate;

	// private Long dropStatusId;
	//private List<Map<String, Object>> fileUrl;


	private String remarks;

	private Boolean draftStatus = Boolean.FALSE;

	// private StatusDTO statusDTO;

	private DroppingTypeDTO droppingTypeDTO;

	private AcceptedDTO acceptedDTO;

	private Long acceptedId;

	private String meetingNo;

	private Long session;

	private LocalDate meetingDate;

	private List<Map<String, Object>> droppingFiles;

	private List<Map<String, Object>> fileUrl;

	private String ministryName;
	private String lokSabhaNumber; // From Assurance
	private String lokSabhaSessionNumber; // From Assurance

	private LocalDate extGrantedTillDate;
	private LocalDate extRequestedTillDate;
	private String ministryRefNo;
	private String questionNo;

	private LocalDate ministryRefDate;
	private String lokSabhaSessionNo;
	private String subject;
	private String departmentName;
	private String rajyaSabhaSessionNumber;

	private String statusForMinistry;
	private String statusForCga;
	private String statusForMopa;
	
	private List<Long> clubbedAssuranceIds;
	private String AssuranceNo;
	private List<AssuranceIdNoPairDTO> clubbedAssurances;
	private Long committeeDecisionId;
	private LocalDate layingDate;
	private String statementNo;

	private String reason;

	private Long implementationTypeId;
	
	private String house;

	private LocalDate dueDateOfImplementation;
	private String detailsOfImplementation;
	private LocalDate dateOfImplementation;

	private CommitteeDecisionDTO committeeDecisionDTO;
	private ImplementationTypeDTO implementationTypeDTO;
	private List<Map<String, Object>> implementationReportFiles;
	private List<Map<String, Object>> partReportFiles;
}
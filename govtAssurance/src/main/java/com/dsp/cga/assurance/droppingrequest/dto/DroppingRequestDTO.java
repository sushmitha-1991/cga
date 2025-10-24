package com.dsp.cga.assurance.droppingrequest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.dto.AcceptedDTO;
import com.dsp.cga.assurance.dto.DroppingTypeDTO;
import com.dsp.cga.assurance.dto.StatusDTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DroppingRequestDTO {

	private Long id;

	private Long assuranceId;

	private Long droppingType; // Code is a reference to the 'master' microservice

	private String supplementaryId;

	private String partId;

	private String assuranceNo;

	private LocalDate dropRequestDate;

	private LocalDate actualDropDate;
	
	private Long type;
	
	private Boolean issueLetterMinistry; 
	
	private LocalDate communicationDate;

	// private Long dropStatusId;

	private String remarks;

	@Builder.Default
	private Boolean draftStatus = Boolean.FALSE;

	// private StatusDTO statusDTO;

	private DroppingTypeDTO droppingTypeDTO;

	private AcceptedDTO acceptedDTO;

	private String ministryRefNo;

	private LocalDate ministryRefDate;

	private Long acceptedId;

	private String subject;

	private String meetingNo;
	
	private Long session;

	private LocalDate meetingDate;
	
	private List<Map<String, Object>> droppingFiles;
	
	private String statusForMinistry;
	
	private String statusForCga;
	
	@Builder.Default
	private Boolean fromMinistry = true;

}

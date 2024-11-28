package com.dsp.cga.assurance.droppingrequest.dto;

import java.time.LocalDate;

import com.dsp.cga.assurance.dto.DroppingTypeDTO;
import com.dsp.cga.assurance.dto.StatusDTO;

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

	private Long supplementaryId;

	private Long partId;

	private LocalDate dropRequestDate;

	private LocalDate actualDropDate;

	private Long dropStatusId;

	private String remarks;

	private Boolean draftStatus = Boolean.FALSE;
	
	private StatusDTO statusDTO;
	
	private DroppingTypeDTO droppingTypeDTO;

}

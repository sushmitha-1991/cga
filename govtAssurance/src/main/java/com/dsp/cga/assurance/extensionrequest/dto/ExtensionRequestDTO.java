package com.dsp.cga.assurance.extensionrequest.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ExtensionRequestDTO {

	private Long id;
	private Long assuranceId;
	private String questionNo;
	private String extensionNo;
	private String subject;
	private LocalDate extRequestedTillDate;
	private LocalDate extGrantedTillDate;
	private String ministryRefNo;
	private LocalDate ministryRefDate;
	private String remarks;
	private String extensionFiles;
	private Boolean draftStatus;
	private String assuranceNo;
}

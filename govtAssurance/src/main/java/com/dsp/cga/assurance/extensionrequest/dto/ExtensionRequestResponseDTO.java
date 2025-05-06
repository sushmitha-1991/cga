package com.dsp.cga.assurance.extensionrequest.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtensionRequestResponseDTO {

	private String assuranceNo;
    private String lokSabhaNo;
    private Long sessionNo;
    private String extensionNo;
    private String status;
    private String remarks;
    private LocalDate extGrantedTillDate;
    private LocalDate extRequestedTillDate;
    private String ministryRefNo;


	private LocalDate ministryRefDate;
	private String lokSabhaSessionNo;

	private String rajyaSabhaSessionNumber;
    
}

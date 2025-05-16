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

	private Long id;
	private String assuranceNo;
    private String lokSabhaNo;
    private Long sessionNo;
    private String extensionNo;
    private String status;
    private String remarks;
    private LocalDate extGrantedTillDate;
    private LocalDate extRequestedTillDate;
    private String ministryRefNo;
    private String questionNo;
    private String memberName;
    private String ministryName;
	private LocalDate ministryRefDate;
	private String lokSabhaSessionNo;
    private String subject;
    private String departmentName;
	private String rajyaSabhaSessionNumber;
    
}

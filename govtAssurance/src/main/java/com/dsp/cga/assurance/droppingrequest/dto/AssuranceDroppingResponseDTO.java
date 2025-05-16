package com.dsp.cga.assurance.droppingrequest.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssuranceDroppingResponseDTO {
	
	private Long id;
    private String lokSabhaNo;
    private String lokSabhaSessionNo;
    private String rajyaSabhaSessionNo;
    private String partId;
    private String supplementaryId;
    private String memberName;
    private String departmentName;
    private String ministryName;
    private String droppingType;
    private LocalDate dropRequestDate;
    private LocalDate actualDropDate;
    private String assuranceNo;
    private String status;
    private String remarks;
    private String ministryRefNo;
private String questionNo;
private String subject;
	private LocalDate ministryRefDate;
}
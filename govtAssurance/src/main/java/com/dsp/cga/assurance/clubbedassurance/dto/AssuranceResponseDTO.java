package com.dsp.cga.assurance.clubbedassurance.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AssuranceResponseDTO {
	
	private Long id;
    private String assuranceNo;
    private String lokSabhaNo;
    private String lokSabhaSessionNo;
    private String rajyaSabhaSessionNo;
    private LocalDate assuranceDate;
    private String questionNo;
    private LocalDate date;
    private String memberName;
    private String departmentName;
    private String ministryName;
    private String subject;
    
    private String status;
    private List<Long> clubbedAssuranceIds;
}
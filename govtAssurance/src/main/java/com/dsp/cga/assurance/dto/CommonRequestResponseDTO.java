package com.dsp.cga.assurance.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonRequestResponseDTO {
    private Long id;
    private String requestType;
    private Long assuranceId;
    private String subject;
    private String remarks;
    private Boolean fromMinistry;
    private String statusForMinistry;
    private String status;
private LocalDate date;
    private String memberName;

  private String  departmentName;
   private String ministryName;
    private String lokSabhaNumber;     // From Assurance
    private String lokSabhaSessionNumber;      // From Assurance
    private String rajyaSabhaSessionNumber;
}
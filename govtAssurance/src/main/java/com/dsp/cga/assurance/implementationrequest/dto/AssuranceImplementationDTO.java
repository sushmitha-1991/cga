package com.dsp.cga.assurance.implementationrequest.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssuranceImplementationDTO {
	
	 private String assuranceNo;
	   // private Long lokSabhaNo;
	    //private Long lokSabhaSessionNo;
	  //  private Long statusId;
	    private LocalDate dueDateOfImplementation;
	    private String detailsOfImplementation;
	    private LocalDate dateOfImplementation;
	    private String status;
	    private String lokSabhaNumber;
	    private String lokSabhaSessionNumber;
	    private String rajyaSabhaSessionNumber;
	    private String supplementaryId;
	    private String partId;
	    private String implementationTypeId;
		private String reason;
		private LocalDate layingDate;
	    

}

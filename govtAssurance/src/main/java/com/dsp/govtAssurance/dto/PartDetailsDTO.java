package com.dsp.govtAssurance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartDetailsDTO {
	
	 private Long id;
	    private String part;
	    private String subject;
	    private Long statusId;
	    private String extentOfAssurance;
	   

}

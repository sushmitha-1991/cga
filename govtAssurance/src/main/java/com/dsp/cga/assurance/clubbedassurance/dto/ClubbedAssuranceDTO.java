package com.dsp.cga.assurance.clubbedassurance.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubbedAssuranceDTO {
	
	 private Long id;
	// private Long sessionNo;
	    private Long assuranceId;
	    private List<Long> clubbedAssuranceIds;
	    private String AssuranceNo;
	    private List<AssuranceIdNoPairDTO> clubbedAssurances; 
	    

}

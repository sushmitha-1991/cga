package com.dsp.cga.assurance.clubbedassurance.dto;

import java.time.LocalDate;
import java.util.List;

import com.dsp.cga.assurance.dto.MemberDTO;

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
	    private LocalDate date;
	    private List<MemberDTO> clubbedMembersData;
	    private List<MemberDTO> supplementaryClubbedMembersData;
	    

}

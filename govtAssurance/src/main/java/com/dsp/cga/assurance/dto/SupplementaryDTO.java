package com.dsp.cga.assurance.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplementaryDTO {
	 private Long id;
	    private String supp;
	    private Long memberId;
	    private List<Long> clubbedMembers;
	    private String part;
	    private String subject;
	    private Long statusId;
	    private String extentOfAssurance;

}

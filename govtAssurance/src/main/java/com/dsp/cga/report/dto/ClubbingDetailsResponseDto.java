package com.dsp.cga.report.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ClubbingDetailsResponseDto {

	String assuranceNo;
	String questionType;
	String questionNo;
//	String questionDate;
	String subject; 
	String ministry;
	String totalClubbedAssurances;
	String clubbedAssurancesList;
	
	public ClubbingDetailsResponseDto(String assuranceNo, String questionType, String questionNo, String subject,
			String ministry, String totalClubbedAssurances, String clubbedAssurancesList) {
		super();
		this.assuranceNo = assuranceNo;
		this.questionType = questionType;
		this.questionNo = questionNo;
		this.subject = subject;
		this.ministry = ministry;
		this.totalClubbedAssurances = totalClubbedAssurances;
		this.clubbedAssurancesList = clubbedAssurancesList;
	}
}

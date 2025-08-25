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
public class MinistryWisePendingAssurancesResponseDto {

	String ministry;
	String sessionNo;
	String lokSabhNo;
	String member;
	String assuranceNo;
	String questionNo;
	String questionDate;
	String questionType;
	String subject;
	String extent;
	String pendingDueTo;
	
public MinistryWisePendingAssurancesResponseDto(String ministry, String sessionNo,String lokSabhNo, String member, String assuranceNo,
		String questionNo, String questionType,String questionDate, String subject, String extent,String pendingDueTo) {
	super();
	this.ministry = ministry;
	this.sessionNo = sessionNo;
	this.lokSabhNo=lokSabhNo;
	this.member = member;
	this.assuranceNo = assuranceNo;
	this.questionNo = questionNo;
	this.questionType = questionType;
	this.questionDate=questionDate;
	this.subject = subject;
	this.extent=extent;
	this.pendingDueTo = pendingDueTo;
}
	
	

	
}

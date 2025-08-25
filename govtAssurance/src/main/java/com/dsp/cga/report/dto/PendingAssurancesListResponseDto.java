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
public class PendingAssurancesListResponseDto {

	String sessionNo;
	String lokSabhaNo;
	String sourceName;
	String member;
	String clubbedMember;
	String ministry;
	String department;
	String assuranceNo;
	String fileNo;
	String questionType;
	String questionNo;
	String questionDate;
	String subject;
	String extent;
	String pendingDueTo;
public PendingAssurancesListResponseDto(String sessionNo,String lokSabhaNo, String sourceName, String member, String clubbedMember,
		String ministry, String department, String assuranceNo, String fileNo, String questionType, String questionNo,
		String questionDate,String subject, String extent, String pendingDueTo) {
	super();
	this.sessionNo = sessionNo;
	this.lokSabhaNo= lokSabhaNo;
	this.sourceName = sourceName;
	this.member = member;
	this.clubbedMember = clubbedMember;
	this.ministry = ministry;
	this.department = department;
	this.assuranceNo = assuranceNo;
	this.fileNo = fileNo;
	this.questionType = questionType;
	this.questionNo = questionNo;
	this.questionDate=questionDate;
	this.subject = subject;
	this.extent=extent;
	this.pendingDueTo = pendingDueTo;
}

}

package com.dsp.cga.report.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ImplementationReportResponseDto {
//	String sessionNo,
//	String committeeDecision, String reason,
	public ImplementationReportResponseDto(String assuranceNo, String questionType, String questionNo,
			String questionDate,String subject, String ministry, String layingDate,  String createdOn) {
		
		super();
		this.assuranceNo = assuranceNo;
		this.questionType = questionType;
		this.questionNo = questionNo;
		this.questionDate = questionDate;
		this.subject = subject;
		this.ministry = ministry;
		this.layingDate = layingDate;
//		this.sessionNo = sessionNo;
//		this.committeeDecision = committeeDecision;
//		this.reason = reason;
		this.createdOn = createdOn;
	}

	String assuranceNo;
	String questionType;
	String questionNo;
	String questionDate;
	String subject;
	String ministry;
	String layingDate;
//	String sessionNo;
//	String committeeDecision;
//	String reason;
	String createdOn;

}

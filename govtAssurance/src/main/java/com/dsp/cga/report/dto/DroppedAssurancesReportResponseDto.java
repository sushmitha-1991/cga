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
public class DroppedAssurancesReportResponseDto {

	String assuranceNo;
	String questionType;
	String questionNo;
//	String questionDate;
	String subject;
	String ministry;
	String ministryRefNo;
	String ministryRefDate;
	String reason;
	String meetingdate;
//	String committeeDecision;
	String updatedOn;

	public DroppedAssurancesReportResponseDto(String assuranceNo, String questionType, String questionNo,
			String subject, String ministry, String ministryRefNo, String ministryRefDate, String reason,
			String meetingdate, String updatedOn) {
		super();
		this.assuranceNo = assuranceNo;
		this.questionType = questionType;
		this.questionNo = questionNo;
		this.subject = subject;
		this.ministry = ministry;
		this.ministryRefNo = ministryRefNo;
		this.ministryRefDate = ministryRefDate;
		this.reason = reason;
		this.meetingdate = meetingdate;
		this.updatedOn = updatedOn;
	}
}

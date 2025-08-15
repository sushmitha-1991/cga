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
public class ExtensionReportResponseDto {

	String assuranceNo;
	 String questionType;
	 String questionNo;
//	String questionDate;
	 String subject;
	 String ministry;
	 String extensionNo;
	String extReqDate;
	String extGrantDate;
	String ministryRefNo;
	String ministryRefDate;
	String extensionRemarks;
	 String updatedOn;
	 
//	 String questionDate,
	public ExtensionReportResponseDto(String assuranceNo, String questionType, String questionNo, 
			String subject, String ministry, String extensionNo, String extReqDate, String extGrantDate,
			String ministryRefNo, String ministryRefDate, String extensionRemarks, String updatedOn) {
		super();
		this.assuranceNo = assuranceNo;
		this.questionType = questionType;
		this.questionNo = questionNo;
//		QuestionDate = questionDate;
		this.subject = subject;
		this.ministry = ministry;
		this.extensionNo = extensionNo;
		this.extReqDate = extReqDate;
		this.extGrantDate = extGrantDate;
		this.ministryRefNo = ministryRefNo;
		this.ministryRefDate = ministryRefDate;
		this.extensionRemarks = extensionRemarks;
		this.updatedOn = updatedOn;
	}
}

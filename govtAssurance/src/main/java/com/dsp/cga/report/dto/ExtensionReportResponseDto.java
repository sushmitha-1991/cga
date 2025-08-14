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

	String AssuranceNo;
	 String QuestionType;
	 String QuestionNo;
//	String QuestionDate;
	 String Subject;
	 String Ministry;
	 String ExtensionNo;
	String ExtReqDate;
	String ExtGrantDate;
	String MinistryRefNo;
	String MinistryRefDate;
	String ExtensionRemarks;
	 String UpdatedOn;
	 
//	 String questionDate,
	public ExtensionReportResponseDto(String assuranceNo, String questionType, String questionNo, 
			String subject, String ministry, String extensionNo, String extReqDate, String extGrantDate,
			String ministryRefNo, String ministryRefDate, String extensionRemarks, String updatedOn) {
		super();
		AssuranceNo = assuranceNo;
		QuestionType = questionType;
		QuestionNo = questionNo;
//		QuestionDate = questionDate;
		Subject = subject;
		Ministry = ministry;
		ExtensionNo = extensionNo;
		ExtReqDate = extReqDate;
		ExtGrantDate = extGrantDate;
		MinistryRefNo = ministryRefNo;
		MinistryRefDate = ministryRefDate;
		ExtensionRemarks = extensionRemarks;
		UpdatedOn = updatedOn;
	}
}

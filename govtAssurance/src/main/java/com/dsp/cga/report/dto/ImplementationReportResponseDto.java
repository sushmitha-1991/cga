package com.dsp.cga.report.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
public class ImplementationReportResponseDto {

//	public String getAssuranceNo();
//	public String getQuestionType();
//	public String getQuestionNo();
//	String getQuestionDate();
//	public String getSubject();
//	public String getMinistry();
//	public String getLayingDate();
//	String getSessionNo();
//	String getCommitteeDecision();
//	String getReason();
//	public String getCreatedOn();
	
	  String assuranceNo;
	     String questionType;
	     String questionNo;
	     String subject;
	     String ministry;
	     String layingDate;
	     String createdOn;
	     
//	     public ImplementationReportResponseDto(Object[] row) {
//	         this.assuranceNo = row[0] != null ? row[0].toString() : null;
//	         this.questionType = row[1] != null ? row[1].toString() : null;
//	         this.questionNo = row[2] != null ? row[2].toString() : null;
//	         this.subject = row[3] != null ? row[3].toString() : null;
//	         this.ministry = row[4] != null ? row[4].toString() : null;
//	         this.layingDate = row[5] != null ? row[5].toString() : null;
//	         this.createdOn = row[6] != null ? row[6].toString() : null;
//	     }
}

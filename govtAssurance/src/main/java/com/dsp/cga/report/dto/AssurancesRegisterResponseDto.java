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
public class AssurancesRegisterResponseDto {

	String assuranceNo;
	String questionNo;
	String questionDate;
	String subject;
	String ministry;
	String implementationDate;
	String layingDate;
	String dropDate;
	String deleteDate;
	String ministryRefNo;
	public AssurancesRegisterResponseDto(String assuranceNo, String questionNo, String questionDate, String subject,
			String ministry, String implementationDate, String layingDate, String dropDate, String deleteDate,
			String ministryRefNo) {
		super();
		this.assuranceNo = assuranceNo;
		this.questionNo = questionNo;
		this.questionDate = questionDate;
		this.subject = subject;
		this.ministry = ministry;
		this.implementationDate = implementationDate;
		this.layingDate = layingDate;
		this.dropDate = dropDate;
		this.deleteDate = deleteDate;
		this.ministryRefNo = ministryRefNo;
	}
}

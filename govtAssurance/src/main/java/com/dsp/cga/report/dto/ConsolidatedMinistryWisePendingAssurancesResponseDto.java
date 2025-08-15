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
public class ConsolidatedMinistryWisePendingAssurancesResponseDto {

	String ministryOrDepartment;
	String pendingAssurances;
	String lessThanTwoYears;
	String betweenTwoAndFiveYears;
	String betweenFiveAndTenYears;
	String moreThanTenYears;
	
	public ConsolidatedMinistryWisePendingAssurancesResponseDto(String ministryOrDepartment, String pendingAssurances,
			String lessThanTwoYears, String betweenTwoAndFiveYears, String betweenFiveAndTenYears,
			String moreThanTenYears) {
		super();
		this.ministryOrDepartment = ministryOrDepartment;
		this.pendingAssurances = pendingAssurances;
		this.lessThanTwoYears = lessThanTwoYears;
		this.betweenTwoAndFiveYears = betweenTwoAndFiveYears;
		this.betweenFiveAndTenYears = betweenFiveAndTenYears;
		this.moreThanTenYears = moreThanTenYears;
	}
}

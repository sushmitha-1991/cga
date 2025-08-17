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
public class ConsolidatedMinistryWiseFimpAssurancesResponseDto {

	String ministryOrDepartment;
	String totalAssurances;
	String totalAssurancesFimp;
	String lessThanTwoYears;
	String betweenTwoAndFiveYears;
	String betweenFiveAndTenYears;
	String moreThanTenYears;
	
	public ConsolidatedMinistryWiseFimpAssurancesResponseDto(String ministryOrDepartment, String totalAssurances,
			String totalAssurancesFimp, String lessThanTwoYears, String betweenTwoAndFiveYears,
			String betweenFiveAndTenYears, String moreThanTenYears) {
		super();
		this.ministryOrDepartment = ministryOrDepartment;
		this.totalAssurances = totalAssurances;
		this.totalAssurancesFimp = totalAssurancesFimp;
		this.lessThanTwoYears = lessThanTwoYears;
		this.betweenTwoAndFiveYears = betweenTwoAndFiveYears;
		this.betweenFiveAndTenYears = betweenFiveAndTenYears;
		this.moreThanTenYears = moreThanTenYears;
	}
}

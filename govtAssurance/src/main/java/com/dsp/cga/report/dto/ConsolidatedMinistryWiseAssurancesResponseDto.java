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
public class ConsolidatedMinistryWiseAssurancesResponseDto {

	String ministry;
	String totalAssurances;
	String totalPendingAssurances;
	String fullyImplementedAssurances;
	String droppedAssurances;
	
	public ConsolidatedMinistryWiseAssurancesResponseDto(String ministry, String totalAssurances,
			String totalPendingAssurances, String fullyImplementedAssurances, String droppedAssurances) {
		super();
		this.ministry = ministry;
		this.totalAssurances = totalAssurances;
		this.totalPendingAssurances = totalPendingAssurances;
		this.fullyImplementedAssurances = fullyImplementedAssurances;
		this.droppedAssurances = droppedAssurances;
	}
	
	
}

package com.dsp.cga.assurance.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DashboardAssurancesWiseCountsResponseDTO {

	String valueId;
	String valueName;
	String assuranceCounts;
}

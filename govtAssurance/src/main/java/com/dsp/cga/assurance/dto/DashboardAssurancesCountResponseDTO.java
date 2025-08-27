package com.dsp.cga.assurance.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DashboardAssurancesCountResponseDTO {

	String abbr;
	String totalAssurances;
	List<DashboardAssurancesWiseCountsResponseDTO> assurancesWiseCountList;
}

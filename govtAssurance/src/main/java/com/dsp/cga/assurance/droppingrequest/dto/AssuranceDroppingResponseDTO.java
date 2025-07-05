package com.dsp.cga.assurance.droppingrequest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssuranceDroppingResponseDTO {

	private Long id;
	private String lokSabhaNo;
	private String lokSabhaSessionNo;
	private String rajyaSabhaSessionNo;
	private String partId;
	private String supplementaryId;
	private String memberName;
	private String departmentName;
	private String ministryName;
	private String droppingType;
	private LocalDate dropRequestDate;
	private LocalDate actualDropDate;
	private String assuranceNo;
	private String status;
	private String remarks;
	private String ministryRefNo;
	private List<Map<String, Object>> droppingFiles;
	private String questionNo;
	private String subject;
	private LocalDate ministryRefDate;
	private String statusForMinistry;
	private String statusForCga;

	private List<MemberDTO> clubbedMembersData;

	private List<MemberDTO> supplementaryClubbedMembersData;
}
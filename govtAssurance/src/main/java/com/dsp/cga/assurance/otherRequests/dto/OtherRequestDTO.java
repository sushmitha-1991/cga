package com.dsp.cga.assurance.otherRequests.dto;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtherRequestDTO {

	private Long id;

	private String assuranceNo;
	private String lokSabhaNumber;
	private String lokSabhaSessionNumber;
	private String rajyaSabhaSessionNumber;
	private String extensionNo;
	private String status;
	private Long assuranceId;

	private String departmentName;
	private String ministryName;
	private String memberName;
	private String questionNo;
	private String statusForMinistry;
	private LocalDate requestedDate;
	private List<MemberDTO> clubbedMembersData;
	private List<MemberDTO> supplementaryClubbedMembersData;
	private Long statusId;

	private String remarks;

	private String subject;

	private List<Map<String, Object>> fileUrl;

	private String fromMinistry;
	
	private String statusForCga;
	
	@Builder.Default
	private Boolean draftStatus = Boolean.FALSE;

}

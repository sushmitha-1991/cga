package com.dsp.cga.assurance.extensionrequest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtensionRequestResponseDTO {

	private Long id;
	private String assuranceNo;
    private String lokSabhaNo;
    private Long sessionNo;
    private String extensionNo;
    private String status;
    private String remarks;
    private LocalDate extGrantedTillDate;
    private LocalDate extRequestedTillDate;
    private String ministryRefNo;
    private String questionNo;
    private String memberName;
    private String ministryName;
	private LocalDate ministryRefDate;
	private String lokSabhaSessionNo;
    private String subject;
    private String departmentName;
	private String rajyaSabhaSessionNumber;
    private String statusForMinistry;
    private List<Map<String, Object>> extensionFiles;
    private List<MemberDTO> clubbedMembersData;
    private List<MemberDTO> supplementaryClubbedMembersData;
}

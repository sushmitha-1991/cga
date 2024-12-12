package com.dsp.cga.assurance.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssuranceDTO {

	 private Long id;
	    private Long lokSabhaNo;
	    private Long lokSabhaSessionNo;
	    private Long rajyaSabhaSessionNo;
	    private LocalDate assuranceDate;
	    private Long categoryId;
	    private String questionNo;
	    private Long assuranceTypeId;
	    private String subject;
	    private Long memberId;
	    private Long ministryId;
	    private Long departmentId;
	    private String pageNo;
	    private String fileNo;
	    private Long statusId;
	    private Long pendingDueTo;
	    private LocalDate mpaRefDate;
	    private String mpaRefNo;
	    private String reportNo;
	    private String remarks;
	    private LocalDate dueDateOfImplementation;
	    private LocalDate dateOfImplementation;
	    private String detailsOfImplementation;
	    private String textOfAssurance;
	  //  private Boolean supplementary;
	    private String supplementaryPageNo;
	    private Long supplementaryMemberId;
	    private Boolean draftStatus;
	    private Long sourceId;
	    private List<Long> clubbedMembers;
	    private List<PartDetailsDTO> partDetails;
	    private List<SupplementaryDTO> supplementary;
	    private String lokSabhaSession;
	   
	    private String categoryName;
	    private String memberName;
	    private String statusName;
	    private String ministryName;
	    private String departmentName;
	    private String assuranceType;
	    private String assuranceNo;
	    private String pageNoCorrected;
	    private String pageNoUncorrected;
	    private String groupName;
	    private String otherPendingDueTo;
	    private MemberDTO memberDTO;
	    private MinistryDTO ministryDTO;
	    private DepartmentsDTO departmentDTO;
	    private StatusDTO statusDTO;
	    private AssuranceTypeDTO assuranceTypeDTO;
	    private RajyaSabhaSessionNoDTO RajyaSabhaSessionNoDTO;
	    private LoksabhaSessionNoDTO lokSabhaSessionNoDTO;
	    private SourceDTO sourceDTO;
		private List<Map<String, Object>> assuranceFiles;

	    private PendingDueToDTO pendingDueToDTO;
	    private LokSabhaNoDTO lokSabhaNoDTO;
	   // private LoksabhaSessionNoDTO loksabhaSessionNoDTO;
	    private List<MemberDTO> clubbedMembersData = new ArrayList<>();
  
}

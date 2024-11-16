package com.dsp.govtAssurance.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.dsp.govtAssurance.supplementry.entity.Supplementary;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
	    private String source;
	    private List<Long> clubbedMembers;
	    private List<PartDetailsDTO> partDetails;
	    private List<SupplementaryDTO> supplementary;
	    private String lokSabhaName;
	    private String categoryName;
	    private String memberName;
	    private String statusName;
	    private String ministryName;
	    private String departmentname;
	    private String assuranceType;
	    private String assuranceNo;
	    private String pageNoCorrected;
	    private String pageNoUncorrected;
  
}

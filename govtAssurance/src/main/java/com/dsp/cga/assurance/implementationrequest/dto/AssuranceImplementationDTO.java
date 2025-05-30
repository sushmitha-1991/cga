package com.dsp.cga.assurance.implementationrequest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.dto.CommitteeDecisionDTO;
import com.dsp.cga.assurance.dto.ImplementationTypeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssuranceImplementationDTO {
	
	 private String assuranceNo;
	   // private Long lokSabhaNo;
	    //private Long lokSabhaSessionNo;
	  //  private Long statusId;
	 private Long id;
	    private LocalDate dueDateOfImplementation;
	    private String detailsOfImplementation;
	    private LocalDate dateOfImplementation;
	    private String status;
	    private String lokSabhaNumber;
	    private String lokSabhaSessionNumber;
	    private String rajyaSabhaSessionNumber;
	    private String supplementaryId;
	    private String partId;
	    private String implementationTypeId;
		private String reason;
		private String statusForMinistry;
		private String remarks;
	//	private String detailsOfImplementation;
		private String ministryName;
		private String departmentName;
		private String memberName;
		private LocalDate layingDate;
		private String house;
		private String questionNo;
		private String subject;
		private String applicationNo;
		  private Long applicationId;
		  private Long refId;
		  private String taskId;
		  private String currentStatus;
		  private Long committeeDecisionId;
		  // private String statusForMinistry;
		    private String statementNo;
		 
		   
		    private Long session;
		  
		    private List<Map<String, Object>> implementationReportFiles;
		    private List<Map<String, Object>> partReportFiles;
		    private List<Map<String, Object>> otherFiles;
	    

}

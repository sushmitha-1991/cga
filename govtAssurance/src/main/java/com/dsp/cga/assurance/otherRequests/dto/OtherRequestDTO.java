package com.dsp.cga.assurance.otherRequests.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private String lokSabhaNo;
    private Long sessionNo;
    private String extensionNo;
    private String status;
	private Long assuranceId;

	 private LocalDate requestedDate;
	
	    private Long statusId; 
	   
	    private String remarks;
	    
	    private String subject;
	   
		private List<Map<String, Object>> fileUrl;

}

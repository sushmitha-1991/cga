package com.dsp.cga.assurance.extensionrequest.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;

public class ExtensionRequest {
	
	 @Column(name = "assurance_id", nullable = false)
	    private Long assuranceId;

	    @Column(name = "question_no", length = 10)
	    private String questionNo;

	    @Column(name = "extension_no", nullable = false)
	    private Integer extensionNo;

	    @Column(name = "ext_requested_till_date")
	    private LocalDate extRequestedTillDate;

	    @Column(name = "ext_granted_till_date")
	    private LocalDate extGrantedTillDate;

	    @Column(name = "ministry_ref_no", length = 50)
	    private String ministryRefNo;

	    @Column(name = "ministry_ref_date")
	    private LocalDate ministryRefDate;

	    @Column(name = "remarks", length = 512)
	    private String remarks;

	    @Column(name = "extension_files", columnDefinition = "jsonb")
	    private String extensionFiles;

	    @Column(name = "draft_status", nullable = false)
	    private Boolean draftStatus = false;

}

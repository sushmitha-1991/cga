package com.dsp.govtAssurance.external.service;

public interface ExternalService {

	String getAssuranceType(Long assuranceTypeId);

	String getMemberName(Long memberId);

	String getMinistryName(Long ministryId);
	
	String getLokSabhaName(Long lokSabhaId);
	
	String getCategoryName(Long categoryId);
	
	String getStatusName(Long statusId);
	
	String getDepartmentName(Long departmentId);
	
	

}

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
public class DashboardAssurancesDetailsResponseDTO {

	String questionNo;
	String assuranceNo;
	String category;
	String sessionNo;
	String lokSabhaNo;
	String assuranceDate;
	String member;
	String ministry;
	String department;
	String questionDate;
	String subject;
	String questionType;
	String status;
	String implementationDetails;
	String remarks;
}

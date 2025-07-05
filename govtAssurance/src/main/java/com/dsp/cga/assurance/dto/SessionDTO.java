package com.dsp.cga.assurance.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionDTO {
	Long lokSabhaNoId;
	Long lokSabhaSessionNoId;
	Long rajyaSabhaSessionNoId;

	String lokSabhaNumber;
	String sessionNumber;

	LocalDate sessionStart;
	LocalDate sessionEnd;
}
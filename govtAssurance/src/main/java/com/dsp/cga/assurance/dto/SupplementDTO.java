package com.dsp.cga.assurance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplementDTO {
	
	private Long id;
	private String code;

}
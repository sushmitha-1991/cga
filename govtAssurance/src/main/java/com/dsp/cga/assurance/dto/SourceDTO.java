package com.dsp.cga.assurance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SourceDTO {
	
	private Long id;


  //private String typeName;

  private Long codeTypeId;

  private String code;
//  private String abbr;
//  private String description;
//  private Integer displaySequence;
//  private String locale;

}

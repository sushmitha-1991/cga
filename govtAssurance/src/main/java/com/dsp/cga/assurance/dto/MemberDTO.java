package com.dsp.cga.assurance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberDTO {

	private Long id;
	
	private Long memberId;
	
	private String fullName;

	private String firstName;

	private String LastName;

	private Long userId;

	// private String lastNameHindi;

}

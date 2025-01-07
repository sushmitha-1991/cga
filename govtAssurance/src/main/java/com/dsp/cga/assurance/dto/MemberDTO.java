package com.dsp.cga.assurance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDTO {

	private Long id;
	
	private Long memberId;
	
	private String memberName;
	
	private String fullName;

	private String firstName;
	
	private String middleName;

	private String LastName;

	private Long userId;

	// private String lastNameHindi;
	
	// Method to generate memberName
	public void generateMemberName() {
		if (fullName != null && !fullName.isEmpty()) {
			memberName = fullName;
		} else {
			// Combine firstName, middleName, and lastName if fullName is not available
			StringBuilder nameBuilder = new StringBuilder();

			if (firstName != null && !firstName.isEmpty()) {
				nameBuilder.append(firstName);
			}

			if (middleName != null && !middleName.isEmpty()) {
				if (nameBuilder.length() > 0) nameBuilder.append(" "); // Add space if firstName exists
				nameBuilder.append(middleName);
			}

			if (LastName != null && !LastName.isEmpty()) {
				if (nameBuilder.length() > 0) nameBuilder.append(" "); // Add space if previous names exist
				nameBuilder.append(LastName);
			}

			memberName = nameBuilder.toString();
		}
	}

}

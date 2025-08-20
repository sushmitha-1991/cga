package com.dsp.cga.assurance.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestHistoryDto {

	 Long id;
	 Long requestId;
	 String requestType;
	 String actionType;
	 List<Map<String, Object>> referenceFiles;
	 Long pendingWith;
	 Long actionTakenBy;
	 String ipAddress;
	 String actionTakenByName;
	 String pendingWithName;
	 String commentForDisplay;
}

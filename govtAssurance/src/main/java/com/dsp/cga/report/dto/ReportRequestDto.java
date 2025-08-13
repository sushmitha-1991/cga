package com.dsp.cga.report.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level=AccessLevel.PRIVATE)
public class ReportRequestDto {

	LocalDateTime fromDate;
	LocalDateTime toDate;
}

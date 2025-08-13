package com.dsp.cga.report.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsp.cga.report.dto.ImplementationReportResponseDto;
import com.dsp.cga.report.repository.ReportRepository;
import com.dsp.cga.report.service.ReportService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepo;

	@Override
	public List<ImplementationReportResponseDto> getImplementationReport(LocalDateTime fromDate,LocalDateTime toDate) {
//		fromDate.isEmpty() || toDate.isEmpty() || 
		if (fromDate == null || toDate == null) {
			throw new IllegalArgumentException("fromDate/toDate must not be blank");
		}
		 List<Object[]> results = reportRepo.fetchImplementationReports(fromDate, toDate);
	        return results.isEmpty() ? Collections.emptyList() : results.stream()
	                .map(row -> new ImplementationReportResponseDto(
	                				row[0] != null ? row[0].toString() : null, // assuranceNo
	                                row[1] != null ? row[1].toString() : null, // questionType
	                                row[2] != null ? row[2].toString() : null, // questionNo
	                                row[3] != null ? row[3].toString() : null, // subject
	                                row[4] != null ? row[4].toString() : null, // ministry
	                                row[5] != null ? row[5].toString() : null, // layingDate
	                                row[6] != null ? row[6].toString() : null  // createdOn
	                		))
	                .toList();
	}
}

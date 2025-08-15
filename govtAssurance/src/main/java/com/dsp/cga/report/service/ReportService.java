package com.dsp.cga.report.service;

import java.time.LocalDate;
import java.util.List;

import com.dsp.cga.report.dto.ExtensionReportResponseDto;
import com.dsp.cga.report.dto.ImplementationReportResponseDto;

public interface ReportService {

	public List<ImplementationReportResponseDto> getImplementationReport(LocalDate fromDate, LocalDate  toDate);
	public List<ExtensionReportResponseDto> getExtensionReport();
}

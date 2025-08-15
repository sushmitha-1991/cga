package com.dsp.cga.report.service;

import java.time.LocalDate;
import java.util.List;

import com.dsp.cga.report.dto.ConsolidatedMinistryWiseFimpAssurancesResponseDto;
import com.dsp.cga.report.dto.ConsolidatedMinistryWisePendingAssurancesResponseDto;
import com.dsp.cga.report.dto.DroppedAssurancesReportResponseDto;
import com.dsp.cga.report.dto.ExtensionReportResponseDto;
import com.dsp.cga.report.dto.ImplementationReportResponseDto;
import com.dsp.cga.report.dto.MinistryWisePendingAssurancesResponseDto;
import com.dsp.cga.report.dto.PendingAssurancesListResponseDto;

public interface ReportService {

	public List<ImplementationReportResponseDto> getImplementationReport(LocalDate fromDate, LocalDate  toDate);
	public List<ExtensionReportResponseDto> getExtensionReport();
	public List<DroppedAssurancesReportResponseDto> getDroppedAssuranceReport();
	public List<PendingAssurancesListResponseDto> getPendingAssurancesReport();
	public List<MinistryWisePendingAssurancesResponseDto> getMinistryWisePendingAssurancesReport();
	public List<ConsolidatedMinistryWisePendingAssurancesResponseDto> getConsolidatedMinistryWisePendingAssurancesReport();
	public List<ConsolidatedMinistryWiseFimpAssurancesResponseDto> getConsolidatedMinistryWiseFimpAssurancesReport();
}

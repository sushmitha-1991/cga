package com.dsp.cga.report.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsp.cga.report.dto.ExtensionReportResponseDto;
import com.dsp.cga.report.dto.ImplementationReportResponseDto;
import com.dsp.cga.report.repository.ReportRepository;
import com.dsp.cga.report.service.ReportService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepo;
	
	public LocalDate addDatePlusOne(LocalDate date)
	{
		return date.plusDays(1); //Add 1 Day to toDate filter
	}

	@Override
	public List<ImplementationReportResponseDto> getImplementationReport(LocalDate fromDate,LocalDate toDate) {
		if (fromDate == null || toDate == null) {
			throw new IllegalArgumentException("fromDate/toDate must not be blank");
		}
		List<ImplementationReportResponseDto> results = reportRepo.fetchImplementationReports(fromDate, addDatePlusOne(toDate));
		return results.isEmpty() ? Collections.emptyList() : results;
	}
	
	@Override
	public List<ExtensionReportResponseDto> getExtensionReport(LocalDate fromDate,LocalDate toDate) {
		if (fromDate == null || toDate == null) {
			throw new IllegalArgumentException("fromDate/toDate must not be blank");
		}
		System.out.println("-----------------Inside extension report service----------------");
		List<ExtensionReportResponseDto> results = reportRepo.fetchExtensionReports(fromDate, addDatePlusOne(toDate));
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results;
	}
}

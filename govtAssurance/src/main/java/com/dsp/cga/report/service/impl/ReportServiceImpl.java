package com.dsp.cga.report.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.dsp.cga.report.dto.ExtensionReportResponseDto;
import com.dsp.cga.report.dto.ImplementationReportResponseDto;
import com.dsp.cga.report.repository.ReportRepository;
import com.dsp.cga.report.service.ReportService;
import com.dsp.cga.util.DateFormatterUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepo;
	
	LocalDate today=LocalDate.now();

	@Override
	public List<ImplementationReportResponseDto> getImplementationReport(LocalDate fromDate,LocalDate toDate) {
		List<Object[]> results=new ArrayList<Object[]>(); 
		if(toDate == null) {
			 results = reportRepo.fetchImplementationReports(fromDate, toDate);
		}
		else {
		 results = reportRepo.fetchImplementationReports(fromDate, DateFormatterUtil.addDatePlusOne(toDate));
		}
		 return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new ImplementationReportResponseDto(
                				row[0] != null ? row[0].toString() : null, // assuranceNo
                                row[1] != null ? row[1].toString() : null, // questionType
                                row[2] != null ? row[2].toString() : null, // questionNo
                                row[3] != null ? row[3].toString() : null, // subject
                                row[4] != null ? row[4].toString() : null, // ministry
                                row[5] != null ? row[5].toString() : null, // layingDate
                                row[6] != null ? (DateFormatterUtil.format(row[6].toString())) : null  // createdOn  
//                                row[7] != null ? row[7].toString() : null,
//                                row[8] != null ? row[8].toString() : null,
//                                row[9] != null ? row[9].toString() : null,
//                                row[10] != null ? row[10].toString() : null
                		))
                .toList();
	}
	
	@Override
	public List<ExtensionReportResponseDto> getExtensionReport(LocalDate fromDate,LocalDate toDate) {
		if (fromDate == null || toDate == null) {
			throw new IllegalArgumentException("fromDate/toDate must not be blank");
		}
		System.out.println("-----------------Inside extension report service----------------");
		List<ExtensionReportResponseDto> results = reportRepo.fetchExtensionReports(fromDate, DateFormatterUtil.addDatePlusOne(today));
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results;
	}
}

package com.dsp.cga.report.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsp.cga.report.dto.ConsolidatedMinistryWiseFimpAssurancesResponseDto;
import com.dsp.cga.report.dto.ConsolidatedMinistryWisePendingAssurancesResponseDto;
import com.dsp.cga.report.dto.DroppedAssurancesReportResponseDto;
import com.dsp.cga.report.dto.ExtensionReportResponseDto;
import com.dsp.cga.report.dto.ImplementationReportResponseDto;
import com.dsp.cga.report.dto.MinistryWisePendingAssurancesResponseDto;
import com.dsp.cga.report.dto.PendingAssurancesListResponseDto;
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
                                row[6] != null ? (DateFormatterUtil.formatDateTime(row[6].toString())) : null  // createdOn  
//                                row[7] != null ? row[7].toString() : null,
//                                row[8] != null ? row[8].toString() : null,
//                                row[9] != null ? row[9].toString() : null,
//                                row[10] != null ? row[10].toString() : null
                		))
                .toList();
	}
	
	@Override
	public List<ExtensionReportResponseDto> getExtensionReport() {
		System.out.println("-----------------Inside extension report service----------------");
		List<Object[]> results = reportRepo.fetchExtensionReports();
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
//		return results.isEmpty() ? Collections.emptyList() : results;
		
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new ExtensionReportResponseDto(
                				row[0] != null ? row[0].toString() : null, // assuranceNo
                                row[1] != null ? row[1].toString() : null, // questionType
                                row[2] != null ? row[2].toString() : null, // questionNo
                                row[3] != null ? row[3].toString() : null, // subject
                                row[4] != null ? row[4].toString() : null, // ministry
                                row[5] != null ? row[5].toString() : null, // extensionNo
                                row[6] != null ? (DateFormatterUtil.formatDate(row[6].toString())) : null,  // extReqDate  
                                row[7] != null ? (DateFormatterUtil.formatDate(row[7].toString())) : null, // extGrantDate
                                row[8] != null ? row[8].toString() : null, //ministryRefNo
                                row[9] != null ? (DateFormatterUtil.formatDate(row[9].toString())) : null, //ministryRefDate
                                row[10] != null ? row[10].toString() : null, //extensionRemarks
                                row[11] != null ? (DateFormatterUtil.formatDateTime(row[11].toString())) : null //updatedOn
                                		
                		))
                .toList();
	}
	
	@Override
	public List<DroppedAssurancesReportResponseDto> getDroppedAssuranceReport() {
		System.out.println("-----------------Inside Dropped Assurance report service----------------");
		List<Object[]> results = reportRepo.fetchDroppedAssurancesReports();
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new DroppedAssurancesReportResponseDto(
                				row[0] != null ? row[0].toString() : null, // assuranceNo
                                row[1] != null ? row[1].toString() : null, // questionType
                                row[2] != null ? row[2].toString() : null, // questionNo
                                row[3] != null ? row[3].toString() : null, // subject
                                row[4] != null ? row[4].toString() : null, // ministry
                                row[5] != null ? row[5].toString() : null, // ministryRefNo
                                row[6] != null ? (DateFormatterUtil.formatDate(row[6].toString())) : null,  // ministryRefDate  
                                row[7] != null ? row[7].toString() : null, // reason
                                row[8] != null ? (DateFormatterUtil.formatDate(row[8].toString())) : null, //meetingDate
                                row[9] != null ? (DateFormatterUtil.formatDateTime(row[9].toString())) : null //updatedOn
                                		
                		))
                .toList();
	}
	
	@Override
	public List<PendingAssurancesListResponseDto> getPendingAssurancesReport() {
		List<Object[]> results = reportRepo.fetchPendingAssurancesLists();
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new PendingAssurancesListResponseDto(
                				row[0] != null ? row[0].toString() : null, // sessionNo
                                row[1] != null ? row[1].toString() : null, // sourceName
                                row[2] != null ? row[2].toString() : null, // member
                                row[3] != null ? row[3].toString() : null, // clubbedMember
                                row[4] != null ? row[4].toString() : null, // ministry
                                row[5] != null ? row[5].toString() : null, // department
                                row[6] != null ? row[6].toString() : null,  // assuranceNo  
                                row[7] != null ? row[7].toString() : null, // fileNo
                                row[8] != null ? row[8].toString() : null, //questionType
                                row[9] != null ? row[9].toString() : null, //questionNo
                                row[10] != null ? row[10].toString() : null, //subject
                                row[11] != null ? row[11].toString() : null //pendingDueTo
                		))
                .toList();
	}
	
	@Override
	public List<MinistryWisePendingAssurancesResponseDto> getMinistryWisePendingAssurancesReport() {
		List<Object[]> results = reportRepo.fetchMinistryWisePendingAssurancesLists();
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new MinistryWisePendingAssurancesResponseDto(
                				row[0] != null ? row[0].toString() : null, // ministry
                                row[1] != null ? row[1].toString() : null, // sessionNo
                                row[2] != null ? row[2].toString() : null, // member
                                row[3] != null ? row[3].toString() : null, // assuranceNo
                                row[4] != null ? row[4].toString() : null, // questionNo
                                row[5] != null ? row[5].toString() : null, // questionType
                                row[6] != null ? row[6].toString() : null,  // subject
                                row[7] != null ? row[7].toString() : null  // pendingDueTo
                                
                		))
                .toList();
	}
	
	@Override
	public List<ConsolidatedMinistryWisePendingAssurancesResponseDto> getConsolidatedMinistryWisePendingAssurancesReport() {
		List<Object[]> results = reportRepo.fetchConsolidatedMinistryWisePendingAssurancesLists();
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new ConsolidatedMinistryWisePendingAssurancesResponseDto(
                				row[0] != null ? row[0].toString() : null, // ministryOrDepartment
                                row[1] != null ? row[1].toString() : null, // pendingAssurances
                                row[2] != null ? row[2].toString() : null, // lessThanTwoYears
                                row[3] != null ? row[3].toString() : null, // betweenTwoAndFiveYears
                                row[4] != null ? row[4].toString() : null, // betweenFiveAndTenYears
                                row[5] != null ? row[5].toString() : null // moreThanTenYears
                                
                		))
                .toList();
	}
	
	@Override
	public List<ConsolidatedMinistryWiseFimpAssurancesResponseDto> getConsolidatedMinistryWiseFimpAssurancesReport() {
		List<Object[]> results = reportRepo.fetchConsolidatedMinistryWiseFimpAssurancesLists();
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new ConsolidatedMinistryWiseFimpAssurancesResponseDto(
                				row[0] != null ? row[0].toString() : null, // ministryOrDepartment
                                row[1] != null ? row[1].toString() : null, // totalAssurances
                                row[2] != null ? row[2].toString() : null, // totalAssurancesFimp		
                                row[3] != null ? row[3].toString() : null, // lessThanTwoYears
                                row[4] != null ? row[4].toString() : null, // betweenTwoAndFiveYears
                                row[5] != null ? row[5].toString() : null, // betweenFiveAndTenYears
                                row[6] != null ? row[6].toString() : null // moreThanTenYears
                                
                		))
                .toList();
	}
}

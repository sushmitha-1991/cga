package com.dsp.cga.report.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.report.dto.AssurancesRegisterResponseDto;
import com.dsp.cga.report.dto.ClubbingDetailsResponseDto;
import com.dsp.cga.report.dto.ConsolidatedMinistryWiseAssurancesResponseDto;
import com.dsp.cga.report.dto.ConsolidatedMinistryWiseFimpAssurancesResponseDto;
import com.dsp.cga.report.dto.ConsolidatedMinistryWisePendingAssurancesResponseDto;
import com.dsp.cga.report.dto.DeletedAssurancesResponseDto;
import com.dsp.cga.report.dto.DroppedAssurancesReportResponseDto;
import com.dsp.cga.report.dto.ExtensionReportResponseDto;
import com.dsp.cga.report.dto.ImplementationReportResponseDto;
import com.dsp.cga.report.dto.MinistryWisePendingAssurancesResponseDto;
import com.dsp.cga.report.dto.PendingAssurancesListResponseDto;
import com.dsp.cga.report.dto.QuarterlyReportResponseDto;
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
		Long houseId=getHouseId();
		List<Object[]> results=new ArrayList<Object[]>(); 
		if(toDate == null) {
			 results = reportRepo.fetchImplementationReports(houseId,fromDate, toDate);
		}
		else {
		 results = reportRepo.fetchImplementationReports(houseId,fromDate, DateFormatterUtil.addDatePlusOne(toDate));
		}
		 return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new ImplementationReportResponseDto(
                				row[0] != null ? row[0].toString() : null, // assuranceNo
                                row[1] != null ? row[1].toString() : null, // questionType
                                row[2] != null ? row[2].toString() : null, // questionNo
                                row[3] != null ? (DateFormatterUtil.formatDateTime(row[3].toString())) : null, //questionDate		
                                row[4] != null ? row[4].toString() : null, // subject
                                row[5] != null ? row[5].toString() : null, // ministry
                                row[6] != null ? (DateFormatterUtil.formatDate(row[6].toString())) : null, // layingDate
                                row[7] != null ? row[7].toString() : null, //sessionNo
                                row[8] != null ? row[8].toString() : null, //lokSabhaNo if houseId=2
                                row[9] != null ? row[9].toString() : null, //committeeDecision
                                row[10] != null ? row[10].toString() : null, //reason
                                row[11] != null ? (DateFormatterUtil.formatDateTime(row[11].toString())) : null  // createdOn  
                                
                		))
                .toList();
	}
	
	@Override
	public List<ExtensionReportResponseDto> getExtensionReport() {
		Long houseId=getHouseId();
		System.out.println("-----------------Inside extension report service----------------");
		List<Object[]> results = reportRepo.fetchExtensionReports(houseId);
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
                                row[3] != null ? (DateFormatterUtil.formatDateTime(row[3].toString())) : null, //questionDate		
                                row[4] != null ? row[4].toString() : null, // subject
                                row[5] != null ? row[5].toString() : null, // ministry
                                row[6] != null ? row[6].toString() : null, // extensionNo
                                row[7] != null ? (DateFormatterUtil.formatDate(row[7].toString())) : null,  // extReqDate  
                                row[8] != null ? (DateFormatterUtil.formatDate(row[8].toString())) : null, // extGrantDate
                                row[9] != null ? row[9].toString() : null, //ministryRefNo
                                row[10] != null ? (DateFormatterUtil.formatDate(row[10].toString())) : null, //ministryRefDate
                                row[11] != null ? row[11].toString() : null, //extensionRemarks
                                row[12] != null ? (DateFormatterUtil.formatDateTime(row[12].toString())) : null //updatedOn
                                		
                		))
                .toList();
	}
	
	@Override
	public List<DroppedAssurancesReportResponseDto> getDroppedAssuranceReport() {
		Long houseId=getHouseId();
		System.out.println("-----------------Inside Dropped Assurance report service----------------");
		List<Object[]> results = reportRepo.fetchDroppedAssurancesReports(houseId);
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
                                row[3] != null ? (DateFormatterUtil.formatDateTime(row[3].toString())) : null, //questionDate	
                                row[4] != null ? row[4].toString() : null, // subject
                                row[5] != null ? row[5].toString() : null, // ministry
                                row[6] != null ? row[6].toString() : null, // ministryRefNo
                                row[7] != null ? (DateFormatterUtil.formatDate(row[7].toString())) : null,  // ministryRefDate  
                                row[8] != null ? row[8].toString() : null, // reason
                                row[9] != null ? (DateFormatterUtil.formatDate(row[9].toString())) : null, //meetingDate
                                row[10] != null ? (DateFormatterUtil.formatDateTime(row[10].toString())) : null //updatedOn
                                		
                		))
                .toList();
	}
	
	@Override
	public List<PendingAssurancesListResponseDto> getPendingAssurancesReport() {
		Long houseId=getHouseId();
		List<Object[]> results = reportRepo.fetchPendingAssurancesLists(houseId);
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new PendingAssurancesListResponseDto(
                				row[0] != null ? row[0].toString() : null, // sessionNo
                				row[1] != null ? row[1].toString() : null, //lokSabhNo if house id=2
                                row[2] != null ? row[2].toString() : null, // sourceName
                                row[3] != null ? row[3].toString() : null, // member
                                row[4] != null ? row[4].toString() : null, // clubbedMember
                                row[5] != null ? row[5].toString() : null, // ministry
                                row[6] != null ? row[6].toString() : null, // department
                                row[7] != null ? row[7].toString() : null,  // assuranceNo  
                                row[8] != null ? row[8].toString() : null, // fileNo
                                row[9] != null ? row[9].toString() : null, //questionType
                                row[10] != null ? row[10].toString() : null, //questionNo
                                row[11] != null ? (DateFormatterUtil.formatDateTime(row[11].toString())) : null,	//questionDate	
                                row[12] != null ? row[12].toString() : null, //subject
                                row[13] != null ? (DateFormatterUtil.formatDate(row[13].toString())) : null,	//extentDate
                                row[14] != null ? row[14].toString() : null //pendingDueTo
                		))
                .toList();
	}
	
	@Override
	public List<MinistryWisePendingAssurancesResponseDto> getMinistryWisePendingAssurancesReport() {
		Long houseId=getHouseId();
		List<Object[]> results = reportRepo.fetchMinistryWisePendingAssurancesLists(houseId);
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
                                row[2] != null ? row[2].toString() : null, //lokSabhNo if house id=2
                                row[3] != null ? row[3].toString() : null, // member
                                row[4] != null ? row[4].toString() : null, // assuranceNo
                                row[5] != null ? row[5].toString() : null, // questionNo
                                row[6] != null ? row[6].toString() : null, // questionType
                                row[7] != null ? (DateFormatterUtil.formatDateTime(row[7].toString())) : null,	//questionDate
                                row[8] != null ? row[8].toString() : null,  // subject
                                row[9] != null ? (DateFormatterUtil.formatDate(row[9].toString())) : null,	//extentDate
                                row[10] != null ? row[10].toString() : null  // pendingDueTo
                                
                		))
                .toList();
	}
	
	@Override
	public List<ConsolidatedMinistryWisePendingAssurancesResponseDto> getConsolidatedMinistryWisePendingAssurancesReport() {
		Long houseId=getHouseId();
		List<Object[]> results = reportRepo.fetchConsolidatedMinistryWisePendingAssurancesLists(houseId);
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
		Long houseId=getHouseId();
		List<Object[]> results = reportRepo.fetchConsolidatedMinistryWiseFimpAssurancesLists(houseId);
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
	
	@Override
	public List<ConsolidatedMinistryWiseAssurancesResponseDto> getConsolidatedMinistryWiseAssurancesReport() {
		Long houseId=getHouseId();
		List<Object[]> results = reportRepo.fetchConsolidatedMinistryWiseAssurancesDetails(houseId);
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new ConsolidatedMinistryWiseAssurancesResponseDto(
                				row[0] != null ? row[0].toString() : null, // ministry
                                row[1] != null ? row[1].toString() : null, // totalAssurances
                                row[2] != null ? row[2].toString() : null, // totalPendingAssurances		
                                row[3] != null ? row[3].toString() : null, // fullyImplementedAssurances
                                row[4] != null ? row[4].toString() : null // droppedAssurances
                		))
                .toList();
	}
	
	@Override
	public List<ClubbingDetailsResponseDto> getClubbingDetailsReport() {
		Long houseId=getHouseId();
		List<Object[]> results = reportRepo.fetchClubbingDetails(houseId);
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new ClubbingDetailsResponseDto(
                				row[0] != null ? row[0].toString() : null, // assuranceNo
                                row[1] != null ? row[1].toString() : null, // questionType
                                row[2] != null ? row[2].toString() : null, // questionNo	
                                row[3] != null ? (DateFormatterUtil.formatDateTime(row[3].toString())) : null, // questionDate
                                row[4] != null ? row[4].toString() : null, // subject
                                row[5] != null ? row[5].toString() : null, // ministry
                                row[6] != null ? row[6].toString() : null, // totalClubbedAssurances
                                row[7] != null ? row[7].toString() : null // clubbedAssurancesList
                		))
                .toList();
	}
	
	@Override
	public List<DeletedAssurancesResponseDto> getDeletedAssurancesReport() {
		Long houseId=getHouseId();
		List<Object[]> results = reportRepo.fetchDeletedAssurancesReport(houseId);
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new DeletedAssurancesResponseDto(
                				row[0] != null ? row[0].toString() : null, // assuranceNo
                                row[1] != null ? row[1].toString() : null, // questionType
                                row[2] != null ? row[2].toString() : null, // questionNo	
                                row[3] != null ? (DateFormatterUtil.formatDateTime(row[3].toString())) : null, // questionDate
                                row[4] != null ? row[4].toString() : null, // subject
                                row[5] != null ? (DateFormatterUtil.formatDateTime(row[5].toString())) : null // deletionDate
                		))
                .toList();
	}
	
	@Override
	public List<AssurancesRegisterResponseDto> getAssuranceRegisterReport() {
		Long houseId=getHouseId();
		List<Object[]> results = reportRepo.fetchAssuranceRegisterReport(houseId);
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new AssurancesRegisterResponseDto(
                				row[0] != null ? row[0].toString() : null, // assuranceNo
                                row[1] != null ? row[1].toString() : null, // questionNo	
                                row[2] != null ? (DateFormatterUtil.formatDateTime(row[2].toString())) : null, // questionDate
                                row[3] != null ? row[3].toString() : null, // subject
                                row[4] != null ? row[4].toString() : null, // ministry	
                                row[5] != null ? (DateFormatterUtil.formatDate(row[5].toString())) : null, // implementationDate
                                row[6] != null ? (DateFormatterUtil.formatDate(row[6].toString())) : null, // layingdate
                                row[7] != null ? (DateFormatterUtil.formatDate(row[7].toString())) : null, // dropDate
                                row[8] != null ? (DateFormatterUtil.formatDateTime(row[8].toString())) : null, // deletionDate
                                row[9] != null ? row[9].toString() : null // ministryRefNo
                		))
                .toList();
	}
	
	@Override
	public List<QuarterlyReportResponseDto> getQuarterlyReport(LocalDate inputDate) {
		Long houseId=getHouseId();
		List<Object[]> results = reportRepo.fetchQuarterlyReport(houseId,inputDate);
		if(results.size()>0) {
		System.out.println(" result :"+results);}
		else
		{
			System.out.println(" list empty :"+results);
		}
		return results.isEmpty() ? Collections.emptyList() : results.stream()
                .map(row -> new QuarterlyReportResponseDto(
                				row[0] != null ? row[0].toString() : null, // ministry
                                row[1] != null ? row[1].toString() : null, // pendingStartQrtr	
                                row[2] != null ? row[2].toString() : null, // addedQrtr
                                row[3] != null ? row[3].toString() : null, // droppedQrtr
                                row[4] != null ? row[4].toString() : null, // laidQrtr	
                                row[5] != null ? row[5].toString() : null, // lessThanSixMonths
                                row[6] != null ? row[6].toString() : null, // betweenSixAndTwelveMonths
                                row[7] != null ? row[7].toString() : null, // betweenTwelveAndTwentyFourMonths
                                row[8] != null ? row[8].toString() : null, // moreThanTwentyFourMonths
                                row[9] != null ? row[9].toString() : null // totalPending
                		))
                .toList();
	}
	
	private Long getHouseId() {
        Long houseId = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsDTO) {
            houseId = ((UserDetailsDTO) principal).getHouseId();
        }
        return houseId;
    }
}

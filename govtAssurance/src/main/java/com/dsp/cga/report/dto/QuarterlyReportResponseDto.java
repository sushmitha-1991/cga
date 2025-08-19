package com.dsp.cga.report.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class QuarterlyReportResponseDto {

	String ministry;
	String pendingStartQrtr;
	String addedQrtr;
	String droppedQrtr;
	String laidQrtr;
	String lessThanSixMonths;
	String betweenSixAndTwelveMonths;
	String betweenTwelveAndTwentyFourMonths;
	String moreThanTwentyFourMonths;
	String totalPending;
	public QuarterlyReportResponseDto(String ministry, String pendingStartQrtr, String addedQrtr, String droppedQrtr,
			String laidQrtr, String lessThanSixMonths, String betweenSixAndTwelveMonths,
			String betweenTwelveAndTwentyFourMonths, String moreThanTwentyFourMonths, String totalPending) {
		super();
		this.ministry = ministry;
		this.pendingStartQrtr = pendingStartQrtr;
		this.addedQrtr = addedQrtr;
		this.droppedQrtr = droppedQrtr;
		this.laidQrtr = laidQrtr;
		this.lessThanSixMonths = lessThanSixMonths;
		this.betweenSixAndTwelveMonths = betweenSixAndTwelveMonths;
		this.betweenTwelveAndTwentyFourMonths = betweenTwelveAndTwentyFourMonths;
		this.moreThanTwentyFourMonths = moreThanTwentyFourMonths;
		this.totalPending = totalPending;
	}
	
}

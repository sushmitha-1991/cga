package com.dsp.cga.assurance.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class LoksabhaSessionNoDTO {
	
	private Long id;
    private Long lokSabhaMasterId;
    private String sessionNumber;
    private LocalDate sessionStart;
    private LocalDate sessionEnd;
    private Integer totalSittings;
    private String sessionType;
}

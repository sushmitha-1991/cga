package com.dsp.cga.assurance.droppingrequest.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssuranceDroppingRequestDTO {
    private String partId;
    private String supplementaryId;
    private Long droppingType;
    private LocalDate dropRequestDate;
    private LocalDate actualDropDate;
    private LocalDate ministryRefDate;
    private String ministryRefNo;
    private String remarks;
    private Long statusId; // Optional
}


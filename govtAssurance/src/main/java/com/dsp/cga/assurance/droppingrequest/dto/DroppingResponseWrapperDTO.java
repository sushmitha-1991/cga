package com.dsp.cga.assurance.droppingrequest.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DroppingResponseWrapperDTO {
    private List<AssuranceDroppingResponseDTO> data;
    private long count;
}
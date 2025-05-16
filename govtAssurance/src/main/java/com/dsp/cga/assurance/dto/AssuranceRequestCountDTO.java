package com.dsp.cga.assurance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssuranceRequestCountDTO {
    private Long implementationCount;
    private Long extensionCount;
    private Long droppingCount;
    private Long clubbedAssuranceCount;
    private Long othersRequestCount;
}


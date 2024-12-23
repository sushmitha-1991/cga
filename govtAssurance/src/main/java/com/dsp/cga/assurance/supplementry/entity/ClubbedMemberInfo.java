package com.dsp.cga.assurance.supplementry.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ClubbedMemberInfo {
    private Long clubbedMember; // Member ID
    private Integer pageNumber; // Page number
}
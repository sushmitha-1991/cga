package com.dsp.govtAssurance.partDetails.entity;

import com.dsp.govtAssurance.entity.Assurance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "part_details", schema = "cga")
public class PartDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "part_seq")
    @SequenceGenerator(name = "part_seq", sequenceName = "cga.part_seq", allocationSize = 1)
    private Long id;

    private String part;

    @Column(name = "subject", length = 255)
    private String subject;

    @Column(name = "status_id", nullable = false)
    private Long statusId;

    private String extentOfAssurance;

    @ManyToOne
    @JoinColumn(name = "assurance_id", nullable = false)
    private Assurance assurance;
}

package com.dsp.cga.implementationrequest.entity;

import java.time.LocalDate;

import com.dsp.cga.entity.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImplementationRequest extends Auditable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "implementation_req_seq")
	@SequenceGenerator(name = "implemetation_req_seq", sequenceName = "cga.implementation_req_seq", allocationSize = 1)
	private Long id;

	@Column(name = "assurance_id", nullable = false)
    private Long assuranceId;

    @Column(name = "committee_decision_id")
    private Long committeeDecisionId;

    @Column(name = "laying_date", nullable = false)
    private LocalDate layingDate;

    @Column(name = "statement_no", length = 50)
    private String statementNo;

    @Column(name = "supplementary_id")
    private Long supplementaryId;

    @Column(name = "part_id")
    private Long partId;

    @Column(name = "reason", length = 512)
    private String reason;

    @Column(name = "implementation_report_files", columnDefinition = "jsonb")
    private String implementationReportFiles;

    @Column(name = "part_report_files", columnDefinition = "jsonb")
    private String partReportFiles;

    @Column(name = "other_files", columnDefinition = "jsonb")
    private String otherFiles;
}

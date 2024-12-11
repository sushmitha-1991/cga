package com.dsp.cga.assurance.implementationrequest.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dsp.cga.entity.Auditable;
import com.dsp.cga.util.JsonMapConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "implementation_requests", schema = "cga")
public class ImplementationRequest extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "implementation_req_seq")
	@SequenceGenerator(name = "implementation_req_seq", sequenceName = "cga.implementation_req_seq", allocationSize = 1)
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
	private String supplementaryId;

	@Column(name = "part_id")
	private String partId;

	@Column(name = "reason", length = 512)
	private String reason;

	@Column(name = "implementation_report_files", columnDefinition = "jsonb")
	@Convert(converter = JsonMapConverter.class)
	private List<Map<String, Object>> implementationReportFiles;

	@Column(name = "part_report_files", columnDefinition = "jsonb")
	@Convert(converter = JsonMapConverter.class)
	private List<Map<String, Object>> partReportFiles;

	@Column(name = "other_files", columnDefinition = "jsonb")
	@Convert(converter = JsonMapConverter.class)
	private List<Map<String, Object>> otherFiles;

	@Column(name = "draft_status", nullable = false)
	private Boolean draftStatus = false;
	
//	@ManyToOne
//    @JoinColumn(name = "assurance_id", nullable = false)
//    private Assurance assurance;

}

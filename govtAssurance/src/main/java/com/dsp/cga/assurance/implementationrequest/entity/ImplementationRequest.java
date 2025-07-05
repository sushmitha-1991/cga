package com.dsp.cga.assurance.implementationrequest.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.util.JsonMapConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "implementation_requests", schema = "cga")
public class ImplementationRequest extends ApplicationInstance {

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "implementation_req_seq")
//	@SequenceGenerator(name = "implementation_req_seq", sequenceName = "cga.implementation_req_seq", allocationSize = 1)
//	private Long id;

	@Column(name = "assurance_id", nullable = false)
	private Long assuranceId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assurance_id", nullable = false, insertable = false,updatable = false)
	private Assurance assurance;
	
	@Column(name = "committee_decision_id")
	private Long committeeDecisionId;

	@Column(name = "laying_date")
	private LocalDate layingDate;

	@Column(name = "statement_no", length = 50)
	private String statementNo;

	@Column(name = "supplementary_id")
	private String supplementaryId;

	@Column(name = "part_id")
	private String partId;

	@Column(name = "reason", length = 512)
	private String reason;
	
	@Column(name = "implementation_type_id")
	private Long implementationTypeId;

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
	
	@Column(name = "session")
	private Long session;
	
	@Column(name = "due_date_of_implementation")
	private LocalDate dueDateOfImplementation;
	
	@Column(name = "remarks", columnDefinition = "TEXT")
	private String remarks;

	@Column(name = "date_of_implementation")
	private LocalDate dateOfImplementation;

	@Column(name = "details_of_implementation", columnDefinition = "TEXT")
	private String detailsOfImplementation;
	
	@Column(name = "from_ministry")
	private Boolean fromMinistry; 
	
	@Column(name = "status_for_ministry")
    private String statusForMinistry;
	
	
	@Column(name = "return_from")
	private String returnFrom; 
	
	@Column(name = "send_to")
	private String sendTo; 
	
	@Column(name = "status_for_cga")
	private String statusForCga; 
	
	@Column(name = "status_for_mopa")
	private String statusForMopa;
		
	
//	@ManyToOne
//    @JoinColumn(name = "assurance_id", nullable = false)
//    private Assurance assurance;

}

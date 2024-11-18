package com.dsp.cga.assurance.implementationRequest.entity;

import java.time.LocalDate;

import com.dsp.cga.entity.Auditable;

import jakarta.persistence.Column;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "implementation_request", schema = "cga")
public class ImplementationRequest extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "implementation_request_seq")
	@SequenceGenerator(name = "implementation_request_seq", sequenceName = "implementation_request_seq", allocationSize = 1)

	private Long id;

	@Column(name = "assurance_no", nullable = false)
	private String assuranceNo;

	@Column(name = "lok_sabha_no")
	private Integer lokSabhaNo;

	@Column(name = "session_no")
	private String sessionNo;

	@Column(name = "ministry")
	private Long ministry;

	@Column(name = "department")
	private Long department;

	@Column(name = "member")
	private Long member;

	@Column(name = "source")
	private String source;

	@Column(name = "question_no")
	private String questionNo;
	
	
	@Column(name = "due_date_of_implementation", nullable = false)
	private LocalDate dueDateOfImplementation;

	@Column(name = "details_of_implementation")
	private String detailsOfImplementation;

	@Column(name = "date_of_implementation")
	private LocalDate dateOfImplementation;

	@Column(name = "remarks", nullable = false)
	private String remarks;

	@Column(name = "upload_path")
	private String uploadPath;

}

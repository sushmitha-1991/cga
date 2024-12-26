package com.dsp.cga.assurance.extensionrequest.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.entity.Auditable;
import com.dsp.cga.util.JsonMapConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "extension_requests", schema = "cga")
public class ExtensionRequest extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "extension_req_seq")
	@SequenceGenerator(name = "extension_req_seq", sequenceName = "cga.extension_req_seq", allocationSize = 1)
	private Long id;

	@Column(name = "assurance_id", nullable = false)
	private Long assuranceId;


//	@Column(name = "question_no", length = 10)
//	private String questionNo;

	@Column(name = "extension_no", nullable = false)
	private String extensionNo;

	@Column(name = "subject", nullable = false)
	private String subject;

	@Column(name = "ext_requested_till_date", nullable = false)
	private LocalDate extRequestedTillDate;

	@Column(name = "ext_granted_till_date", nullable = false)
	private LocalDate extGrantedTillDate;

	@Column(name = "ministry_ref_no", length = 50, nullable = false)
	private String ministryRefNo;

	@Column(name = "ministry_ref_date", nullable = false)
	private LocalDate ministryRefDate;

	@Column(name = "remarks", length = 512)
	private String remarks;
	
	@Column(name = "statusId")
	private Long statusId;

	@Column(name = "extension_files", columnDefinition = "jsonb")
	@Convert(converter = JsonMapConverter.class)
	private List<Map<String, Object>> extensionFiles;

	@Column(name = "draft_status", nullable = false)
	private Boolean draftStatus = false;

}

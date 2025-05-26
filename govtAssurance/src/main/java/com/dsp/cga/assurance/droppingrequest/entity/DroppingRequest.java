package com.dsp.cga.assurance.droppingrequest.entity;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "dropping_requests", schema = "cga")
public class DroppingRequest extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dropping_req_seq")
	@SequenceGenerator(name = "dropping_req_seq", sequenceName = "cga.dropping_req_seq", allocationSize = 1)
	private Long id;

	@Column(name = "assurance_id", nullable = false)
	private Long assuranceId;

	@Column(name = "droppingType")
	private Long droppingType; // Code is a reference to the 'master' microservice

	@Column(name = "supplementary_id")
	private String supplementaryId;

	@Column(name = "part_id")
	private String partId;
	
	
	

	@Column(name = "drop_request_date")
	private LocalDate dropRequestDate;

	@Column(name = "actual_drop_date")
	private LocalDate actualDropDate;

	@Column(name = "ministry_ref_no")
	private String ministryRefNo;

	@Column(name = "ministry_ref_date")
	private LocalDate ministryRefDate;

	@Column(name = "accepted_id")
	private Long acceptedId;
	
	@Column(name = "type")
	private Long type;
	
	@Column(name = "communication_date")
	private LocalDate communicationDate;

	@Column(name = "subject")
	private String subject;
	
	@Column(name = "issue_letter_ministry")
	private Boolean issueLetterMinistry = Boolean.FALSE;
	
	
	@Column(name = "session")
	private Long session;

	@Column(name = "remarks", length = 512)
	private String remarks;
	@Column(name = "meeting_no")
	private String meetingNo;
	@Column(name = "meeting_date")
	private LocalDate meetingDate;
	
	@Column(name = "dropping_files", columnDefinition = "jsonb")
	@Convert(converter = JsonMapConverter.class)
	private List<Map<String, Object>> droppingFiles;

	@Column(name = "draft_status")
	private Boolean draftStatus = Boolean.FALSE;
	
	@Column(name = "from_ministry")
	private Boolean fromMinistry; 
	
	@Column(name = "status_for_ministry")
    private String statusForMinistry;
	

}

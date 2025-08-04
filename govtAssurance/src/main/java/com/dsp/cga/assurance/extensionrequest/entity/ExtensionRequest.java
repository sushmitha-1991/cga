package com.dsp.cga.assurance.extensionrequest.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.SQLDelete;

import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.entity.Auditable;
import com.dsp.cga.util.JsonMapConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@SQLDelete(sql = "UPDATE cga.extension_requests SET is_active = false, is_deleted = true WHERE id = ?")
public class ExtensionRequest extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "extension_req_seq")
	@SequenceGenerator(name = "extension_req_seq", sequenceName = "cga.extension_req_seq", allocationSize = 1)
	private Long id;

	@Column(name = "assurance_id", nullable = false)
	private Long assuranceId;

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assurance_id", nullable = false, insertable = false,updatable = false)
	private Assurance assurance;
	
	@Column(name = "extension_no")
	private String extensionNo;

	@Column(name = "subject")
	private String subject;

	@Column(name = "ext_requested_till_date")
	private LocalDate extRequestedTillDate;

	@Column(name = "ext_granted_till_date")
	private LocalDate extGrantedTillDate;

	@Column(name = "ministry_ref_no", length = 50)
	private String ministryRefNo;

	@Column(name = "ministry_ref_date")
	private LocalDate ministryRefDate;

	@Column(name = "remarks", length = 512)
	private String remarks;
	
	@Column(name = "statusId")
	private Long statusId;
	
	@Column(name = "status_for_ministry")
    private String statusForMinistry;
	
	@Column(name = "extension_files", columnDefinition = "jsonb")
	@Convert(converter = JsonMapConverter.class)
	private List<Map<String, Object>> extensionFiles;

	@Column(name = "draft_status")
	private Boolean draftStatus = false;
	
	@Column(name = "from_ministry")
	private Boolean fromMinistry;
	
	@Column(name = "status_for_cga")
	private String statusForCga; 
}

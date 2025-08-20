package com.dsp.cga.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.implementationrequest.entity.ApplicationInstance;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@Data
@Table(name = "request_history", schema = "cga")
public class RequestHistory extends Auditable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_history_id_seq")
	@SequenceGenerator(name = "request_history_id_seq", sequenceName = "cga.request_history_id_seq", allocationSize = 1)
	private Long id;
	
	@Column(name = "request_id")
	private Long requestId;
	
	@Column(name = "request_type")
	private String requestType;
	
	@Column(name = "action_type")
	private String actionType;
	
	@Convert(converter = JsonConverter.class)
    @Column(name = "reference_files", columnDefinition = "JSONB")
    private List<Map<String,Object>> referenceFiles;
	
	@Column(name = "pending_with")
	private Long pendingWith;
	
	@Column(name = "action_taken_by")
	private Long actionTakenBy;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "action_taken_by_name")
	private String actionTakenByName;
	
	@Column(name = "pending_with_name")
	private String pendingWithName;
	
	@Column(name = "comment_for_display")
	private String commentForDisplay;
	

}

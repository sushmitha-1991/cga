package com.dsp.cga.assurance.droppingrequest.entity;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="dropping_requests", schema = "cga")
public class DroppingRequest extends Auditable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dropping_req_seq")
	@SequenceGenerator(name = "dropping_req_seq", sequenceName = "cga.dropping_req_seq", allocationSize = 1)
	private Long id;
	
	  @Column(name = "assurance_id", nullable = false)
	    private Long assuranceId;

	  @Column(name = "droppingType", nullable = false)
	    private Long droppingType; // Code is a reference to the 'master' microservice

	    @Column(name = "supplementary_id")
	    private Long supplementaryId;

	    @Column(name = "part_id")
	    private Long partId;

	    @Column(name = "drop_request_date", nullable = false)
	    private LocalDate dropRequestDate;

	    @Column(name = "actual_drop_date")
	    private LocalDate actualDropDate;

//	    @Column(name = "drop_status_id")
//	    private Long dropStatusId;

	    @Column(name = "remarks", length = 512)
	    private String remarks;

	    @Column(name = "draft_status", nullable = false)
	    private Boolean draftStatus = Boolean.FALSE;

}

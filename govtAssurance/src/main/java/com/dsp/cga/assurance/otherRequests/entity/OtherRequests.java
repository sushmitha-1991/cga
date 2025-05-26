package com.dsp.cga.assurance.otherRequests.entity;

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
@Table(name = "other_requests", schema = "cga")

public class OtherRequests extends Auditable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "other_req_seq")
	@SequenceGenerator(name = "other_req_seq", sequenceName = "cga.other_req_seq", allocationSize = 1)
	private Long id;

	  @Column(name = "assurance_id")
	  private Long assuranceId;
	
	  @Column(name = "requested_date")
	   private LocalDate requestedDate;

	    @Column(name = "status_id")
	    private Long statusId; 

	    @Column(name = "remarks", columnDefinition = "TEXT")
	    private String remarks;
	    
	    @Column(name = "subject")
	    private String subject;
	    
	    @Column(name = "from_ministry")
		private Boolean fromMinistry;
	    
	    @Column(name = "status_for_ministry")
	    private String statusForMinistry;
		

	    @Column(name = "other_files", columnDefinition = "jsonb")
		@Convert(converter = JsonMapConverter.class)
		private List<Map<String, Object>> fileUrl;

	    
	}
	



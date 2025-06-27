package com.dsp.cga.assurance.clubbedassurance.entity;

import java.time.LocalDate;
import java.util.List;

import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.entity.Auditable;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "clubbed_assurances", schema="cga")
public class ClubbedAssurance extends Auditable{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clubbed_Assurance_seq")
	@SequenceGenerator(name = "clubbed_Assurance_seq", sequenceName = "cga.clubbed_Assurance_seq", allocationSize = 1)
	private Long id;
	
	//private Long sessionNo;

	@Column(name = "assurance_id", nullable = false)
	private Long assuranceId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assurance_id", nullable = false, insertable = false,updatable = false)
	private Assurance assurance;	
	
	@Column(name = "date")
	private LocalDate date;
	
	@Column(name = "from_ministry")
	private Boolean fromMinistry; 
	
	@Column(name = "status_for_ministry")
    private String statusForMinistry;
	

	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "clubbed_assurance_mapping", schema = "cga", 
                     joinColumns = @JoinColumn(name = "clubbed_assurance_id"))
    @Column(name = "mapped_clubbed_assurance_id")
    private List<Long> clubbedAssuranceIds;
}

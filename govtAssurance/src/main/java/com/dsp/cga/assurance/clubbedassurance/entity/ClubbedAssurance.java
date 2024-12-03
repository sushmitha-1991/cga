package com.dsp.cga.assurance.clubbedassurance.entity;

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
@Table(name= "clubbed_assurances", schema="cga")
public class ClubbedAssurance extends Auditable{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clubbed_Assurance_seq")
	@SequenceGenerator(name = "clubbed_Assurance_seq", sequenceName = "cga.clubbed_Assurance_seq", allocationSize = 1)
	private Long id;
	
	//private Long sessionNo;

	@Column(name = "assurance_id", nullable = false)
	private Long assuranceId;

	@Column(name = "clubbed_assurance_id", nullable = false)
	private Long clubbedAssuranceId;
}

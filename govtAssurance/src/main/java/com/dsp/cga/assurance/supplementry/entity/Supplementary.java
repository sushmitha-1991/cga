package com.dsp.cga.assurance.supplementry.entity;

import java.util.ArrayList;
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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "supplementary", schema = "cga")
public class Supplementary extends Auditable{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplementary_seq")
	@SequenceGenerator(name = "supplementary_seq", sequenceName = "cga.supplementary_seq", allocationSize = 1)
	private Long id;

	private String supp;
	
	@Column(name = "member_id")
	private Long memberId;

    @ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "supplementary_clubbed_members", schema = "cga", joinColumns = @JoinColumn(name = "supplementary_id"))
	@Column(name = "clubbed_member")
	private List<Long> clubbedMembers = new ArrayList<>();

	private String part;

	@Column(name = "subject", length = 255)
	private String subject;

	@Column(name = "status_id")
	private Long statusId;

	private String extentOfAssurance;

	@ManyToOne
	@JoinColumn(name = "assurance_id", referencedColumnName = "id")
	private Assurance assurance;
}

package com.dsp.govtAssurance.supplementry.entity;

import java.util.ArrayList;
import java.util.List;
import com.dsp.govtAssurance.entity.Assurance;
import com.dsp.govtAssurance.entity.Auditable;

import jakarta.persistence.*;
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

	@ElementCollection
	@CollectionTable(name = "supplementary_clubbed_members", schema = "cga", joinColumns = @JoinColumn(name = "supplementary_id"))
	@Column(name = "clubbed_member")
	private List<Long> clubbedMembers = new ArrayList<>();

	private String part;

	@Column(name = "subject", length = 255)
	private String subject;

	@Column(name = "status_id", nullable = false)
	private Long statusId;

	private String extentOfAssurance;

	@ManyToOne
	@JoinColumn(name = "assurance_id", referencedColumnName = "id")
	private Assurance assurance;
}

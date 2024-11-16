package com.dsp.govtAssurance.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.dsp.govtAssurance.partDetails.entity.PartDetails;
import com.dsp.govtAssurance.supplementry.entity.Supplementary;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "assurance", schema = "cga")
public class Assurance extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assurance_seq")
	@SequenceGenerator(name = "assurance_seq", sequenceName = "cga.assurance_seq", allocationSize = 1)
	private Long id;

	@Column(name = "lok_sabha_no")
	private Long lokSabhaNo;

	@Column(name = "lok_sabha_session_no")
	private Long lokSabhaSessionNo;

	@Column(name = "rajya_sabha_session_no")
	private Long rajyaSabhaSessionNo;

	@NotNull
	@Column(name = "assurance_date", nullable = false)
	private LocalDate assuranceDate;

	@NotNull
	@Column(name = "category_id", nullable = false)
	private Long categoryId;

	@Column(name = "question_no", length = 50)
	private String questionNo;

	@NotNull
	@Column(name = "assurance_type_id", nullable = false)
	private Long assuranceTypeId;

	@Column(name = "subject", length = 255)
	private String subject;

	@NotNull
	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@NotNull
	@Column(name = "ministry_id", nullable = false)
	private Long ministryId;

	@NotNull
	@Column(name = "department_id", nullable = false)
	private Long departmentId;

	@Column(name = "page_no", length = 20)
	private String pageNo;

	@Column(name = "file_no", length = 50)
	private String fileNo;

	@NotNull
	@Column(name = "status_id", nullable = false)
	private Long statusId;

	@Column(name = "pending_due_to")
	private Long pendingDueTo;

	@Column(name = "mpa_ref_date")
	private LocalDate mpaRefDate;

	@Column(name = "report_no", length = 30)
	private String reportNo;

	@Column(name = "remarks", length = 512)
	private String remarks;

	@Column(name = "due_date_of_implementation")
	private LocalDate dueDateOfImplementation;

	@Column(name = "date_of_implementation")
	private LocalDate dateOfImplementation;

	@Column(name = "details_of_implementation", columnDefinition = "TEXT")
	private String detailsOfImplementation;

	@Column(name = "text_of_assurance", length = 200)
	private String textOfAssurance;

//    @Column(name = "supplementary")
//    private Boolean supplementary;

	@Column(name = "supplementary_page_no", length = 10)
	private String supplementaryPageNo;

	@Column(name = "supplementary_member_id")
	private Long supplementaryMemberId;

	@NotNull
	@Column(name = "draft_status", nullable = false)
	private Boolean draftStatus = false;

	@Column(name = "source", length = 255)
	private String source;
	
	@Column(name = "assuranceNo", length = 25)
	private String assuranceNo;
	
	@Column(name = "pageNoUncorrected", length = 10)
	private String pageNoUncorrected;
	
	@Column(name = "pageNoCorrected", length = 10)
	private String pageNoCorrected;
	

	@ElementCollection
	@CollectionTable(name = "clubbed_members", schema = "cga", 
	                 joinColumns = @JoinColumn(name = "assurance_id"))
	@Column(name = "clubbed_member")
	private List<Long> clubbedMembers = new ArrayList<>();


	@OneToMany(mappedBy = "assurance", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PartDetails> partDetails;

	@OneToMany(mappedBy = "assurance", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Supplementary> supplementary;
	
	

}

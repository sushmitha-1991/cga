package com.dsp.cga.assurance.entity;





import com.dsp.cga.entity.Auditable;

import jakarta.persistence.Column;
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

@Table(name = "codes", schema = "master")
//@Audited
public class Code extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "code_seq")
	@SequenceGenerator(name = "code_seq", sequenceName = "code_id_seq", allocationSize = 1)
	private Long id;

	@Column(name = "code_type_id", nullable = false)
	private Long codeTypeId;

	@Column(name = "code", nullable = false, length = 255)
	private String code;

	@Column(name = "abbr", length = 255, unique = true, nullable = false)
	private String abbr;

	@Column(name = "locale")
	private String locale;

	@Column(name = "description")
	private String description;

	@Column(name = "display_sequence")
	private Integer displaySequence;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "code_type_id", referencedColumnName = "id", insertable = false, updatable = false)
//	private CodeType codeType;

	@Column(name = "code_data", nullable = true, columnDefinition = "jsonb")
	private String codeData;

}

/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.cga.assurance.implementationrequest.entity;

import java.util.List;

import com.dsp.cga.entity.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "applications", schema = "master")
public class Application extends Auditable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_seq")
	@SequenceGenerator(name = "application_seq", sequenceName = "application_seq", allocationSize = 1)
	private Long id;

	@Column(nullable = false, length = 10)
	private String house;

	@Column(nullable = false, length = 255)
	private String name;

	@Column(length = 500)
	private String description;

	@Column(name = "form_url", nullable = false, length = 255)
	private String formUrl;

	@Column(name = "report_url", nullable = false, length = 255)
	private String reportUrl;

	@Column(name = "display_sequence")
	private Integer displaySequence;

	@Column(nullable = false, length = 255)
	private String abbr;

	@Column(name = "is_external")
	private Boolean isExternal;

//	@OneToOne
//	@JoinColumn(name = "module_id", referencedColumnName = "id")
//	private Modules moduleId;

//	@OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//	private List<ApplicationInstance> applicationInstances;

	@Column(name = "my_task_flag")
	private Boolean myTaskflag;

}

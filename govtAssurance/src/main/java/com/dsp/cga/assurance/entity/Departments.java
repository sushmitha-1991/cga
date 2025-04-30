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
@Table(name = "departments", schema = "master")
public class Departments extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departments_seq")
	@SequenceGenerator(name = "departments_seq", sequenceName = "departements_seq", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)  // Many departments belong to one ministry
    @JoinColumn(name = "ministry_id", nullable = false)
    private Ministries ministry;  // Assuming Ministries is the related entity for 'ministry_id'

    @Column(name = "department_name", nullable = false, length = 255, unique = true)
    private String departmentName;

    @Column(name = "department_code", nullable = false)
    private Integer departmentCode;

    @Column(name = "abbr", length = 20)
    private String abbr;

    @Column(name = "description", length = 255)
    private String description;

}

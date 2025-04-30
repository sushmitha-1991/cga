package com.dsp.cga.assurance.entity;

import java.util.List;

import com.dsp.cga.entity.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "ministries", schema = "master")
public class Ministries extends Auditable{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ministries_seq")
	@SequenceGenerator(name = "ministries_seq", sequenceName = "ministries_seq", allocationSize = 1)
	private Long id;
	
	@Column(name = "ministry_name", nullable = false, length = 255, unique = true)
    private String ministryName;

    @Column(name = "ministry_code", nullable = false, unique = true)
    private Integer ministryCode;

    @Column(name = "abbr", length = 20)
    private String abbr;

    @Column(name = "description", length = 255)
    private String description;
    
    @OneToMany(mappedBy = "ministry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Departments> departments;
 

	

}

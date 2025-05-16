/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.cga.assurance.entity;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import com.dsp.cga.entity.Auditable;
import com.dsp.cga.entity.BaseUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(schema = "members", name = "personal_details")
public class PersonalDetails extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personal_details_seq")
	@SequenceGenerator(name = "personal_details_seq", sequenceName = "personal_details_seq", allocationSize = 1)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private BaseUser user;

	@Column(name = "initials_id")
	private Long initialsId;

	@Column(name = "initials_hindi_id")
	private Long initialsHindiId;

	@Column(name = "first_name_hindi", length = 100)
	private String firstNameHindi;

	@Column(name = "last_name_hindi", length = 100)
	private String lastNameHindi;

	@Column(name = "full_name", length = 100)
	private String fullName;

	@Column(name = "alias_name", length = 100)
	private String aliasName;

	@Column(name = "pan_no", length = 15)
	private String panNo;

	@Column(name = "aadhar_no", length = 10)
	private String aadharNo;

	@Column(name = "gender", length = 1, nullable = false)
	private String gender;

	@Column(name = "alternative_mobile_no", length = 15)
	private String alternativeMobileNo;

	@Column(name = "blood_group_id")
	private Integer bloodGroupId;

	@Column(name = "constituency_id")
	private Integer constituencyId;

	@Column(name = "state_id")
	private Integer stateId;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "place_of_birth", length = 255)
	private String placeOfBirth;

	@Column(name = "marital_status_id")
	private Integer maritalStatusId;

	@Column(name = "date_of_marriage")
	private LocalDate dateOfMarriage;

	@Column(name = "ic_no", length = 10)
	private String icNo;

	@Column(name = "mp_code", length = 10)
	private String mpCode;

	@Column(name = "party_id")
	private Integer partyId;

	@Builder.Default
	@Column(name = "physically_disabled", nullable = false)
	private Boolean physicallyDisabled = false;

	@Builder.Default
	@Column(name = "freedom_fighter", nullable = false)
	private Boolean freedomFighter = false;

	@Column(name = "preferred_sansad_email", length = 255)
	private String preferredSansadEmail;
//
//	@Builder.Default
//	@OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	private List<SocialMediaAccounts> socialMediaAccounts = new ArrayList<>();

}

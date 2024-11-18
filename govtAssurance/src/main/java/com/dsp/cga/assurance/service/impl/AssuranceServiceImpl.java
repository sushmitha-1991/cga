package com.dsp.cga.assurance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.external.service.ExternalService;
import com.dsp.cga.assurance.partdetails.repository.PartDetailsRepository;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.assurance.service.AssuranceService;
import com.dsp.cga.assurance.supplementry.repository.SupplementaryRepository;
import com.dsp.cga.mapper.AssuranceMapper;

@Service
public class AssuranceServiceImpl implements AssuranceService {

	@Autowired
	private AssuranceRepository assuranceRepository;

	@Autowired
	private PartDetailsRepository partDetailsRepository;

	@Autowired
	private SupplementaryRepository supplementaryRepository;

	@Autowired
	private AssuranceMapper assuranceMapper;
	
	@Autowired
    private ExternalService externalService;

	@Autowired
	private RestTemplate restTemplate;

	@Transactional
	@Override
	public AssuranceDTO createAssurance(AssuranceDTO assuranceDTO) {
	    // Fetch external names using the external service
//	    String lokSabhaName = externalService.getLokSabhaName(assuranceDTO.getLokSabhaNo());
//	    String categoryName = externalService.getCategoryName(assuranceDTO.getCategoryId());
//	    String memberName = externalService.getMemberName(assuranceDTO.getMemberId());
//	    String statusName = externalService.getStatusName(assuranceDTO.getStatusId());
//	    String ministryName = externalService.getMinistryName(assuranceDTO.getMinistryId());
//	    String departmentName = externalService.getDepartmentName(assuranceDTO.getDepartmentId());

	    // Map the AssuranceDTO to Assurance entity
	    Assurance assurance = assuranceMapper.toEntity(assuranceDTO);
	   // assurance.setClubbedMembers(assuranceDTO.getClubbedMembers());
	    assurance.getSupplementary().forEach(supplementary -> {
	        supplementary.setAssurance(assurance); // Link Assurance to Supplementary
	    });
	    
	    // Save Assurance entity
	    Assurance savedAssurance = assuranceRepository.save(assurance); // Save once, to make it effectively final



	    // Map entity back to DTO to return response
	    AssuranceDTO savedAssuranceDTO = assuranceMapper.toDTO(assurance);
//	    savedAssuranceDTO.setLokSabhaName(lokSabhaName);
//	    savedAssuranceDTO.setCategoryName(categoryName);
//	    savedAssuranceDTO.setMemberName(memberName);
//	    savedAssuranceDTO.setStatusName(statusName);
//	    savedAssuranceDTO.setMinistryName(ministryName);
//	    savedAssuranceDTO.setDepartmentname(departmentName);

	    return savedAssuranceDTO;
	}

}

package com.dsp.cga.assurance.service;

import java.util.List;

import com.dsp.cga.assurance.dto.AssuranceDTO;

public interface AssuranceService {

	AssuranceDTO createAssurance(AssuranceDTO assuranceDTO);

	List<AssuranceDTO> getAllAssurance();

	
}

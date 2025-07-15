package com.dsp.cga.assurance.dsp1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cga/dsp1")
public class GovtAssuranceController {

	@Autowired
    private GovtAssuranceResponseService govtAssuranceResponseService;

	@GetMapping("/govtAssuranceList")
	public GovtAssuranceResponseDTO getAssuranceList(@RequestParam(required = true) String mpCode) {
	    return govtAssuranceResponseService.getGovtAssuranceList(mpCode);
	}

}

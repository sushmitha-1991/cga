package com.dsp.cga.mapper;

import com.dsp.cga.assurance.dto.CommonRequestResponseDTO;
import com.dsp.cga.assurance.otherRequests.entity.OtherRequests;
import com.dsp.cga.eunms.CgaStatus;
import com.dsp.cga.eunms.MinistryStatus;

public class OtherRequestMapper {

	
	  public static void updateEntityForReturnOrAccept(CommonRequestResponseDTO dto, OtherRequests entity) {
		  
		  if (!MinistryStatus.RETURNED.getValue().equals(dto.getStatusForMinistry()) 
			  && !CgaStatus.ACCEPTED.getValue().equals(dto.getStatusForMinistry())) 
			  return;
		  
		  if (MinistryStatus.RETURNED.getValue().equals(dto.getStatusForMinistry())) {
			  
			  entity.setStatusForMinistry(MinistryStatus.RETURNED.getValue());
			  entity.setStatusForCga(MinistryStatus.RETURNED.getValue());
			  entity.setReturnRemarks(entity.getReturnRemarks());
			  return;
		  }

		  if (MinistryStatus.ACCEPTED.getValue().equals(dto.getStatusForMinistry())) {
			  
			  entity.setStatusForMinistry(MinistryStatus.ACCEPTED.getValue());
			  entity.setStatusForCga(MinistryStatus.ACCEPTED.getValue());
			  return;
		  }

	  }
	  
}

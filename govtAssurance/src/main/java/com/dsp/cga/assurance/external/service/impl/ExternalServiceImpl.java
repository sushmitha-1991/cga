package com.dsp.cga.assurance.external.service.impl;

import org.springframework.stereotype.Service;

import com.dsp.cga.assurance.external.service.ExternalService;

@Service
public class ExternalServiceImpl implements ExternalService {

//    @Value("${master.lokSabhaServiceUrl}")
//    private String lokSabhaServiceUrl;
//
//    @Value("${master.categoryServiceUrl}")
//    private String categoryServiceUrl;
//
//    @Value("${master.memberServiceUrl}")
//    private String memberServiceUrl;
//
//    @Value("${master.statusServiceUrl}")
//    private String statusServiceUrl;
//
//    @Value("${master.departmentServiceUrl}")
//    private String departmentServiceUrl;
//
//    private RestTemplate restTemplate;
//
//    
//    public String getLokSabhaName(Long lokSabhaId) {
//        String url = lokSabhaServiceUrl + "/api/lokSabha/" + lokSabhaId;
//        return restTemplate.getForObject(url, String.class);
//    }
//
//    public String getCategoryName(Long categoryId) {
//        String url = categoryServiceUrl + "/api/category/" + categoryId;
//        return restTemplate.getForObject(url, String.class);
//    }
//
//    public String getMemberName(Long memberId) {
//        String url = memberServiceUrl + "/api/member/" + memberId;
//        return restTemplate.getForObject(url, String.class);
//    }
//
//    public String getStatusName(Long statusId) {
//        String url = statusServiceUrl + "/api/status/" + statusId;
//        return restTemplate.getForObject(url, String.class);
//    }
//
//    public String getDepartmentName(Long departmentId) {
//        String url = departmentServiceUrl + "/api/department/" + departmentId;
//        return restTemplate.getForObject(url, String.class);
//    }
//
//	@Override
//	public String getAssuranceType(Long assuranceTypeId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getMinistryName(Long ministryId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}

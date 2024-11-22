//package com.dsp.cga.assurance.external.service.impl;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import com.dsp.cga.assurance.external.service.ExternalService;
//
//@Service
//public class ExternalServiceImpl implements ExternalService {
//
////    @Value("${master.lokSabhaServiceUrl}")
////    private String lokSabhaServiceUrl;
//    
////    @Value("${master.rajyaSabhaServiceUrl}")
////    private String rajyaSabhaServiceUrl;
//
////    @Value("${master.categoryServiceUrl}")
////    private String categoryServiceUrl;
//
//    @Value("${member.memberServiceUrl}")
//    private String memberServiceUrl;
//
//    @Value("${master.statusServiceUrl}")
//    private String statusServiceUrl;
//
//    @Value("${master.departmentServiceUrl}")
//    private String departmentServiceUrl;
//    
//    @Value("${master.AssuranceServiceUrl}")
//    private String AssuranceServiceUrl;
//    
//    @Value("${master.ministryServiceUrl}")
//    private String ministryServiceUrl;
//
//    private RestTemplate restTemplate;
//
//    
////    public String getLokSabhaSession(Long lokSabhaId) {
////        String url = lokSabhaServiceUrl + "/get/lokSabha/" + lokSabhaId;
////        return restTemplate.getForObject(url, String.class);
////    }
////
////    public String getCategoryName(Long categoryId) {
////        String url = categoryServiceUrl + "/" + categoryId;
////        return restTemplate.getForObject(url, String.class);
////    }
//
//    public String getMemberName(Long memberId) {
//        String url = memberServiceUrl + "/getById/" + memberId;
//        return restTemplate.getForObject(url, String.class);
//    }
//
//    public String getStatusName(Long statusId) {
//        String url = statusServiceUrl + "/" + statusId;
//        return restTemplate.getForObject(url, String.class);
//    }
//
//    public String getDepartmentName(Long departmentId) {
//        String url = departmentServiceUrl + "/get/department" + departmentId;
//        return restTemplate.getForObject(url, String.class);
//    }
//
//	@Override
//	public String getAssuranceType(Long assuranceTypeId) {
//		  String url = AssuranceServiceUrl + "get" + assuranceTypeId;
//	        return restTemplate.getForObject(url, String.class);
//	    }
//
//	@Override
//	public String getMinistryName(Long ministryId) {
//		  String url = ministryServiceUrl + "/get/ministry/" + ministryId;
//	        return restTemplate.getForObject(url, String.class);
//	    }
//
////	@Override
////	public String getRajyaSabhaSession(Long rajyaSabhaId) {
////		 String url = rajyaSabhaServiceUrl + "/get/lokSabha/" + rajyaSabhaId;
////	        return restTemplate.getForObject(url, String.class);
////	    }
//	}
//

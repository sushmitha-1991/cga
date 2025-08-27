package com.dsp.cga.eunms;

public enum DashboardAssurancesType {

	Session_Wise ("Session Wise Assurances"),
	Ministry_Wise ("Ministry Wise Assurances"),
	Member_Wise ("Member Wise Assurances"),
	Status_Wise ("Status Wise Assurances"),
	Category_Wise ("Category Wise Assurances");
	
	private final String value;

	DashboardAssurancesType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

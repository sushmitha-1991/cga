package com.dsp.cga.eunms;

public enum MinistryStatus {
	
	SUBMITTED ("Submitted"),
	RETURNED ("Returned"),
	DRAFT ("Draft"),
	ACCEPTED ("Accepted");

	private final String value;

	MinistryStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package com.dsp.cga.eunms;

public enum CgaStatus {
	
	RECEIVED ("Received"),
	ACCEPTED ("Accepted"),
	IN_PROGRESS ("In Progress"),
	RETURNED ("Returned");
	
	private final String value;

	CgaStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

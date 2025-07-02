package com.dsp.cga.eunms;

public enum MinistryRequestStatus {
	
	SUBMITTED ("Submitted"),
	RETURNED ("Returned"),
	
	/** Status for MOPA Interface */
	RECEIVED ("Received"),
	ACCEPTED ("Accepted"),
	IN_PROGRESS ("In Progress");

	private final String value;

	MinistryRequestStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

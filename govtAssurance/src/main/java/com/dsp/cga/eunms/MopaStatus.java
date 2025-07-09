package com.dsp.cga.eunms;

public enum MopaStatus {
	
	/** Status for MOPA Interface */
	RECEIVED ("Received"),
	ACCEPTED ("Accepted"),
	IN_PROGRESS ("In Progress"),
	RETURNED ("Returned"),
	RETURNED_TO_MINISTRY ("ReturnedToMinistry");


	private final String value;

	MopaStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

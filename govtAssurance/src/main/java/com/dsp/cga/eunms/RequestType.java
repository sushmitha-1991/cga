package com.dsp.cga.eunms;

public enum RequestType {
	
	IMPLEMENTATION_REQ ("Implementation Request"),
	EXTENSION_REQ ("Extension Request"),
	DROPPING_REQ ("Dropping Request"),
	OTHER_REQ ("Other Request"),
	CLUB_REQ ("Clubbed Assurance");
	
	private final String value;

	RequestType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

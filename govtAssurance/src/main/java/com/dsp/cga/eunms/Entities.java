package com.dsp.cga.eunms;

public enum Entities {
	
	MOPA ("mopa"),
	CGA ("cga"),
	MINISTRY ("ministry");
	
	private final String value;

	Entities(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

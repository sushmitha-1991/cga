package com.dsp.cga.eunms;

public enum RequestHistoryStatus {

	MOPA (2L),
	MINISTRY (1L);
	
	private final Long value;

	RequestHistoryStatus(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}

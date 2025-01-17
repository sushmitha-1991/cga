package com.dsp.cga.assurance.dto;

public enum AssuranceCategory {
	
	Question("SQ"),
    CALLING_ATTENTION("CA"),
    Special("SP"),
    Mention("MN"),
    Point("PO"),
    General("GN"),
    Half("HA"),
    MISC("MI"),
	SPLM("SM"),
	Resolution("RS"),
	Bills("BI");

    private final String code;

    AssuranceCategory(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}


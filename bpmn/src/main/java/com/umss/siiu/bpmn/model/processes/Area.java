package com.umss.siiu.bpmn.model.processes;

public enum Area {
    ROI("ROI", "roi"),
    RELOMA("Reloma", "reloma"),
    ZONING("Zoning", "zoning"),
    TAXES("Taxes", "taxes"),
    NARRATIVE("Narrative", "narrative"),
    FORMATTING("Formatting", "formatting"),
    QA("QA", "qa"),
    NONE("None", "");

    private final String name;
    private final String code;

    Area(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

}

package com.umss.siiu.bpmn.model.processes;

public enum TaskType {
    BOOTSTRAP("Bootstrap", "boot"),
    VALIDATE_PARCEL("Validate Parcel", "vp"),
    ROI("ROI", "roi"),
    RELOMA("Reloma", "reloma"),
    ZONING("Zoning", "zoning"),
    TAXES("Taxes", "taxes"),
    NARRATIVE("Narrative", "narrative"),
    FORMATTING("Formatting", "formatting"),
    REFORMATTING("Reformatting", "reformatting"),
    QA("QA", "qa"),
    RE_REVISE("Re-Revise", "rrevise"),
    UPLOAD_FILES("Upload Files", "uploadf");

    private final String name;
    private final String code;

    TaskType(String name, String code) {
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

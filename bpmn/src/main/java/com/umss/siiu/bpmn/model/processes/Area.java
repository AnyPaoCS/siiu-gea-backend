package com.umss.siiu.bpmn.model.processes;

public enum Area {
    FILES_AREA("Files Area", "files_a"),
    DEPARTMENT_AREA("Department Area", "dep_a"),
    FACULTY_AREA("Faculty Area", "fac_a"),
    CASH_AREA("Cash Area", "cash_a"),
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

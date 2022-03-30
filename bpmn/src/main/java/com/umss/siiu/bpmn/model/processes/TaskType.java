package com.umss.siiu.bpmn.model.processes;

public enum TaskType {
    REQUEST_PROCESS("Request Process", "req_pro"),
    VALIDATE_PARCEL("Validate Parcel", "vp"),
    REVIEW_REQUIREMENTS("Review Requirements", "rev_doc"),
    ENABLE_PAYMENT("Enable Payment", "ena_pay"),
    VALIDATION_PAYMENT("Validation Payment", "val_pay"),
    VALIDATION_DOCUMENTS("Validation Documents", "val_doc"),
    SIGNATURE_DOCUMENTS("Signature Documents", "sign_doc"),
    CONCLUDE_PROCESS("Conclude Process", "con_pro"),
    OBSERVATIONS("Observations", "obs");

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

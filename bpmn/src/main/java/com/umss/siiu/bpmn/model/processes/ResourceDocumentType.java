package com.umss.siiu.bpmn.model.processes;

public enum ResourceDocumentType {
    ORIGINAL_DOCUMENT("Documento Original", "DOC-ORIGINAL"),
    COPY_ORIGINAL_DOCUMENT("Fotocopia del documento original a legalizar", "DOC-COPY"),
    VALUED("Valorado de Legalizacion Fotocopia", "VALORADO"),
    LEGALIZED_DOCUMENT("Documento Legalizado", "DOC-LEGALIZADO");

    private final String name;
    private final String code;

    ResourceDocumentType(String name, String code) {
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
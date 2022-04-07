package com.umss.siiu.bpmn.model.processes;

public enum ResourceDocumentType {

    DIPLOMA_BACHILLER_DOCUMENTO_ORIGINAL("Fotocopia anverso y reverso del Diploma de Bachiller/Diploma Académico/Titulo Profesional/Título Postgrado/RR.Homologación Policía o Militar; según requiera el solicitante","R_Diploma_Bachiller"),
    DIPLOMA_BACHILLER_VALORADO("Valorado de Legalización para: Diploma de Bachiller/Diploma Académico/Titulo Profesional/Título Postgrado/RR.Homologación Policía o Militar; según requiera el solicitante","R_V_Diploma_Bachiller"),
    DIPLOMA_BACHILLER_DOCUMENTO_LEGALIZADO("Documento Legalizado", "L_Diploma_Bachiller"),
    DOCUMENTO_VARIOS_DOCUMENTO_ORIGINAL("Documento Original", "R_Documentos_Varios"),
    DOCUMENTOS_VARIOS_FOTOCOPIA("Fotocopia del documento original a legalizar", "R_F_Documentos_Varios"),
    DOCUMENTOS_VARIOS_VALORADO("Valorado de Legalizacion Fotocopia", "R_V_Documentos_Varios"),
    DOCUMENTOS_VARIOS_DOCUMENTO_LEGALIZADO("Documento Legalizado", "L_Documentos_Varios");

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
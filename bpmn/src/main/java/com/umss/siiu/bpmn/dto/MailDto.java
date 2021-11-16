package com.umss.siiu.bpmn.dto;

import java.util.Map;

public class MailDto {

    private String[] to;
    private String subject;
    private String templateName;
    private Map<String, Object> parameters;
    private MailAttachmentDto<?> mailAttachment;

    public MailDto() {
    }

    public MailDto(String[] to, String subject, String templateName, Map<String, Object> parameters) {
        this.to = to;
        this.subject = subject;
        this.templateName = templateName;
        this.parameters = parameters;
    }

    public MailDto(String[] to, String subject, String templateName, Map<String, Object> parameters,
            MailAttachmentDto<?> mailAttachment) {
        this.to = to;
        this.subject = subject;
        this.templateName = templateName;
        this.parameters = parameters;
        this.mailAttachment = mailAttachment;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public MailAttachmentDto<?> getMailAttachment() {
        return mailAttachment;
    }

    public void setMailAttachment(MailAttachmentDto<?> mailAttachment) {
        this.mailAttachment = mailAttachment;
    }

}

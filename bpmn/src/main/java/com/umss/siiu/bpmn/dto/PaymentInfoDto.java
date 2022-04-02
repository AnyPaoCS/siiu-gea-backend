package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.Payment;

public class PaymentInfoDto {

    private long processId;
    private String paymentCode;
    private String amount;
    private String description;
    private String paymentStatus;
    private String paymentType;

    public PaymentInfoDto() {
    }

    public PaymentInfoDto (Payment payment) {
        if (payment.getProcessInstance() != null) {
            this.processId = payment.getProcessInstance().getId();
        } else {
            this.processId = 0;
        }
        this.amount = payment.getAmount().toString();
        this.description = payment.getDescription();
        this.paymentStatus = payment.getPaymentStatus().toString();
        this.paymentType = payment.getPaymentType().toString();
        this.paymentCode = payment.getPaymentCode();
    }

    public long getProcessId() {
        return processId;
    }

    public void setProcessId(long processId) {
        this.processId = processId;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}

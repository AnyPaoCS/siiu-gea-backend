package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.Payment;
import com.umss.siiu.bpmn.model.processes.ProcessInstance;
import com.umss.siiu.core.dto.DtoBase;

import java.math.BigDecimal;

public class PaymentDto extends DtoBase<Payment> {

    private BigDecimal amount;
    private String description;
    private ProcessInstance processInstance;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }
}

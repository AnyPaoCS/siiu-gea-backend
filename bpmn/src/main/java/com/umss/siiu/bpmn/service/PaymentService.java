package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.dto.PaymentInfoDto;
import com.umss.siiu.bpmn.model.Payment;
import com.umss.siiu.core.service.GenericService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface PaymentService extends GenericService<Payment> {

    long savePayment (PaymentInfoDto paymentInfoDto);

    PaymentInfoDto realizePayment (Long paymentId);

    PaymentInfoDto realizePaymentByProcessInstanceId (Long processInstanceId);

    PaymentInfoDto findByPaymentId (Long paymentId);

    PaymentInfoDto findByProcessInstanceId (Long processInstanceId);

    boolean verifyPaymentInformationIsCorrect (Long processInstanceId, BigDecimal amount);

    List<Payment> getPTPaymentsForScheduledTask ();

    List<Payment> getPaymentsBetweenDates (Date date1, Date date2);
}

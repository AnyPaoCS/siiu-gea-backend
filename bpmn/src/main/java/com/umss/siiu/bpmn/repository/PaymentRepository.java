package com.umss.siiu.bpmn.repository;

import com.umss.siiu.bpmn.model.Payment;
import com.umss.siiu.bpmn.model.PaymentType;
import com.umss.siiu.core.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PaymentRepository extends GenericRepository<Payment> {

    Payment findByProcessInstanceId(Long processInstanceId);

    List<Payment> findByPaymentType(PaymentType paymentType);

    @Query("SELECT payment FROM Payment payment WHERE payment.createdAt BETWEEN ?1 AND ?2")
    List<Payment> findPaymentsBetweenDates (Date date1, Date date2);

}

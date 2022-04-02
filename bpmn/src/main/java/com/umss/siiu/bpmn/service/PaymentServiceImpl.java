package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.Payment;
import com.umss.siiu.bpmn.model.PaymentType;
import com.umss.siiu.bpmn.repository.PaymentRepository;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl extends GenericServiceImpl<Payment> implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final static long TEN_DAYS = 1000 * 60 * 10;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment findByProcessInstanceId(Long processInstanceId) {
        return paymentRepository.findByProcessInstanceId(processInstanceId);
    }

    @Override
    public boolean verifyPaymentInformationIsCorrect(Long processInstanceId, BigDecimal amount) {
        Payment payment = paymentRepository.findByProcessInstanceId(processInstanceId);
        return payment.getAmount().equals(amount);
    }

    @Override
    public List<Payment> getPTPaymentsForScheduledTask() {
        Date now = new Date();
        Date datePast = new Date((now.getTime()-TEN_DAYS));
        List<Payment> paymentList = paymentRepository.findPaymentsBetweenDates(datePast, now);
        List<Payment> res = new ArrayList<>();
        System.out.println(paymentList.size());
        paymentList.forEach(p -> {
            System.out.println("########");
            System.out.println(p.getPaymentCode());
            System.out.println(p.getCreatedAt());
            System.out.println(p.getId());
            System.out.println(p.getProcessInstance());
            if (p.getPaymentType().equals(PaymentType.TRANSFERENCIA)) {
                res.add(p);
            }
        });
        System.out.println("*************");
        res.forEach(p -> {
            System.out.println("########");
            System.out.println(p.getPaymentCode());
            System.out.println(p.getCreatedAt());
            System.out.println(p.getId());
            System.out.println(p.getProcessInstance());
        });
        System.out.println("*************");
        paymentList = paymentList.stream().filter(item -> item.getPaymentType().equals(PaymentType.TRANSFERENCIA)).collect(Collectors.toList());
        System.out.println(paymentList.size());
        /* hacer query personal o ver como hacer para obtener entre un rango de fehcas*/
        return paymentList;
    }

    @Override
    public List<Payment> getPaymentsBetweenDates(Date date1, Date date2) {
        return paymentRepository.findPaymentsBetweenDates(date1, date2);
    }

    @Override
    protected GenericRepository<Payment> getRepository() {
        return paymentRepository;
    }
}

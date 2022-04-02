package com.umss.siiu.bpmn.schedulers;

import com.umss.siiu.bpmn.model.Payment;
import com.umss.siiu.bpmn.service.PaymentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class PaymentScheduler {

    private static final int TIME_DELAY = 1000 * 30;

    private final PaymentService paymentService;
    private volatile AtomicBoolean runningDaemon = new AtomicBoolean(false);

    public PaymentScheduler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Scheduled(fixedRate = TIME_DELAY)
    public void execRealizePTPayments () {
        System.out.println("scheduler payment");
        List<Payment> list = paymentService.getPTPaymentsForScheduledTask();
    }

}

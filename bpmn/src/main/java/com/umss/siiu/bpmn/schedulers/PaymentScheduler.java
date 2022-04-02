package com.umss.siiu.bpmn.schedulers;

import com.umss.siiu.bpmn.dto.PaymentList;
import com.umss.siiu.bpmn.model.Payment;
import com.umss.siiu.bpmn.service.PaymentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentScheduler {

    private static final int TIME_DELAY = 1000 * 60 * 10;

    private final PaymentService paymentService;

    public PaymentScheduler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Scheduled(fixedRate = TIME_DELAY)
    public void execRealizePTPayments () throws ServerException {
        System.out.println("scheduler payment");
        List<Payment> list = paymentService.getPTPaymentsForScheduledTask();
        if (list == null || list.isEmpty()) {
            System.out.println("No hay pagos pendientes por el momento");
            return ;
        }
        List<Long> idList = new ArrayList<>();
        for (Payment p : list) {
            idList.add(p.getId());
//            paymentService.realizePayment(p.getId());
        }
        PaymentList paymentList = new PaymentList();
        paymentList.setPaymenListId(idList);
        RestTemplate rest = new RestTemplate();
        PaymentList paymentsAccepted = rest.postForObject("http://localhost:8080/payment/mock", paymentList, PaymentList.class);
        if (paymentsAccepted == null) {
            throw new ServerException("Error en la api de banco union");
        }
        for (Long id : paymentsAccepted.getPaymenListId()) {
            System.out.println(id);
            paymentService.realizePayment(id);
        }
    }

}

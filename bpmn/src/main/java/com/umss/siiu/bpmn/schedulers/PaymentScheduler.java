package com.umss.siiu.bpmn.schedulers;

import com.umss.siiu.bpmn.dto.PaymentList;
import com.umss.siiu.bpmn.model.Payment;
import com.umss.siiu.bpmn.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentScheduler {

    private static final int TIME_DELAY = 1000 * 60 * 10;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PaymentService paymentService;

    public PaymentScheduler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Scheduled(fixedRate = TIME_DELAY)
    public void execRealizePTPayments () throws ServerException {
        List<Payment> list = paymentService.getPTPaymentsForScheduledTask();
        if (list == null || list.isEmpty()) {
            logger.info("No hay pagos pendientes por el momento");
            return ;
        }
        List<Long> idList = new ArrayList<>();
        for (Payment p : list) {
            idList.add(p.getProcessInstance().getId());
        }
        var paymentList = new PaymentList();
        paymentList.setPaymenListId(idList);
        var rest = new RestTemplate();
        PaymentList paymentsAccepted = rest.postForObject("http://localhost:8080/payment/mock", paymentList, PaymentList.class);
        if (paymentsAccepted == null) {
            throw new ServerException("Error en la api de banco union");
        }
        for (Long id : paymentsAccepted.getPaymenListId()) {
            paymentService.realizePaymentByProcessInstanceId(id);
        }
    }

}

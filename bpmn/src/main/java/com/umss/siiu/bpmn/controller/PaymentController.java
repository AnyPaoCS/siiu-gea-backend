package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.PaymentDto;
import com.umss.siiu.bpmn.dto.PaymentInfoDto;
import com.umss.siiu.bpmn.model.Payment;
import com.umss.siiu.bpmn.service.PaymentService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.service.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payments")
public class PaymentController extends GenericController<Payment, PaymentDto> {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> savePayment (@RequestBody PaymentInfoDto dto) {
        long paymentId = paymentService.savePayment(dto);
        return new ResponseEntity<>(paymentId, HttpStatus.OK);
    }

    @GetMapping("/information/{processInstanceId}")
    public ResponseEntity<PaymentInfoDto> getPaymentInformation (@PathVariable("processInstanceId") long processInstanceId) {
        PaymentInfoDto dto = paymentService.findByProcessInstanceId((Long)processInstanceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    protected GenericService<Payment> getService() {
        return paymentService;
    }
}

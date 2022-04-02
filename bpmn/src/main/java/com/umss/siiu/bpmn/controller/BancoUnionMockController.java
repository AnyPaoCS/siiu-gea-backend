package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.PaymentList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class BancoUnionMockController {

    public BancoUnionMockController() {}

    @PostMapping("/mock")
    public ResponseEntity<PaymentList> mockPayment (@RequestBody PaymentList list) {
        System.out.println("controller called");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}

package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.PaymentInfoDto;
import com.umss.siiu.core.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private UserService userService;


    @PostMapping("/{userId}")
    public RedirectView mockPayment (@RequestBody PaymentInfoDto dto) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port("8080")
                .path("")
                .buildAndExpand().toUriString();
        return new RedirectView(url);
    }
}

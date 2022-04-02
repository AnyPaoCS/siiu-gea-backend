package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.dto.PaymentInfoDto;
import com.umss.siiu.bpmn.model.Payment;
import com.umss.siiu.bpmn.model.PaymentStatus;
import com.umss.siiu.bpmn.model.PaymentType;
import com.umss.siiu.bpmn.model.processes.ProcessInstance;
import com.umss.siiu.bpmn.repository.PaymentRepository;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl extends GenericServiceImpl<Payment> implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ProcessInstanceService processInstanceService;
    private final static long TEN_MINUTES = 1000 * 60 * 10;

    public PaymentServiceImpl(PaymentRepository paymentRepository, ProcessInstanceService processInstanceService) {
        this.paymentRepository = paymentRepository;
        this.processInstanceService = processInstanceService;
    }

    @Override
    public long savePayment(PaymentInfoDto paymentInfoDto) {
        Payment p = new Payment();
        ProcessInstance pI = processInstanceService.findById(paymentInfoDto.getProcessId());
        p.setAmount(new BigDecimal(paymentInfoDto.getAmount()));
        p.setDescription(paymentInfoDto.getDescription());
        p.setPaymentCode("PT" + pI.getId());
        p.setPaymentStatus(PaymentStatus.valueOf(paymentInfoDto.getPaymentStatus()));
        p.setPaymentType(PaymentType.valueOf(paymentInfoDto.getPaymentType()));
        p.setProcessInstance(pI);
        return paymentRepository.save(p).getId();
    }

    @Override
    public PaymentInfoDto realizePayment(Long paymentId) {
        Payment p = paymentRepository.findById(paymentId).orElse(null);
        if (p == null) {
            return null;
        }
        p.setPaymentStatus(PaymentStatus.DONE);
        return new PaymentInfoDto(paymentRepository.save(p));
    }

    @Override
    public PaymentInfoDto realizePaymentByProcessInstanceId(Long processInstanceId) {
        Payment p = paymentRepository.findByProcessInstanceId(processInstanceId);
        if (p == null) {
            return null;
        }
        p.setPaymentStatus(PaymentStatus.DONE);
        return new PaymentInfoDto(paymentRepository.save(p));
    }

    @Override
    public PaymentInfoDto findByPaymentId(Long paymentId) {
        Payment p = paymentRepository.findById(paymentId).orElse(null);
        if (p == null) {
            return null;
        }
        return new PaymentInfoDto(p);
    }

    @Override
    public PaymentInfoDto findByProcessInstanceId(Long processInstanceId) {
        return new PaymentInfoDto(paymentRepository.findByProcessInstanceId(processInstanceId));
    }

    @Override
    public boolean verifyPaymentInformationIsCorrect(Long processInstanceId, BigDecimal amount) {
        Payment payment = paymentRepository.findByProcessInstanceId(processInstanceId);
        if (payment == null) {
            return false;
        }
        return payment.getAmount().equals(amount);
    }

    @Override
    public List<Payment> getPTPaymentsForScheduledTask() {
        Date now = new Date();
        Date datePast = new Date((now.getTime()-TEN_MINUTES));
        List<Payment> paymentList = paymentRepository.findPaymentsBetweenDates(datePast, now);
        List<Payment> res = new ArrayList<>();
        paymentList.forEach(p -> {
            if (p.getPaymentType().equals(PaymentType.TRANSFERENCIA) && p.getPaymentStatus().equals(PaymentStatus.PENDING)) {
                res.add(p);
            }
        });
        return res;
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

package com.f4.fqs.payment.controller;

import com.f4.fqs.payment.service.PaymentService;
import com.f4.fqs.payment.dto.PaymentApproveDto;
import com.f4.fqs.payment.dto.PaymentReadyDto;
import com.f4.fqs.payment.dto.PaymentReadyRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    final String FINAL_REDIRECT_URL = "success";

    //결제 요청
    @PostMapping("/ready")
    public ResponseEntity<PaymentReadyDto> readyToPay(@ModelAttribute PaymentReadyRequest request) {

        log.info("결제 요청: {}", request);

        PaymentReadyDto response = paymentService.PaymentReady(request);

        String tid = response.getTid();

        paymentService.saveTid(tid);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(response.getNext_redirect_pc_url()))
                .build();
    }

    @GetMapping("/approve")
    public ResponseEntity<Void> approve(@RequestParam("pg_token") String pgToken, RedirectAttributes redirectAttributes) {
        PaymentApproveDto response = paymentService.payApprove(pgToken);
        paymentService.savePaymentInfo(response);

        redirectAttributes.addFlashAttribute("tid", response.getTid());
        redirectAttributes.addFlashAttribute("amount", response.getAmount().getTotal());
        redirectAttributes.addFlashAttribute("itemName", response.getItem_name());
        redirectAttributes.addFlashAttribute("quantity", response.getQuantity());
        redirectAttributes.addFlashAttribute("createdAt", response.getCreated_at());
        redirectAttributes.addFlashAttribute("approvedAt", response.getApproved_at());

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/payment/success"))
                .build();
    }

    @GetMapping("/success")
    public String success(Model model) {
        return FINAL_REDIRECT_URL;
    }
}

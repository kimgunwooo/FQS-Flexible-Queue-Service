package com.f4.fqs.payment.service;

import com.f4.fqs.payment.domain.Payment;
import com.f4.fqs.payment.dto.PaymentApproveDto;
import com.f4.fqs.payment.dto.PaymentReadyDto;
import com.f4.fqs.payment.dto.PaymentReadyRequest;
import com.f4.fqs.payment.repository.PaymentRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드

    @Value("${payment.kakao.amdin_key}")
    private String SECRET_KEY;

    private String SECRET_KEY_PREFIX = "SECRET_KEY ";

    private String tid;

    public PaymentReadyDto PaymentReady(PaymentReadyRequest request) {



        // 카카오페이 요청 양식
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("partner_order_id", "가맹점 주문 번호");
        parameters.put("partner_user_id", "가맹점 회원 ID");
        parameters.put("item_name", request.getItemName());
        parameters.put("quantity", String.valueOf(request.getQuantity()));
        parameters.put("total_amount", String.valueOf(request.getTotalAmount()));
        parameters.put("tax_free_amount", String.valueOf(request.getTaxFreeAmount()));

        parameters.put("approval_url", "http://localhost:19097/payment/approve"); // 성공 시 redirect url

        parameters.put("cancel_url", "http://developers.kakao.com/cancle"); // 취소 시 redirect url
        parameters.put("fail_url", "http://developers.kakao.com/fail"); // 실패 시 redirect url

        // 파라미터, 헤더
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
        ResponseEntity<PaymentReadyDto> responseEntity = restTemplate.postForEntity(url, requestEntity, PaymentReadyDto.class);

        return responseEntity.getBody();
    }


    public void saveTid(String tid) {
        this.tid = tid;
    }

    public PaymentApproveDto payApprove(String pgToken){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", tid);
        parameters.put("partner_order_id", "가맹점 주문 번호");
        parameters.put("partner_user_id", "가맹점 회원 ID");
        parameters.put("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        ResponseEntity<PaymentApproveDto> responseEntity = restTemplate.postForEntity(url, requestEntity, PaymentApproveDto.class);

        return responseEntity.getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = SECRET_KEY_PREFIX + SECRET_KEY;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/json");


        return httpHeaders;
    }

    public void savePaymentInfo(PaymentApproveDto response) {

        Payment payment = Payment.builder()
                .tid(response.getTid())
                .price(response.getAmount().getTotal())
                .itemName(response.getItem_name())
                .month(response.getQuantity())
                .created_at(response.getCreated_at())
                .approved_at(response.getApproved_at())
                .build();

    paymentRepository.save(payment);

    }
}

package com.f4.fqs.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReadyRequest {

    private String itemName;
    private int quantity;
    private int totalAmount;
    private int taxFreeAmount;

}

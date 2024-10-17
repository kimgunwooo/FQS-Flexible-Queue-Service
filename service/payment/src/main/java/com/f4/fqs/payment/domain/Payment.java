package com.f4.fqs.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Entity
@Getter
@Table(name = "p_payment_history")
@NoArgsConstructor
public class Payment {

    @Id
    @Column(name = "id",nullable = false)
    private String tid;

    @Column(name = "price")
    private int price;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "month")
    private int month;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "approved_at")
    private String approved_at;

}

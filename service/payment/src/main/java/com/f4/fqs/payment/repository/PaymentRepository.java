package com.f4.fqs.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.f4.fqs.payment.domain.Payment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

}

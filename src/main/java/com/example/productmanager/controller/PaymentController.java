package com.example.productmanager.controller;

import com.example.productmanager.dao.TransactionRepository;
import com.example.productmanager.dao.UserRepository;
import com.example.productmanager.entity.Transaction;
import com.example.productmanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentController {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentController(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/check-payment")
    public ResponseEntity<String> checkPaymentStatus(@RequestParam Long userId) {
        List<Transaction> userTransactions = transactionRepository.findByUser_UserId(userId);
        BigDecimal totalAmount = userTransactions.stream()
                .filter(transaction -> "SUCCESS".equals(transaction.getStatus()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalAmount.compareTo(new BigDecimal("100")) >= 0) {
            return ResponseEntity.ok("User has made a payment of at least 100 units.");
        } else {
            return ResponseEntity.ok("User has not made a sufficient payment.");
        }
    }

    @GetMapping("/checkTopUpStatus")
    public ResponseEntity<String> checkTopUpStatus(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        List<Transaction> transactions = transactionRepository.findByUser(user);

        if (transactions.isEmpty()) {
            return ResponseEntity.ok("User has not made any top-up transactions.");
        } else {
            return ResponseEntity.ok("User has made top-up transactions.");
        }
    }

}

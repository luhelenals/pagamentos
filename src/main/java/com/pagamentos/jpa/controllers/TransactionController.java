package com.pagamentos.jpa.controllers;

import com.pagamentos.jpa.dtos.TransactionRecordDto;
import com.pagamentos.jpa.models.TransactionModel;
import com.pagamentos.jpa.repositories.TransactionRepository;
import com.pagamentos.jpa.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionService transactionService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping
    public ResponseEntity<TransactionModel> saveTransaction(@RequestBody TransactionRecordDto transactionRecordDto) {
        TransactionModel transaction = transactionService.saveTransaction(transactionRecordDto);
        if (transaction == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionModel>> findAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }
}

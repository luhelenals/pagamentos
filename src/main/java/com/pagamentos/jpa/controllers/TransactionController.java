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
@RequestMapping("/pagamentos/transactions") // Rota base para operações com transações
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    // Construtor para injeção dos serviços e repositórios necessários
    public TransactionController(TransactionService transactionService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    // Endpoint para salvar uma nova transação
    @PostMapping
    public ResponseEntity<TransactionModel> saveTransaction(@RequestBody TransactionRecordDto transactionRecordDto) {
        // Chama o serviço para salvar a transação
        TransactionModel transaction = transactionService.saveTransaction(transactionRecordDto);

        // Se a transação for inválida, retorna 400 (Bad Request)
        if (transaction == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // Caso contrário, retorna a transação salva com status 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    // Endpoint para listar todas as transações
    @GetMapping
    public ResponseEntity<List<TransactionModel>> findAllTransactions() {
        // Retorna todas as transações registradas com status 200 (OK)
        return ResponseEntity.ok(transactionRepository.findAll());
    }
}
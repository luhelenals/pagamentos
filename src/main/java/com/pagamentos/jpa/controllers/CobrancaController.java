package com.pagamentos.jpa.controllers;

import com.pagamentos.jpa.dtos.CobrancaRecordDto;
import com.pagamentos.jpa.models.CobrancaModel;
import com.pagamentos.jpa.models.TransactionModel;
import com.pagamentos.jpa.repositories.CobrancaRepository;
import com.pagamentos.jpa.services.CobrancaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos/cobrancas")
public class CobrancaController {

    private final CobrancaService cobrancaService;
    private final CobrancaRepository cobrancaRepository;

    public CobrancaController(CobrancaService cobrancaService, CobrancaRepository cobrancaRepository) {
        this.cobrancaService = cobrancaService;
        this.cobrancaRepository = cobrancaRepository;
    }

    @PostMapping
    public ResponseEntity<CobrancaModel> saveCobranca(@RequestBody CobrancaRecordDto cobrancaRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cobrancaService.saveCobranca(cobrancaRecordDto));
    }

    @GetMapping
    public ResponseEntity<List<CobrancaModel>> findAllTransactions() {
        return ResponseEntity.ok(cobrancaRepository.findAll());
    }
}

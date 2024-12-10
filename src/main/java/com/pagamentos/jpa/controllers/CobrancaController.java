package com.pagamentos.jpa.controllers;

import com.pagamentos.jpa.dtos.CobrancaRecordDto;
import com.pagamentos.jpa.models.CobrancaModel;
import com.pagamentos.jpa.repositories.CobrancaRepository;
import com.pagamentos.jpa.services.CobrancaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos/cobrancas") // Rota base para operações com cobranças
public class CobrancaController {

    private final CobrancaService cobrancaService;
    private final CobrancaRepository cobrancaRepository;

    // Construtor para injeção dos serviços e repositórios necessários
    public CobrancaController(CobrancaService cobrancaService, CobrancaRepository cobrancaRepository) {
        this.cobrancaService = cobrancaService;
        this.cobrancaRepository = cobrancaRepository;
    }

    // Endpoint para salvar uma nova cobrança
    @PostMapping
    public ResponseEntity<CobrancaModel> saveCobranca(@RequestBody CobrancaRecordDto cobrancaRecordDto) {
        // Chama o serviço para salvar a cobrança e retorna 201 (Created) com a cobrança salva
        return ResponseEntity.status(HttpStatus.CREATED).body(cobrancaService.saveCobranca(cobrancaRecordDto));
    }

    // Endpoint para listar todas as cobranças
    @GetMapping
    public ResponseEntity<List<CobrancaModel>> findAllTransactions() {
        // Retorna todas as cobranças registradas com status 200 (OK)
        return ResponseEntity.ok(cobrancaRepository.findAll());
    }
}
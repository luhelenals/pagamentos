package com.pagamentos.jpa.controllers;

import com.pagamentos.jpa.dtos.CobrancaRecordDto;
import com.pagamentos.jpa.models.CobrancaModel;
import com.pagamentos.jpa.services.CobrancaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos/cobrancas")
public class CobrancaController {

    private final CobrancaService cobrancaService;

    public CobrancaController(CobrancaService cobrancaService) {
        this.cobrancaService = cobrancaService;
    }

    @PostMapping
    public ResponseEntity<CobrancaModel> saveCobranca(@RequestBody CobrancaRecordDto cobrancaRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cobrancaService.saveCobranca(cobrancaRecordDto));
    }
}

package com.pagamentos.jpa.controllers;

import com.pagamentos.jpa.dtos.CardRecordDto;
import com.pagamentos.jpa.models.CardModel;
import com.pagamentos.jpa.repositories.CardRepository;
import com.pagamentos.jpa.services.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos/cartoes") // Rota base para operações com cartões
public class CardController {

    private final CardService cardService;
    private final CardRepository cardRepository;

    // Construtor para injeção das dependências necessárias
    public CardController(CardService cardService, CardRepository cardRepository) {
        this.cardService = cardService;
        this.cardRepository = cardRepository;
    }

    // Endpoint para salvar um novo cartão
    @PostMapping
    public ResponseEntity<CardModel> saveCard(@RequestBody CardRecordDto cardRecordDto) {
        // Chama o serviço para salvar o cartão e retorna 201 (Created) com o cartão salvo
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.saveCard(cardRecordDto));
    }

    // Endpoint para listar todos os cartões
    @GetMapping
    public ResponseEntity<List<CardModel>> findAllTransactions() {
        // Retorna todos os cartões cadastrados com status 200 (OK)
        return ResponseEntity.ok(cardRepository.findAll());
    }
}
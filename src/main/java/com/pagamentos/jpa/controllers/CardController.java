package com.pagamentos.jpa.controllers;

import com.pagamentos.jpa.dtos.CardRecordDto;
import com.pagamentos.jpa.models.CardModel;
import com.pagamentos.jpa.models.TransactionModel;
import com.pagamentos.jpa.repositories.CardRepository;
import com.pagamentos.jpa.services.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos/cartoes")
public class CardController {

    private final CardService cardService;
    private final CardRepository cardRepository;

    public CardController(CardService cardService, CardRepository cardRepository) {
        this.cardService = cardService;
        this.cardRepository = cardRepository;
    }

    @PostMapping
    public ResponseEntity<CardModel> saveCard(@RequestBody CardRecordDto cardRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.saveCard(cardRecordDto));
    }

    @GetMapping
    public ResponseEntity<List<CardModel>> findAllTransactions() {
        return ResponseEntity.ok(cardRepository.findAll());
    }
}

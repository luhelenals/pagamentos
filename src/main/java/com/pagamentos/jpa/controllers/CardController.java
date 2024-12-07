package com.pagamentos.jpa.controllers;

import com.pagamentos.jpa.dtos.CardRecordDto;
import com.pagamentos.jpa.models.CardModel;
import com.pagamentos.jpa.services.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos/cartoes")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardModel> saveCard(@RequestBody CardRecordDto cardRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.saveCard(cardRecordDto));
    }
}

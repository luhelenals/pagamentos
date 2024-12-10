package com.pagamentos.jpa.controllers;

import com.pagamentos.jpa.dtos.CardVerificationResponseDto;
import com.pagamentos.jpa.models.CardModel;
import com.pagamentos.jpa.services.CardValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/verifica-cartao")
public class CardVerificationController {

    private CardValidationService cardValidationService;

    public CardVerificationController(CardValidationService cardValidationService) {
        this.cardValidationService = cardValidationService;
    }

    @PostMapping
    public boolean verifyCard(@RequestBody CardModel cartao) {
        return cardValidationService.verifyCard(cartao);
    }
}

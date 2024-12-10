package com.pagamentos.jpa.controllers;

import com.pagamentos.jpa.models.CardModel;
import com.pagamentos.jpa.services.CardValidationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verifica-cartao") // Rota base para verificação de cartões
public class CardVerificationController {

    private CardValidationService cardValidationService;

    // Construtor para injeção do serviço de validação de cartão
    public CardVerificationController(CardValidationService cardValidationService) {
        this.cardValidationService = cardValidationService;
    }

    // Endpoint para verificar um cartão
    @PostMapping
    public boolean verifyCard(@RequestBody CardModel cartao) {
        // Chama o serviço para validar o cartão e retorna o resultado da validação
        return cardValidationService.verifyCard(cartao);
    }
}

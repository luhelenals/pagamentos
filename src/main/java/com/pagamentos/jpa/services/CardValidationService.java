package com.pagamentos.jpa.services;

import com.pagamentos.jpa.dtos.CardVerificationResponseDto;
import com.pagamentos.jpa.models.CardModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CardValidationService {

    private final RestTemplate restTemplate;

    public CardValidationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifyCard(CardModel cartao) {
        String url = String.format(
                "https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer?cardNumber=%s&expiryDate=%s&cvv=%s",
                cartao.getNumero(),
                cartao.getValidade(),
                cartao.getCvv()
        );

        try {
            ResponseEntity<CardVerificationResponseDto> response = restTemplate.getForEntity(url, CardVerificationResponseDto.class);
            return response.getBody().data().authorized();
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Retorna false caso a requisição falhe
        }
    }
}

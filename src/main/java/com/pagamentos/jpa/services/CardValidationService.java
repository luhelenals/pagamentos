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

    // Método para verificar a validade de um cartão
    public boolean verifyCard(CardModel cartao) {
        // Formata a URL para a requisição à API de verificação de cartões
        String url = String.format(
                "https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer?cardNumber=%s&expiryDate=%s&cvv=%s",
                cartao.getNumero(),
                cartao.getValidade(),
                cartao.getCvv()
        );

        try {
            // Realiza a requisição GET para a URL e obtém a resposta como um objeto CardVerificationResponseDto
            ResponseEntity<CardVerificationResponseDto> response = restTemplate.getForEntity(url, CardVerificationResponseDto.class);

            // Retorna se o cartão foi autorizado, de acordo com a resposta da API
            return response.getBody().data().authorized();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime o erro no console caso ocorra uma falha na requisição
            return false; // Retorna false caso a requisição falhe
        }
    }
}

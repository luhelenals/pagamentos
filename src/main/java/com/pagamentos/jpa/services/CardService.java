package com.pagamentos.jpa.services;

import com.pagamentos.jpa.dtos.CardRecordDto;
import com.pagamentos.jpa.dtos.CobrancaRecordDto;
import com.pagamentos.jpa.models.CardModel;
import com.pagamentos.jpa.models.CobrancaModel;
import com.pagamentos.jpa.repositories.CardRepository;
import com.pagamentos.jpa.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CardModel saveCard(CardRecordDto cardRecordDto) {
        CardModel card = new CardModel();
        card.setValidade(cardRecordDto.validade());
        card.setCvv(cardRecordDto.cvv());
        card.setNumero(cardRecordDto.numero());
        card.setUser(userRepository.findById(cardRecordDto.user_id()).get());

        return cardRepository.save(card);
    }
}

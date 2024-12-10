package com.pagamentos.jpa.services;

import com.pagamentos.jpa.dtos.CardRecordDto; // Importa o DTO que contém os dados do cartão
import com.pagamentos.jpa.models.CardModel; // Importa a classe que representa o modelo do cartão
import com.pagamentos.jpa.repositories.CardRepository; // Importa o repositório para gerenciar os cartões no banco de dados
import com.pagamentos.jpa.repositories.UserRepository; // Importa o repositório para gerenciar os usuários no banco de dados
import jakarta.transaction.Transactional; // Importa a anotação para garantir que o método seja executado de forma transacional
import org.springframework.stereotype.Service; // Anotação para marcar a classe como um serviço gerenciado pelo Spring

@Service // Define a classe como um serviço para ser gerenciado pelo Spring
public class CardService {

    // Declaração das variáveis para os repositórios de cartão e usuário
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    // Construtor que recebe os repositórios e os inicializa
    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    // Método que salva um novo cartão no banco de dados
    @Transactional // Garante que o método será executado de forma transacional (toda a operação será tratada como uma única transação)
    public CardModel saveCard(CardRecordDto cardRecordDto) {
        CardModel card = new CardModel(); // Cria um novo objeto CardModel

        // Define os atributos do cartão a partir dos dados recebidos no DTO
        card.setValidade(cardRecordDto.validade()); // Define a validade do cartão
        card.setCvv(cardRecordDto.cvv()); // Define o CVV do cartão
        card.setNumero(cardRecordDto.numero()); // Define o número do cartão
        card.setUser(userRepository.findById(cardRecordDto.user_id()).get()); // Associa o cartão a um usuário, usando o ID do usuário do DTO

        // Salva o cartão no banco de dados e retorna o objeto salvo
        return cardRepository.save(card);
    }
}

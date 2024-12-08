package com.pagamentos.jpa.services;

import com.pagamentos.jpa.dtos.TransactionRecordDto;
import com.pagamentos.jpa.models.*;
import com.pagamentos.jpa.repositories.CardRepository;
import com.pagamentos.jpa.repositories.CobrancaRepository;
import com.pagamentos.jpa.repositories.TransactionRepository;
import com.pagamentos.jpa.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CobrancaRepository cobrancaRepository;
    private final CardRepository cardRepository;

    public TransactionService(UserRepository userRepository,
                              CobrancaRepository cobrancaRepository,
                              TransactionRepository transactionRepository,
                              CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.cobrancaRepository = cobrancaRepository;
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public TransactionModel saveTransaction(TransactionRecordDto transactionRecordDto) {
        TransactionModel transaction = new TransactionModel();

        transaction.setTipo(transactionRecordDto.tipo());

        switch(transaction.getTipo()) {
            case DEPOSITO -> {
                UserModel user = userRepository.findById(transactionRecordDto.user_origem_id()).get();
                user.setSaldo(user.getSaldo().add(transactionRecordDto.valor()));
                transaction.setCreatedAt(LocalDateTime.now());
                transaction.setUserOrigem(user);
                transaction.setValor(transactionRecordDto.valor());

                return transactionRepository.save(transaction);
            }
            case PAGAMENTO_CARTAO -> {
                // implementar autorização externa
                CardModel card = cardRepository.findById(transactionRecordDto.card_id()).get();
                // https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer
                if(true) {
                    CobrancaModel cobranca = cobrancaRepository.findById(transactionRecordDto.cobranca_id()).get();

                    UserModel userDestino = userRepository.findById(cobranca.getUserDestino().getId()).get();
                    UserModel userOrigem = userRepository.findById(cobranca.getUserOrigem().getId()).get();
                    userDestino.setSaldo(userDestino.getSaldo().subtract(cobranca.getValor()));

                    cobranca.setStatus(StatusCobranca.REALIZADO);
                    transaction.setCreatedAt(LocalDateTime.now());

                    transaction.setUserDestino(userDestino);
                    transaction.setUserOrigem(userOrigem);

                    transaction.setValor(cobranca.getValor());

                    return transactionRepository.save(transaction);
                }
                else {
                    System.out.println("Transação negada.");
                }
            }
            case PAGAMENTO_SALDO -> {
                CobrancaModel cobranca = cobrancaRepository.findById(transactionRecordDto.cobranca_id()).get();

                UserModel userOrigem = userRepository.findById(cobranca.getUserOrigem().getId()).get();

                // Verifica se o saldo do usuário de origem é suficiente
                if (userOrigem.getSaldo().compareTo(cobranca.getValor()) >= 0) {
                    UserModel userDestino = userRepository.findById(cobranca.getUserDestino().getId()).get();

                    // Atualiza o saldo do usuário de origem (subtrai o valor da cobrança)
                    userOrigem.setSaldo(userOrigem.getSaldo().subtract(cobranca.getValor()));

                    // Atualiza o saldo do usuário de destino (adiciona o valor da cobrança)
                    userDestino.setSaldo(userDestino.getSaldo().add(cobranca.getValor()));

                    // Atualiza o status da cobrança
                    cobranca.setStatus(StatusCobranca.REALIZADO);

                    // Configura os detalhes da transação
                    transaction.setValor(cobranca.getValor());
                    transaction.setCreatedAt(LocalDateTime.now());
                    transaction.setUserOrigem(userOrigem);
                    transaction.setUserDestino(userDestino);

                    // Salva a transação no repositório
                    return transactionRepository.save(transaction);
                } else {
                    System.out.println("Saldo insuficiente para realizar a transação.");
                    throw new RuntimeException("Saldo insuficiente.");
                }
            }
        }
        return transaction;
    }
}

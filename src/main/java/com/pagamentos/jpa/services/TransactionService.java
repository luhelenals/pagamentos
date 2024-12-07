package com.pagamentos.jpa.services;

import com.pagamentos.jpa.dtos.TransactionRecordDto;
import com.pagamentos.jpa.models.*;
import com.pagamentos.jpa.repositories.CardRepository;
import com.pagamentos.jpa.repositories.CobrancaRepository;
import com.pagamentos.jpa.repositories.TransactionRepository;
import com.pagamentos.jpa.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
            }
            case PAGAMENTO_CARTAO -> {
                // implementar autorização externa
                CardModel card = cardRepository.findById(transactionRecordDto.card_id()).get();
                // https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer
                if(true) {
                    CobrancaModel cobranca = cobrancaRepository.findById(transactionRecordDto.cobranca_id()).get();

                    UserModel userDestino = userRepository.findById(cobranca.getUserDestino().getId()).get();
                    userDestino.setSaldo(userDestino.getSaldo().subtract(transactionRecordDto.valor()));

                    cobranca.setStatus(StatusCobranca.REALIZADO);
                }
                else {
                    System.out.println("Transação negada.");
                }
            }
            case PAGAMENTO_SALDO -> {
                CobrancaModel cobranca = cobrancaRepository.findById(transactionRecordDto.cobranca_id()).get();

                UserModel userOrigem = userRepository.findById(cobranca.getUserDestino().getId()).get();
                userOrigem.setSaldo(userOrigem.getSaldo().subtract(transactionRecordDto.valor()));

                if(userOrigem.getSaldo().compareTo(transactionRecordDto.valor()) >= 0) {
                    userOrigem.setSaldo(userOrigem.getSaldo().subtract(transactionRecordDto.valor()));

                    UserModel userDestino = userRepository.findById(cobranca.getUserDestino().getId()).get();
                    userDestino.setSaldo(userDestino.getSaldo().subtract(transactionRecordDto.valor()));

                    cobranca.setStatus(StatusCobranca.REALIZADO);
                }
                else {
                    System.out.println("Saldo insuficiente.");
                }
            }
        }
        return transactionRepository.save(transaction);
    }
}

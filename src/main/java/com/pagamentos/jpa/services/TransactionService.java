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
    private final CardValidationService cardValidationService;

    // Construtor para injeção de dependências
    public TransactionService(UserRepository userRepository,
                              CobrancaRepository cobrancaRepository,
                              TransactionRepository transactionRepository,
                              CardRepository cardRepository,
                              CardValidationService cardValidationService) {
        this.userRepository = userRepository;
        this.cobrancaRepository = cobrancaRepository;
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.cardValidationService = cardValidationService;
    }

    @Transactional // Garante que as operações realizadas dentro do método sejam tratadas como uma transação
    public TransactionModel saveTransaction(TransactionRecordDto transactionRecordDto) {
        // Valida os dados da transação
        if (transactionRecordDto.valor() == null
                || transactionRecordDto.tipo() == null
                || (transactionRecordDto.cobranca_id() == null
                && (transactionRecordDto.tipo() == TransactionType.PAGAMENTO_CARTAO
                || transactionRecordDto.tipo() == TransactionType.PAGAMENTO_SALDO))
                || ((transactionRecordDto.cvv_cartao() == null || transactionRecordDto.numero_cartao() == null || transactionRecordDto.validade_cartao() == null)
                && transactionRecordDto.tipo() == TransactionType.DEPOSITO || transactionRecordDto.tipo() == TransactionType.PAGAMENTO_CARTAO))
            return null; // Retorna null caso a transação não seja válida

        TransactionModel transaction = new TransactionModel(); // Cria uma nova instância de transação

        transaction.setTipo(transactionRecordDto.tipo()); // Define o tipo da transação

        switch(transaction.getTipo()) {
            case DEPOSITO -> {
                // Para o tipo DEPOSITO, valida os dados do cartão e faz a transação
                CardModel card = cardRepository.findCardByAttributes(
                        transactionRecordDto.numero_cartao(),
                        transactionRecordDto.cvv_cartao(),
                        transactionRecordDto.validade_cartao(),
                        transactionRecordDto.user_origem_id());

                boolean autorizacao = cardValidationService.verifyCard(card); // Valida o cartão com o serviço externo

                if(autorizacao) {
                    UserModel user = userRepository.findById(transactionRecordDto.user_origem_id()).get();
                    user.setSaldo(user.getSaldo().add(transactionRecordDto.valor())); // Atualiza o saldo do usuário
                    transaction.setCreatedAt(LocalDateTime.now()); // Define o horário da transação
                    transaction.setUserOrigem(user); // Define o usuário de origem da transação
                    transaction.setValor(transactionRecordDto.valor()); // Define o valor da transação

                    // Salva e retorna a transação
                    return transactionRepository.save(transaction);
                } else {
                    System.out.println("Transação negada.");
                    throw new RuntimeException("Transação negada pela autorização externa.");
                }
            }
            case PAGAMENTO_CARTAO -> {
                // Para o tipo PAGAMENTO_CARTAO, valida o cartão e faz a transação para pagamento de uma cobrança
                CardModel card = cardRepository.findCardByAttributes(
                        transactionRecordDto.numero_cartao(),
                        transactionRecordDto.cvv_cartao(),
                        transactionRecordDto.validade_cartao(),
                        transactionRecordDto.user_origem_id());

                boolean autorizacao = cardValidationService.verifyCard(card); // Valida o cartão

                if (autorizacao) {
                    // Busca a cobrança associada à transação
                    CobrancaModel cobranca = cobrancaRepository.findById(transactionRecordDto.cobranca_id())
                            .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));

                    // Busca o usuário de destino e atualiza seu saldo
                    UserModel userDestino = userRepository.findById(cobranca.getUserOrigem().getId())
                            .orElseThrow(() -> new RuntimeException("Usuário de destino não encontrado"));
                    userDestino.setSaldo(userDestino.getSaldo().add(cobranca.getValor())); // Atualiza o saldo do destino

                    // Atualiza o status da cobrança e os detalhes da transação
                    cobranca.setStatus(StatusCobranca.REALIZADO);
                    transaction.setValor(cobranca.getValor());
                    transaction.setCreatedAt(LocalDateTime.now());
                    transaction.setUserDestino(userDestino);

                    // O usuário de origem da cobrança é referenciado, mas não afeta o saldo
                    UserModel userOrigem = userRepository.findById(cobranca.getUserOrigem().getId())
                            .orElseThrow(() -> new RuntimeException("Usuário de origem não encontrado"));
                    transaction.setUserOrigem(userOrigem);

                    // Salva e retorna a transação
                    return transactionRepository.save(transaction);
                } else {
                    System.out.println("Transação negada.");
                    throw new RuntimeException("Transação negada pela autorização externa.");
                }
            }
            case PAGAMENTO_SALDO -> {
                // Para o tipo PAGAMENTO_SALDO, realiza o pagamento utilizando o saldo do usuário de origem
                CobrancaModel cobranca = cobrancaRepository.findById(transactionRecordDto.cobranca_id()).get();

                UserModel userOrigem = userRepository.findById(cobranca.getUserOrigem().getId()).get();

                // Verifica se o saldo do usuário de origem é suficiente para a transação
                if (userOrigem.getSaldo().compareTo(cobranca.getValor()) >= 0) {
                    UserModel userDestino = userRepository.findById(cobranca.getUserDestino().getId()).get();

                    // Atualiza os saldos de origem e destino
                    userOrigem.setSaldo(userOrigem.getSaldo().subtract(cobranca.getValor()));
                    userDestino.setSaldo(userDestino.getSaldo().add(cobranca.getValor()));

                    // Atualiza o status da cobrança
                    cobranca.setStatus(StatusCobranca.REALIZADO);

                    // Configura os detalhes da transação
                    transaction.setValor(cobranca.getValor());
                    transaction.setCreatedAt(LocalDateTime.now());
                    transaction.setUserOrigem(userOrigem);
                    transaction.setUserDestino(userDestino);

                    // Salva e retorna a transação
                    return transactionRepository.save(transaction);
                } else {
                    System.out.println("Saldo insuficiente para realizar a transação.");
                    throw new RuntimeException("Saldo insuficiente.");
                }
            }
        }
        return null; // Retorna null se nenhum tipo de transação for correspondido
    }
}
package com.pagamentos.jpa.services;

import com.pagamentos.jpa.dtos.CobrancaRecordDto;
import com.pagamentos.jpa.models.CobrancaModel;
import com.pagamentos.jpa.models.StatusCobranca;
import com.pagamentos.jpa.models.UserModel;
import com.pagamentos.jpa.repositories.CobrancaRepository;
import com.pagamentos.jpa.repositories.TransactionRepository;
import com.pagamentos.jpa.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CobrancaService {
    private final UserRepository userRepository;
    private final CobrancaRepository cobrancaRepository;

    // Construtor para injeção de dependências
    public CobrancaService(UserRepository userRepository,
                           CobrancaRepository cobrancaRepository,
                           TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.cobrancaRepository = cobrancaRepository;
    }

    @Transactional // Garante que as operações realizadas dentro do método sejam tratadas como uma transação
    public CobrancaModel saveCobranca(CobrancaRecordDto cobrancaRecordDto) {
        CobrancaModel cobranca = new CobrancaModel();

        // Preenche os dados da cobrança com base no DTO recebido
        cobranca.setCPF_destino(cobrancaRecordDto.cpf_destino());
        cobranca.setCPF_origem(cobrancaRecordDto.cpf_origem());
        cobranca.setDescricao(cobrancaRecordDto.descricao());
        cobranca.setStatus(StatusCobranca.PENDENTE); // Define o status inicial da cobrança como "PENDENTE"
        cobranca.setValor(cobrancaRecordDto.valor());

        // Encontra o usuário de origem utilizando o CPF e associa a cobrança a ele
        UserModel userOrigem = userRepository.findUserByCPF(cobranca.getCPF_origem());
        userOrigem.addCobrancaFeita(cobranca); // Adiciona a cobrança à lista de cobranças feitas pelo usuário

        // Encontra o usuário de destino utilizando o CPF e associa a cobrança a ele
        UserModel userDestino = userRepository.findUserByCPF(cobranca.getCPF_destino());
        userDestino.addCobrancaRecebida(cobranca); // Adiciona a cobrança à lista de cobranças recebidas pelo usuário

        // Salva a cobrança no repositório e retorna o objeto salvo
        return cobrancaRepository.save(cobranca);
    }
}

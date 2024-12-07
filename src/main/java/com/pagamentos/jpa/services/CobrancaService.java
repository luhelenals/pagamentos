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

    public CobrancaService(UserRepository userRepository,
                           CobrancaRepository cobrancaRepository,
                           TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.cobrancaRepository = cobrancaRepository;
    }

    @Transactional
    public CobrancaModel saveCobranca(CobrancaRecordDto cobrancaRecordDto) {
        CobrancaModel cobranca = new CobrancaModel();

        cobranca.setCPF_destino(cobrancaRecordDto.cpf_destino());
        cobranca.setCPF_origem(cobrancaRecordDto.cpf_origem());
        cobranca.setDescricao(cobrancaRecordDto.descricao());
        cobranca.setStatus(StatusCobranca.PENDENTE); // Status inicial default
        cobranca.setValor(cobrancaRecordDto.valor());

        UserModel userOrigem = userRepository.findUserByCPF(cobranca.getCPF_origem());
        userOrigem.addCobrancaFeita(cobranca);

        UserModel userDestino = userRepository.findUserByCPF(cobranca.getCPF_destino());
        userDestino.addCobrancaRecebida(cobranca);

        return cobrancaRepository.save(cobranca);
    }

}

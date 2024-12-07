package com.pagamentos.jpa.dtos;

import com.pagamentos.jpa.models.CobrancaModel;
import com.pagamentos.jpa.models.TransactionModel;

import java.math.BigDecimal;
import java.util.Set;

public record UserRecordDto(String nome,
                            String cpf,
                            String email,
                            String senha,
                            BigDecimal saldo,
                            Set<CobrancaModel> cobrancas,
                            Set<TransactionModel> transacoes) {
}

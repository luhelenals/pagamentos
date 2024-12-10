package com.pagamentos.jpa.dtos;

import com.pagamentos.jpa.models.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRecordDto(TransactionType tipo,
                                   String numero_cartao,
                                   String validade_cartao,
                                   String cvv_cartao,
                                   UUID cobranca_id,
                                   BigDecimal valor,
                                   UUID user_origem_id) {
}

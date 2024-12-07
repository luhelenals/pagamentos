package com.pagamentos.jpa.dtos;

import com.pagamentos.jpa.models.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRecordDto(TransactionType tipo,
                                   UUID card_id,
                                   UUID cobranca_id,
                                   BigDecimal valor,
                                   UUID user_origem_id) {
}

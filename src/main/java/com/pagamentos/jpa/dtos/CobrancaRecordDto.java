package com.pagamentos.jpa.dtos;

import java.math.BigDecimal;

public record CobrancaRecordDto(String cpf_origem,
                                String cpf_destino,
                                BigDecimal valor,
                                String descricao) {
}

package com.pagamentos.jpa.dtos;

import java.math.BigDecimal;

public record UserRecordDto(String nome,
                            String cpf,
                            String email,
                            String senha,
                            BigDecimal saldo) {
}

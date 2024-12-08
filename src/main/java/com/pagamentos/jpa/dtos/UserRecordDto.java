package com.pagamentos.jpa.dtos;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record UserRecordDto(String nome,
                            String cpf,
                            String email,
                            String senha,
                            BigDecimal saldo) {
}

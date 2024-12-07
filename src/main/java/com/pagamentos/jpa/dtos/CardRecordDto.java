package com.pagamentos.jpa.dtos;

import java.util.UUID;

public record CardRecordDto(String numero,
                            String cvv,
                            String validade,
                            UUID user_id) {
}

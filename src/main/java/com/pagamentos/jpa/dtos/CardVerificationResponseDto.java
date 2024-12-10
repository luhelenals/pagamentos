package com.pagamentos.jpa.dtos;

public record CardVerificationResponseDto (String status, DataDTO data) {

    public record DataDTO(boolean authorized) {
    }
}

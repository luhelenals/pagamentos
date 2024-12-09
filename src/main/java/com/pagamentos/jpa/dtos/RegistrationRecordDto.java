package com.pagamentos.jpa.dtos;

public record RegistrationRecordDto(String cpf,
                                    String nome,
                                    String email,
                                    String password) {
}

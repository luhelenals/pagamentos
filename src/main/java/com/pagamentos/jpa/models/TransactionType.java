package com.pagamentos.jpa.models;

public enum TransactionType {
    PAGAMENTO_SALDO,   // Pagamento feito com saldo do usuário
    PAGAMENTO_CARTAO,  // Pagamento feito com cartão de crédito
    DEPOSITO;          // Depósito na conta do usuário
}

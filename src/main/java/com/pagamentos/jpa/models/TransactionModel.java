package com.pagamentos.jpa.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_TRANSACAO")
public class TransactionModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType tipo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = true)
    private CardModel cartao;

    @ManyToOne
    @JoinColumn(name = "user_origem_id", nullable = false)
    private UserModel user;

    @OneToOne
    @JoinColumn(name = "cobranca_id", referencedColumnName = "id", nullable = true)
    private CobrancaModel cobranca;

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public TransactionType getTipo() {
        return tipo;
    }

    public void setTipo(TransactionType tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCartao(CardModel cartao) {
        this.cartao = cartao;
    }

    public CardModel getCartao() {
        return cartao;
    }

    public CobrancaModel getCobranca() {
        return cobranca;
    }

    public void setCobranca(CobrancaModel cobranca) {
        this.cobranca = cobranca;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public UserModel getUser() {
        return user;
    }
}

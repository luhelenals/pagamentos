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

    @Column(nullable = false, length = 11)
    private String cpfOrigem;

    @Column(nullable = false, length = 11)
    private String cpfDestino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType tipo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private CardModel cartao;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @OneToOne
    @JoinColumn(name = "cobranca_id", referencedColumnName = "id", nullable = false)
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

    public String getCpfOrigem() {
        return cpfOrigem;
    }

    public void setCpfOrigem(String cpfOrigem) {
        this.cpfOrigem = cpfOrigem;
    }

    public String getCpfDestino() {
        return cpfDestino;
    }

    public void setCpfDestino(String cpfDestino) {
        this.cpfDestino = cpfDestino;
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

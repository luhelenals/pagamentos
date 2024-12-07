package com.pagamentos.jpa.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TB_COBRANCA")
public class CobrancaModel  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = false)
    private BigDecimal valor;

    @Column(nullable = false, unique = false, length = 11)
    private String CPF_origem;

    @Column(nullable = false, unique = false, length = 11)
    private String CPF_destino;

    @Column(nullable = true, unique = false)
    private String descricao;

    @Column(nullable = false, unique = false)
    private StatusCobranca status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @OneToOne(mappedBy = "cobranca", cascade = CascadeType.ALL, orphanRemoval = true)
    private TransactionModel transacao;

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

    public String getCPF_origem() {
        return CPF_origem;
    }

    public void setCPF_origem(String CPF_origem) {
        this.CPF_origem = CPF_origem;
    }

    public String getCPF_destino() {
        return CPF_destino;
    }

    public void setCPF_destino(String CPF_destino) {
        this.CPF_destino = CPF_destino;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusCobranca getStatus() {
        return status;
    }

    public void setStatus(StatusCobranca status) {
        this.status = status;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public UserModel getUser() {
        return user;
    }

    public void setTransacao(TransactionModel transacao) {
        this.transacao = transacao;
    }

    public TransactionModel getTransacao() {
        return transacao;
    }
}

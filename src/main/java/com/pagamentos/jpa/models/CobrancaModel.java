package com.pagamentos.jpa.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TB_COBRANCA")
public class CobrancaModel implements Serializable {

    private static final long serialVersionUID = 1L; // Versão para controle de serialização

    @Id // Marca o campo como chave primária
    @GeneratedValue(strategy = GenerationType.AUTO) // Gera um UUID automaticamente
    private UUID id;

    @Column(nullable = false, unique = false) // Define uma coluna que não pode ser nula
    private BigDecimal valor; // O valor da cobrança

    @Column(nullable = false, unique = false, length = 11) // Define uma coluna para o CPF de origem e não pode ser nula
    private String CPF_origem; // CPF do usuário de origem

    @Column(nullable = false, unique = false, length = 11) // Define uma coluna para o CPF de destino e não pode ser nula
    private String CPF_destino; // CPF do usuário de destino

    @Column(nullable = true, unique = false) // Define uma coluna para a descrição, que pode ser nula
    private String descricao; // Descrição opcional da cobrança

    @Column(nullable = false, unique = false) // Define uma coluna para o status da cobrança e não pode ser nula
    private StatusCobranca status; // Status da cobrança (por exemplo, PENDENTE, PAGO, etc.)

    @ManyToOne // Relacionamento Many-to-One com a entidade UserModel (origem)
    @JoinColumn(name = "user_origem_id") // Define a chave estrangeira para a tabela de usuários (origem)
    private UserModel userOrigem;

    @ManyToOne // Relacionamento Many-to-One com a entidade UserModel (destino)
    @JoinColumn(name = "user_destino_id") // Define a chave estrangeira para a tabela de usuários (destino)
    private UserModel userDestino;

    @OneToOne(mappedBy = "cobranca", cascade = CascadeType.ALL, orphanRemoval = true)
    // Relacionamento One-to-One com a entidade TransactionModel (cada cobrança tem uma transação associada)
    // mappedBy indica que a propriedade 'cobranca' na classe TransactionModel é responsável pela associação
    // cascade = CascadeType.ALL faz com que as operações em Cobranca sejam propagadas para a TransactionModel
    // orphanRemoval = true significa que se a cobrança for removida, a transação associada também será removida
    private TransactionModel transacao;

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

    public void setUserOrigem(UserModel user) {
        this.userOrigem = user;
    }

    public UserModel getUserOrigem() {
        return userOrigem;
    }

    public void setTransacao(TransactionModel transacao) {
        this.transacao = transacao;
    }

    public TransactionModel getTransacao() {
        return transacao;
    }

    public void setUserDestino(UserModel userDestino) {
        this.userDestino = userDestino;
    }

    public UserModel getUserDestino() {
        return userDestino;
    }
}

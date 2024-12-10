package com.pagamentos.jpa.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TB_CARTAO")
public class CardModel implements Serializable {

    private static final long serialVersionUID = 1L; // Versão para controle de serialização

    @Id // Marca o campo como chave primária
    @GeneratedValue(strategy = GenerationType.AUTO) // Gera um UUID automaticamente
    private UUID id;

    @Column(nullable = false, length = 16) // Define uma coluna de 16 caracteres que não pode ser nula
    private String numero;

    @Column(nullable = false, length = 5) // Define uma coluna de 5 caracteres para a validade do cartão (MM/YY) e não pode ser nula
    private String validade; // Formato: MM/YY

    @Column(nullable = false, length = 3) // Define uma coluna de 3 caracteres para o CVV do cartão e não pode ser nula
    private String cvv;

    @ManyToOne // Relacionamento Many-to-One com a entidade UserModel
    @JoinColumn(name = "user_origem_id") // Define a chave estrangeira para a tabela de usuários
    private UserModel user;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Impede que o campo transações seja incluído na serialização JSON
    @OneToMany(mappedBy = "cartao", fetch = FetchType.LAZY) // Relacionamento One-to-Many com a entidade TransactionModel, carregado de forma preguiçosa (lazy loading)
    private Set<TransactionModel> transacoes = new HashSet<>(); // Um conjunto de transações relacionadas a este cartão

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}

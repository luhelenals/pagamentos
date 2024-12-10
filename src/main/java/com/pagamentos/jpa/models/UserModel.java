package com.pagamentos.jpa.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "TB_USER")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserModel implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    @Id  // Define o identificador da entidade
    @GeneratedValue(strategy = GenerationType.AUTO)  // Gera o valor automaticamente
    private UUID id;  // ID do usuário, do tipo UUID

    @Column(nullable = false)  // Coluna obrigatória no banco
    private String nome;  // Nome do usuário

    @Column(nullable = false, unique = true)  // Coluna obrigatória e única
    private String email;  // Email do usuário

    @Column(nullable = false)  // Coluna obrigatória
    private String senha;  // Senha do usuário

    @Column(nullable = false, unique = true, length = 11)  // Coluna obrigatória, única e com tamanho fixo
    private String cpf;  // CPF do usuário

    @Column(nullable = false)  // Coluna obrigatória
    private BigDecimal saldo;  // Saldo do usuário

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // Não serializa esta propriedade para leitura
    @OneToMany(mappedBy = "userOrigem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // Relacionamento com CobrancaModel
    private Set<CobrancaModel> cobrancasFeitas = new HashSet<>();  // Conjunto de cobranças feitas pelo usuário

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // Não serializa esta propriedade para leitura
    @OneToMany(mappedBy = "userDestino", fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // Relacionamento com CobrancaModel
    private Set<CobrancaModel> cobrancasRecebidas = new HashSet<>();  // Conjunto de cobranças recebidas pelo usuário

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // Não serializa esta propriedade para leitura
    @OneToMany(mappedBy = "userDestino", fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // Relacionamento com TransactionModel
    private Set<TransactionModel> transacoesRecebidas = new HashSet<>();  // Conjunto de transações recebidas

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // Não serializa esta propriedade para leitura
    @OneToMany(mappedBy = "userOrigem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // Relacionamento com TransactionModel
    private Set<TransactionModel> transacoesFeitas = new HashSet<>();  // Conjunto de transações feitas

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // Não serializa esta propriedade para leitura
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // Relacionamento com CardModel
    private Set<CardModel> cartoes = new HashSet<>();  // Conjunto de cartões do usuário

    // Construtor personalizado para inicialização
    public UserModel (String nome, String email, String senha, String cpf) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;

        // Inicializa valores padrão
        this.saldo = BigDecimal.ZERO;  // Saldo inicial
        this.cobrancasFeitas = new HashSet<>();
        this.cobrancasRecebidas = new HashSet<>();
        this.transacoesRecebidas = new HashSet<>();
        this.transacoesFeitas = new HashSet<>();
        this.cartoes = new HashSet<>();
    }

    // Método estático para validação de CPF
    public static boolean isCPF(String CPF) {
        List<String> CPFsInvalidos = Arrays.asList( "00000000000", "11111111111", "22222222222", "33333333333", "44444444444", "55555555555", "66666666666", "77777777777", "88888888888", "99999999999");
        if(CPFsInvalidos.contains(CPF)) return(false);  // CPF inválido se for uma sequência repetida

        char dig10, dig11;
        int sm, i, r, num, peso;

        // Bloco "try" para capturar exceções
        try {
            // Cálculo do 1º dígito verificador do CPF
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                num = (int)(CPF.charAt(i) - 48);  // Converte caractere para número
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48);  // Converte para o caractere

            // Cálculo do 2º dígito verificador do CPF
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);

            // Verifica se os dígitos calculados são iguais aos informados
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);  // CPF válido
            else return(false);  // CPF inválido
        } catch (InputMismatchException erro) {
            return(false);  // Exceção em caso de erro de conversão
        }
    }

    // Métodos getters e setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setCobrancasFeitas(Set<CobrancaModel> cobrancas) {
        this.cobrancasFeitas = cobrancas;
    }

    public Set<CobrancaModel> getCobrancasFeitas() {
        return cobrancasFeitas;
    }

    public void setCartoes(Set<CardModel> cartoes) {
        this.cartoes = cartoes;
    }

    public void addCartao(CardModel cartao) {
        if (this.cartoes == null) {
            this.cartoes = new HashSet<>();
        }
        this.cartoes.add(cartao);
        cartao.setUser(this);
    }

    public Set<CardModel> getCartoes() {
        return cartoes;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setTransacoesRecebidas(Set<TransactionModel> transacoes) {
        this.transacoesRecebidas = transacoes;
    }

    public Set<TransactionModel> getTransacoesRecebidas() {
        return transacoesRecebidas;
    }

    public void setTransacoesFeitas(Set<TransactionModel> transacoes) {
        this.transacoesFeitas = transacoes;
    }

    public Set<TransactionModel> getTransacoesFeitas() {
        return transacoesFeitas;
    }

    public void setCobrancasRecebidas(Set<CobrancaModel> cobrancas) {
        this.cobrancasRecebidas = cobrancas;
    }

    public Set<CobrancaModel> getCobrancasRecebidas() {
        return cobrancasRecebidas;
    }

    public void addCobrancaRecebida(CobrancaModel cobranca) {
        if (this.cobrancasRecebidas == null) {
            this.cobrancasRecebidas = new HashSet<>();
        }
        this.cobrancasRecebidas.add(cobranca);
        cobranca.setUserDestino(this);
    }

    public void addCobrancaFeita(CobrancaModel cobranca) {
        if (this.cobrancasFeitas == null) {
            this.cobrancasFeitas = new HashSet<>();
        }
        this.cobrancasFeitas.add(cobranca);
        cobranca.setUserOrigem(this);
    }

    // Implementação dos métodos de UserDetails do Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();  // Não define autoridades específicas para este usuário
    }

    @Override
    public String getPassword() {
        return senha;  // Retorna a senha do usuário
    }

    @Override
    public String getUsername() {
        return email;  // Retorna o email como o nome de usuário
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // A conta não expira
    }

    @Override
    public boolean isEnabled() {
        return true;  // A conta está habilitada
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // A conta não está bloqueada
    }
}
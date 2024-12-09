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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private BigDecimal saldo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "userOrigem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CobrancaModel> cobrancasFeitas = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "userDestino", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CobrancaModel> cobrancasRecebidas = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "userDestino", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TransactionModel> transacoesRecebidas = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "userOrigem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TransactionModel> transacoesFeitas = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CardModel> cartoes = new HashSet<>();

    public UserModel (String nome, String email, String senha, String cpf) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;

        // Valores default iniciais
        this.saldo = BigDecimal.ZERO;
        this.cobrancasFeitas = new HashSet<>();
        this.cobrancasRecebidas = new HashSet<>();
        this.transacoesRecebidas = new HashSet<>();
        this.transacoesFeitas = new HashSet<>();
        this.cartoes = new HashSet<>();
    }

    public static boolean isCPF(String CPF) {
        // considera-se erro CPF"s formados por uma sequencia de numeros iguais
        List<String> CPFsInvalidos = Arrays.asList( "00000000000", "11111111111", "22222222222", "33333333333", "44444444444", "55555555555", "66666666666", "77777777777", "88888888888", "99999999999");
        if(CPFsInvalidos.contains(CPF)) return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere "0" no inteiro 0
                // (48 eh a posicao de "0" na tabela ASCII)
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
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

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }

    // Getters e Setters
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
}
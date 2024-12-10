package com.pagamentos.jpa.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    // Define a configuração de segurança para as requisições HTTP
    @Bean
    public SecurityFilterChain securityFilterChains(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Desabilita proteção CSRF, pois estamos usando tokens JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define que não será usada sessão HTTP
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Permite o acesso público ao login
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Permite o acesso público ao registro
                        .anyRequest().permitAll()) // Permite o acesso a qualquer outra requisição (deve ser utilizado somente o acesso por tokens de autenticação futuramente)
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Adiciona o filtro customizado de segurança
                .build(); // Constrói a configuração de segurança
    }

    // Cria um bean para o AuthenticationManager, que é responsável pela autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Obtém o AuthenticationManager da configuração
    }

    // Cria um bean para o PasswordEncoder, que irá usar o BCrypt para criptografar senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Retorna uma instância do BCryptPasswordEncoder
    }
}

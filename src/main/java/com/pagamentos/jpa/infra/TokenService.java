package com.pagamentos.jpa.infra;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pagamentos.jpa.models.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}") // Lê o segredo do token configurado no arquivo application.properties ou application.yml
    private String secret;

    // Método para gerar um token JWT para o usuário fornecido
    public String generateToken(UserModel user) {
        try {
            // Define o algoritmo de criptografia (HMAC256) com o segredo
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Cria o token JWT com as informações do usuário
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);

            return token;
        } catch(JWTCreationException exception) {
            // Caso ocorra erro na criação do token, lança uma exceção
            throw new RuntimeException("Erro em gerar o token", exception);
        }
    }

    // Método para validar um token JWT e retornar o sujeito (usuário) associado
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Valida o token e retorna o sujeito (email do usuário)
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject(); // Retorna o sujeito (usuário) do token
        } catch (JWTVerificationException exception) {
            // Caso o token seja inválido, retorna uma string vazia
            return "";
        }
    }

    // Método para gerar a data de expiração do token (2 horas a partir do momento atual)
    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(2) // Adiciona 2 horas de validade ao token
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
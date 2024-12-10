package com.pagamentos.jpa.infra;

import com.pagamentos.jpa.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService; // Serviço para validação e manipulação de tokens JWT

    @Autowired
    UserRepository userRepository; // Repositório para acessar os dados do usuário no banco de dados

    public SecurityFilter(TokenService tokenService) {
        // Construtor utilizado para injeção de dependência, mas não necessário, pois o Spring já faz isso automaticamente
    }

    // Este método é chamado para cada requisição, após a filtragem da segurança
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request); // Recupera o token JWT do cabeçalho da requisição

        // Se o token não for nulo, valida e autentica o usuário
        if(token != null) {
            var login = tokenService.validateToken(token); // Valida o token e recupera o login (email) do usuário
            UserDetails userDetails = userRepository.findUserByEmail(login); // Busca os detalhes do usuário no banco de dados

            // Cria um objeto de autenticação com as informações do usuário
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
            // Define o contexto de segurança com o usuário autenticado
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continua o processamento da requisição
        filterChain.doFilter(request, response);
    }

    // Recupera o token JWT do cabeçalho "Authorization" da requisição
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        // Se não houver o cabeçalho, retorna null
        if(authHeader == null) return null;
        // Retorna o token removendo o prefixo "Bearer "
        return authHeader.replace("Bearer ", "");
    }
}

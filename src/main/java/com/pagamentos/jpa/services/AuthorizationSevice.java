package com.pagamentos.jpa.services;

import com.pagamentos.jpa.models.UserModel; // Importa a classe UserModel para representar o usuário
import com.pagamentos.jpa.repositories.UserRepository; // Importa o repositório para buscar usuários no banco de dados
import org.springframework.security.core.userdetails.UserDetails; // Importa a interface UserDetails para representar os detalhes do usuário
import org.springframework.security.core.userdetails.UserDetailsService; // Importa a interface UserDetailsService para implementar o carregamento dos detalhes do usuário
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Exceção caso o usuário não seja encontrado
import org.springframework.stereotype.Service; // Anotação para definir esta classe como um serviço do Spring

@Service // Marca a classe como um serviço para ser gerenciado pelo Spring
public class AuthorizationSevice implements UserDetailsService { // Implementa a interface UserDetailsService para carregar detalhes de um usuário

    UserRepository userRepository; // Declaração do repositório de usuários

    // Construtor que recebe o UserRepository para realizar buscas no banco
    public AuthorizationSevice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Método que busca um usuário pelo username (que pode ser email ou CPF)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user; // Declaração da variável user do tipo UserModel

        // Verifica se o username contém "@" (indicando que é um email)
        if (username.contains("@"))
            user = userRepository.findUserByEmail(username); // Busca o usuário pelo email
        else
            user = userRepository.findUserByCPF(username); // Se não for email, busca pelo CPF

        // Se o usuário não for encontrado, lança uma exceção indicando que o usuário não existe
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }

        // Retorna o usuário encontrado
        return user;
    }
}
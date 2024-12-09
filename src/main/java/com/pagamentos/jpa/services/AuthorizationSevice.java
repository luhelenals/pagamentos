package com.pagamentos.jpa.services;

import com.pagamentos.jpa.models.UserModel;
import com.pagamentos.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationSevice implements UserDetailsService {

    UserRepository userRepository;

    public AuthorizationSevice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }
        return user;
    }

}

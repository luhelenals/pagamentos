package com.pagamentos.jpa.controllers;

import com.pagamentos.jpa.dtos.AuthenticationRecordDto;
import com.pagamentos.jpa.dtos.LoginResponseDto;
import com.pagamentos.jpa.dtos.RegistrationRecordDto;
import com.pagamentos.jpa.infra.TokenService;
import com.pagamentos.jpa.models.UserModel;
import com.pagamentos.jpa.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth") // Rota base para autenticação
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private TokenService tokenService;

    // Construtor para injeção das dependências necessárias
    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    // Endpoint para login de usuários
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRecordDto data) {
        try {
            // Autentica o usuário com email e senha
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            // Gera e retorna o token JWT para o usuário autenticado
            var token = tokenService.generateToken((UserModel) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDto(token));
        } catch (Exception e) {
            // Retorna 401 em caso de erro de autenticação
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Endpoint para registro de novos usuários
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegistrationRecordDto data) {
        // Valida os dados do usuário antes de registrar (preenchimento, CPF válido, e unicidade)
        if ((data.password() == null || data.email() == null || data.nome() == null || data.cpf() == null)
                || (this.userRepository.findUserByEmail(data.email()) != null || this.userRepository.findUserByCPF(data.cpf()) != null)
                || !UserModel.isCPF(data.cpf()))
            return ResponseEntity.badRequest().build();

        // Encripta a senha e salva o novo usuário
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUser = new UserModel(data.nome(), data.email(), encryptedPassword, data.cpf());
        this.userRepository.save(newUser);

        // Retorna 200 OK após o registro bem-sucedido
        return ResponseEntity.ok().build();
    }
}
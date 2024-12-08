package com.pagamentos.jpa.controllers;

import com.pagamentos.jpa.models.TransactionModel;
import com.pagamentos.jpa.models.UserModel;
import com.pagamentos.jpa.repositories.UserRepository;
import com.pagamentos.jpa.services.UserService;
import com.pagamentos.jpa.dtos.UserRecordDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<UserModel> saveUser(@RequestBody UserRecordDto userRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userRecordDto));
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> findAllTransactions() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}

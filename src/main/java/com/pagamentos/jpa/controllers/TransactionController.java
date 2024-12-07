package com.pagamentos.jpa.controllers;

/*
* @RestController
@RequestMapping("/pagamentos/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserModel> saveUser(@RequestBody UserRecordDto userRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userRecordDto));
    }
}
*/

import com.pagamentos.jpa.dtos.TransactionRecordDto;
import com.pagamentos.jpa.models.TransactionModel;
import com.pagamentos.jpa.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionModel> saveTransaction(@RequestBody TransactionRecordDto transactionRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.saveTransaction(transactionRecordDto));
    }
}

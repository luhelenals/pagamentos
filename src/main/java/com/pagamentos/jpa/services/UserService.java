//package com.pagamentos.jpa.services;
//
//import com.pagamentos.jpa.dtos.UserRecordDto;
//import com.pagamentos.jpa.models.CardModel;
//import com.pagamentos.jpa.models.UserModel;
//import com.pagamentos.jpa.repositories.CardRepository;
//import com.pagamentos.jpa.repositories.CobrancaRepository;
//import com.pagamentos.jpa.repositories.TransactionRepository;
//import com.pagamentos.jpa.repositories.UserRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Service;
//
//import java.util.stream.Collectors;
//
//@Service
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final CobrancaRepository cobrancaRepository;
//    private final TransactionRepository transactionRepository;
//    private final CardRepository cardRepository;
//
//    public UserService(UserRepository userRepository,
//                       CobrancaRepository cobrancaRepository,
//                       TransactionRepository transactionRepository,
//                       CardRepository cardRepository) {
//        this.userRepository = userRepository;
//        this.cobrancaRepository = cobrancaRepository;
//        this.transactionRepository = transactionRepository;
//        this.cardRepository = cardRepository;
//    }
//
//    @Transactional
//    public UserModel saveUser(UserRecordDto userRecordDto) {
//        UserModel user = new UserModel();
//        user.setCpf(userRecordDto.cpf());
//        user.setNome(userRecordDto.nome());
//        user.setEmail(userRecordDto.email());
//        user.setSenha(userRecordDto.senha());
//        user.setSaldo(userRecordDto.saldo());
//
//        return userRepository.save(user);
//    }
//
//}

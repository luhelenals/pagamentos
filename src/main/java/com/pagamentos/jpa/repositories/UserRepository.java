package com.pagamentos.jpa.repositories;

import com.pagamentos.jpa.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    @Query(value = "SELECT * FROM tb_user WHERE email = :email", nativeQuery = true)
    UserModel findUserByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM tb_user WHERE cpf = :cpf", nativeQuery = true)
    UserModel findUserByCPF(@Param("cpf") String cpf);

    @Query(value = "SELECT saldo FROM tb_user WHERE cpf = :cpf", nativeQuery = true)
    BigDecimal findSaldoByCPF(@Param("cpf") String cpf);
}

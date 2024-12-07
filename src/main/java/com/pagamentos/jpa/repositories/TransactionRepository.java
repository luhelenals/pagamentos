package com.pagamentos.jpa.repositories;

import com.pagamentos.jpa.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionModel, UUID> {
}

package com.pagamentos.jpa.repositories;

import com.pagamentos.jpa.models.CardModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<CardModel, UUID> {
}

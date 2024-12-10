package com.pagamentos.jpa.repositories;

import com.pagamentos.jpa.models.CardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CardRepository extends JpaRepository<CardModel, UUID> {

    // Encontra o cartão que possui os atributos passados como parâmetro
    @Query(value = "SELECT * FROM tb_cartao WHERE numero = :numero AND cvv = :cvv" +
            "AND validade = :validade AND user_origem_id = :id", nativeQuery = true)
    CardModel findCardByAttributes(@Param("numero") String numero,
                                   @Param("cvv") String cvv,
                                   @Param("validade") String validade,
                                   @Param("id") UUID id);
}

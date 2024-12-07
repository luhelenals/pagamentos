package com.pagamentos.jpa.repositories;

import com.pagamentos.jpa.models.CobrancaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CobrancaRepository extends JpaRepository<CobrancaModel, UUID> {

    @Query(value = "SELECT * FROM tb_cobranca WHERE cpf = :cpf", nativeQuery = true)
    List<CobrancaModel> findCobrancaModelsByCPF(@Param("cpf") String cpf);
}

package br.com.teste.repository;

import br.com.teste.entity.Ata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AtaRepository extends JpaRepository<Ata, Long> {

    @Query("SELECT a FROM Ata a WHERE LOWER(a.workshop.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Ata> findByWorkshopNomeContainingIgnoreCase(@Param("nome") String nome);

    @Query("SELECT a FROM Ata a WHERE a.workshop.dataRealizacao >= :inicio AND a.workshop.dataRealizacao < :fim")
    List<Ata> findByWorkshopData(@Param("inicio") LocalDateTime inicio,
                                 @Param("fim") LocalDateTime fim);
}
package br.com.bancorob.desafiocontasapagar.repository;

import br.com.bancorob.desafiocontasapagar.domain.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findByDataVencimentoBetweenAndDescricaoContaining(LocalDate startDate, LocalDate endDate, String descricao);
    List<Conta> findByDataPagamentoBetween(LocalDate startDate, LocalDate endDate);
}
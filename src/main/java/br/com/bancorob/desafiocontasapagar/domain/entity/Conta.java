package br.com.bancorob.desafiocontasapagar.domain.entity;

import br.com.bancorob.desafiocontasapagar.domain.enums.Situacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;

    public Conta() {

    }
}
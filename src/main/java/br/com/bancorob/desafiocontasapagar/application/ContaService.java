package br.com.bancorob.desafiocontasapagar.application;

import br.com.bancorob.desafiocontasapagar.domain.entity.Conta;
import br.com.bancorob.desafiocontasapagar.domain.enums.Situacao;
import br.com.bancorob.desafiocontasapagar.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public Conta save(Conta conta) {
        return contaRepository.save(conta);
    }

    public Optional<Conta> findById(Long id) {
        return contaRepository.findById(id);
    }

    public Page<Conta> findAll(Pageable pageable) {
        return contaRepository.findAll(pageable);
    }

    public List<Conta> findByDataVencimentoAndDescricao(LocalDate startDate, LocalDate endDate, String descricao) {
        return contaRepository.findByDataVencimentoBetweenAndDescricaoContaining(startDate, endDate, descricao);
    }

    public BigDecimal getTotalPaidByPeriod(LocalDate startDate, LocalDate endDate) {
        return contaRepository.findByDataPagamentoBetween(startDate, endDate)
                .stream()
                .map(Conta::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateSituacao(Long id, Situacao situacao) {
        Optional<Conta> conta = contaRepository.findById(id);
        conta.ifPresent(c -> {
            c.setSituacao(situacao);
            contaRepository.save(c);
        });
    }
}
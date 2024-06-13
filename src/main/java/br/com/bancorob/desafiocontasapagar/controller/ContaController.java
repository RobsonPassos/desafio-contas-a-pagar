package br.com.bancorob.desafiocontasapagar.controller;

import br.com.bancorob.desafiocontasapagar.application.ContaService;
import br.com.bancorob.desafiocontasapagar.domain.entity.Conta;
import br.com.bancorob.desafiocontasapagar.domain.enums.Situacao;
import br.com.bancorob.desafiocontasapagar.dto.ContaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<Conta> create(@RequestBody ContaDTO contaDTO) {
        Conta conta = toEntity(contaDTO);
        Conta savedConta = contaService.save(conta);
        return ResponseEntity.ok(savedConta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conta> update(@PathVariable Long id, @RequestBody ContaDTO contaDTO) {
        Conta conta = toEntity(contaDTO);
        conta.setId(id);
        Conta updatedConta = contaService.save(conta);
        return ResponseEntity.ok(updatedConta);
    }

    @PatchMapping("/{id}/situacao")
    public ResponseEntity<Void> updateSituacao(@PathVariable Long id, @RequestBody Situacao situacao) {
        contaService.updateSituacao(id, situacao);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<Conta>> getAll(Pageable pageable) {
        Page<Conta> contas = contaService.findAll(pageable);
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Conta>> getByDataVencimentoAndDescricao(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String descricao) {
        List<Conta> contas = contaService.findByDataVencimentoAndDescricao(startDate, endDate, descricao);
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> getById(@PathVariable Long id) {
        Optional<Conta> conta = contaService.findById(id);
        return conta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/total-paid")
    public ResponseEntity<BigDecimal> getTotalPaidByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal totalPaid = contaService.getTotalPaidByPeriod(startDate, endDate);
        return ResponseEntity.ok(totalPaid);
    }

    private Conta toEntity(ContaDTO contaDTO) {
        Conta conta = new Conta();
        conta.setId(contaDTO.getId());
        conta.setDataVencimento(contaDTO.getDataVencimento());
        conta.setDataPagamento(contaDTO.getDataPagamento());
        conta.setValor(contaDTO.getValor());
        conta.setDescricao(contaDTO.getDescricao());
        conta.setSituacao(contaDTO.getSituacao());
        return conta;
    }
}
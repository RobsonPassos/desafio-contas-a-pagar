package br.com.bancorob.desafiocontasapagar.controller;


import br.com.bancorob.desafiocontasapagar.application.ContaService;
import br.com.bancorob.desafiocontasapagar.domain.entity.Conta;
import br.com.bancorob.desafiocontasapagar.domain.enums.Situacao;
import br.com.bancorob.desafiocontasapagar.dto.ContaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ContaControllerTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setDescricao("Conta de teste");

        when(contaService.save(any(Conta.class))).thenReturn(new Conta());

        ResponseEntity<Conta> response = contaController.create(contaDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setDescricao("Conta de teste");

        when(contaService.save(any(Conta.class))).thenReturn(new Conta());

        ResponseEntity<Conta> response = contaController.update(id, contaDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateSituacao() {
        Long id = 1L;
        Situacao situacao = Situacao.PAGO;

        ResponseEntity<Void> response = contaController.updateSituacao(id, situacao);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAll() {
        Page<Conta> contas = new PageImpl<>(new ArrayList<>());

        when(contaService.findAll(any(Pageable.class))).thenReturn(contas);

        ResponseEntity<Page<Conta>> response = contaController.getAll(Pageable.unpaged());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetByDataVencimentoAndDescricao() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();
        String descricao = "Teste";

        List<Conta> contas = new ArrayList<>();

        when(contaService.findByDataVencimentoAndDescricao(startDate, endDate, descricao)).thenReturn(contas);

        ResponseEntity<List<Conta>> response = contaController.getByDataVencimentoAndDescricao(startDate, endDate, descricao);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetById_WhenFound() {
        Long id = 1L;
        Conta conta = new Conta();

        when(contaService.findById(id)).thenReturn(Optional.of(conta));

        ResponseEntity<Conta> response = contaController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetById_WhenNotFound() {
        Long id = 1L;

        when(contaService.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Conta> response = contaController.getById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetTotalPaidByPeriod() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();
        BigDecimal totalPaid = BigDecimal.TEN;

        when(contaService.getTotalPaidByPeriod(startDate, endDate)).thenReturn(totalPaid);

        ResponseEntity<BigDecimal> response = contaController.getTotalPaidByPeriod(startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
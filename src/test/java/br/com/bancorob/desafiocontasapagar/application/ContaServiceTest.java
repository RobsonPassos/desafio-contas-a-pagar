package br.com.bancorob.desafiocontasapagar.application;

import br.com.bancorob.desafiocontasapagar.domain.entity.Conta;
import br.com.bancorob.desafiocontasapagar.domain.enums.Situacao;
import br.com.bancorob.desafiocontasapagar.repository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    public ContaServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Conta conta = new Conta();
        conta.setValor(new BigDecimal("100.00"));
        Mockito.when(contaRepository.save(conta)).thenReturn(conta);
        Conta savedConta = contaService.save(conta);
        assertEquals(conta.getValor(), savedConta.getValor());
    }

    @Test
    public void testFindById() {
        Conta conta = new Conta();
        conta.setId(1L);
        Mockito.when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        Optional<Conta> foundConta = contaService.findById(1L);
        assertEquals(conta.getId(), foundConta.get().getId());
    }

    @Test
    public void testUpdateSituacao() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setSituacao(Situacao.A_PAGAR);
        Mockito.when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        contaService.updateSituacao(1L, Situacao.PAGO);
        Mockito.verify(contaRepository, Mockito.times(1)).save(conta);
        assertEquals(Situacao.PAGO, conta.getSituacao());
    }
}
package br.com.bancorob.desafiocontasapagar.controller;

import static org.junit.jupiter.api.Assertions.*;

import br.com.bancorob.desafiocontasapagar.application.ContaService;
import br.com.bancorob.desafiocontasapagar.controller.ContaImportController;
import br.com.bancorob.desafiocontasapagar.domain.entity.Conta;
import br.com.bancorob.desafiocontasapagar.domain.enums.Situacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaImportControllerTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaImportController contaImportController;

    @Test
    void testImportCsv_Success() throws IOException {
        // Mock MultipartFile
        String content = "2024-06-15,,100.00,Conta de teste,A_PAGAR\n" +
                "2024-06-20,2024-06-18,150.00,Outra conta,PAGO";
        byte[] contentBytes = content.getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file", "contas.csv", "text/csv", contentBytes);

        // Mock Contas
        Conta conta1 = new Conta(1L, LocalDate.parse("2024-06-15"), null, new BigDecimal("100.00"), "Conta de teste", Situacao.A_PAGAR);
        Conta conta2 = new Conta(2L, LocalDate.parse("2024-06-20"), LocalDate.parse("2024-06-18"), new BigDecimal("150.00"), "Outra conta", Situacao.PAGO);
        List<Conta> contas = Arrays.asList(conta1, conta2);

        // Mock Service
        when(contaService.save(any(Conta.class))).thenReturn(null);

        // Test
        ResponseEntity<String> response = contaImportController.importCsv(multipartFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Importação concluída com sucesso.", response.getBody());
        verify(contaService, times(2)).save(any(Conta.class));
    }

    @Test
    void testImportCsv_EmptyFile() {
        MultipartFile emptyFile = new MockMultipartFile("file", "empty.csv", "text/csv", new byte[0]);

        ResponseEntity<String> response = contaImportController.importCsv(emptyFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Por favor, selecione um arquivo CSV.", response.getBody());
        verifyNoInteractions(contaService);
    }

    @Test
    void testImportCsv_WrongContentType() {
        MultipartFile wrongTypeFile = new MockMultipartFile("file", "wrong.txt", "text/plain", "data".getBytes());

        ResponseEntity<String> response = contaImportController.importCsv(wrongTypeFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Por favor, envie um arquivo CSV.", response.getBody());
        verifyNoInteractions(contaService);
    }

    @Test
    void testImportCsv_InvalidDataFormat() throws IOException {
        // Mock MultipartFile
        String content = "2024-06-15,,100.00,Conta de teste"; // Incomplete data
        byte[] contentBytes = content.getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file", "invalid.csv", "text/csv", contentBytes);

        ResponseEntity<String> response = contaImportController.importCsv(multipartFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Formato de linha inválido: 2024-06-15,,100.00,Conta de teste", response.getBody());
        verifyNoInteractions(contaService);
    }

    @Test
    void testImportCsv_InternalError() throws IOException {
        // Mock MultipartFile
        ByteArrayInputStream inputStream = mock(ByteArrayInputStream.class);
        MultipartFile multipartFile = new MockMultipartFile("file", "contas.csv", "text/csv", inputStream);

        ResponseEntity<String> response = contaImportController.importCsv(multipartFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Por favor, selecione um arquivo CSV.", response.getBody());
        verifyNoInteractions(contaService);
    }
}

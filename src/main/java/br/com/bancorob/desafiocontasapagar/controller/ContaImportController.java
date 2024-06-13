package br.com.bancorob.desafiocontasapagar.controller;

import br.com.bancorob.desafiocontasapagar.application.ContaService;
import br.com.bancorob.desafiocontasapagar.domain.entity.Conta;
import br.com.bancorob.desafiocontasapagar.domain.enums.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/contas/import")
public class ContaImportController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Por favor, selecione um arquivo CSV.");
        }

        if (!file.getContentType().equals("text/csv")) {
            return ResponseEntity.badRequest().body("Por favor, envie um arquivo CSV.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<Conta> contas = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 5) {
                    return ResponseEntity.badRequest().body("Formato de linha inválido: " + line);
                }
                Conta conta = new Conta();
                conta.setDataVencimento(LocalDate.parse(data[0]));
                conta.setDataPagamento(data[1].isEmpty() ? null : LocalDate.parse(data[1]));
                conta.setValor(new BigDecimal(data[2]));
                conta.setDescricao(data[3]);
                conta.setSituacao(Situacao.valueOf(data[4].toUpperCase()));
                contas.add(conta);
            }
            contas.forEach(contaService::save);
            return ResponseEntity.ok("Importação concluída com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao importar o arquivo CSV.");
        }
    }
}
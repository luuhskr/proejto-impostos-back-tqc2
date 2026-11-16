
package br.com.controle.controller;

import br.com.controle.model.NotaFiscal;
import br.com.controle.service.NotaFiscalService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notas-fiscais")
public class NotaFiscalController {

    private final NotaFiscalService service;

    public NotaFiscalController(NotaFiscalService service) {
        this.service = service;
    }

    // Listar todas as notas
    @GetMapping
    public List<NotaFiscal> listarTodas() {
        return service.listarTodas();
    }

    // Cadastrar uma nota
    @PostMapping
    public ResponseEntity<NotaFiscal> cadastrar(
            @Valid @RequestBody NotaFiscal notaFiscal) {

        NotaFiscal notaSalva = service.salvar(notaFiscal);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notaSalva);
    }
}


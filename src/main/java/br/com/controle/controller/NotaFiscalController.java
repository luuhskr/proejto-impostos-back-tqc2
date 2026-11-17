
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

    // Listar todas as compras
    @GetMapping
    public List<NotaFiscal> listarTodas() {
        return service.listarTodas();
    }

    // Buscar uma compra pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<NotaFiscal> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cadastrar uma compra
    @PostMapping
    public ResponseEntity<NotaFiscal> cadastrar(
            @Valid @RequestBody NotaFiscal notaFiscal) {

        NotaFiscal notaSalva = service.salvar(notaFiscal);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notaSalva);
    }

    // Editar uma compra
    @PutMapping("/{id}")
    public ResponseEntity<NotaFiscal> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody NotaFiscal dados) {

        return service.buscarPorId(id).map(compra -> {

            compra.setNumero(dados.getNumero());
            compra.setDataEmissao(dados.getDataEmissao());
            compra.setValorTotal(dados.getValorTotal());
            compra.setStatus(dados.getStatus());
            compra.setTributosFederais(dados.getTributosFederais());
            compra.setTributosEstaduais(dados.getTributosEstaduais());
            compra.setTributosMunicipais(dados.getTributosMunicipais());

            NotaFiscal atualizada = service.salvar(compra);

            return ResponseEntity.ok(atualizada);

        }).orElse(ResponseEntity.notFound().build());
    }

    // Excluir uma compra
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}



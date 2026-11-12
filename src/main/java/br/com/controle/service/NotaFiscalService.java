package br.com.controle.service;

import br.com.controle.model.NotaFiscal;
import br.com.controle.repository.NotaFiscalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotaFiscalService {

    private final NotaFiscalRepository repository;

    public NotaFiscalService(NotaFiscalRepository repository) {
        this.repository = repository;
    }

    public List<NotaFiscal> listarTodas() {
        return repository.findAll();
    }

    public Optional<NotaFiscal> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public NotaFiscal salvar(NotaFiscal notaFiscal) {
        return repository.save(notaFiscal);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

}
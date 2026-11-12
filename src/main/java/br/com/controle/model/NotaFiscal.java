package br.com.controle.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "notas_fiscais")
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    private LocalDate dataEmissao;

    private BigDecimal valorTotal;

    private String status;

    // Construtor vazio
    public NotaFiscal() {
    }

    // Construtor com parâmetros
    public NotaFiscal(String numero, LocalDate dataEmissao,
                      BigDecimal valorTotal, String status) {
        this.numero = numero;
        this.dataEmissao = dataEmissao;
        this.valorTotal = valorTotal;
        this.status = status;
    }


    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

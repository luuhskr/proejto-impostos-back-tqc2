package br.com.controle.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "notas_fiscais")
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O número da nota fiscal é obrigatório.")
    private String numero;

    @NotNull(message = "A data de emissão é obrigatória.")
    private LocalDate dataEmissao;

    @NotNull(message = "O valor total é obrigatório.")
    @DecimalMin(value = "0.0", message = "O valor total não pode ser negativo.")
    private BigDecimal valorTotal;

    @NotBlank(message = "O status da nota fiscal é obrigatório.")
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

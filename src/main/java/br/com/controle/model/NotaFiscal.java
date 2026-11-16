
package br.com.controle.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "notas_fiscais")
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A identificação da compra é obrigatória.")
    private String numero;

    @NotNull(message = "A data da compra é obrigatória.")
    private LocalDate dataEmissao;

    @NotNull(message = "O valor total é obrigatório.")
    @DecimalMin(value = "0.0", message = "O valor não pode ser negativo.")
    private BigDecimal valorTotal;

    @NotBlank(message = "O status é obrigatório.")
    private String status;

    // Valores aproximados dos tributos
    @NotNull(message = "Informe os tributos federais.")
    @DecimalMin(value = "0.0", message = "O valor não pode ser negativo.")
    private BigDecimal tributosFederais = BigDecimal.ZERO;

    @NotNull(message = "Informe os tributos estaduais.")
    @DecimalMin(value = "0.0", message = "O valor não pode ser negativo.")
    private BigDecimal tributosEstaduais = BigDecimal.ZERO;

    @NotNull(message = "Informe os tributos municipais.")
    @DecimalMin(value = "0.0", message = "O valor não pode ser negativo.")
    private BigDecimal tributosMunicipais = BigDecimal.ZERO;

    // Construtor vazio
    public NotaFiscal() {
    }

    // Construtor original
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

    public BigDecimal getTributosFederais() {
        return tributosFederais;
    }

    public void setTributosFederais(BigDecimal tributosFederais) {
        this.tributosFederais = tributosFederais;
    }

    public BigDecimal getTributosEstaduais() {
        return tributosEstaduais;
    }

    public void setTributosEstaduais(BigDecimal tributosEstaduais) {
        this.tributosEstaduais = tributosEstaduais;
    }

    public BigDecimal getTributosMunicipais() {
        return tributosMunicipais;
    }

    public void setTributosMunicipais(BigDecimal tributosMunicipais) {
        this.tributosMunicipais = tributosMunicipais;
    }

    // Calcula a soma dos tributos
    public BigDecimal getTotalTributos() {
        return tributosFederais
                .add(tributosEstaduais)
                .add(tributosMunicipais);
    }

    // Calcula o percentual dos tributos sobre o valor da compra
    public BigDecimal getPercentualTributos() {

        if (valorTotal == null || valorTotal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return getTotalTributos()
                .multiply(new BigDecimal("100"))
                .divide(valorTotal, 2, RoundingMode.HALF_UP);
    }
}

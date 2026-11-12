
package controletributos;

import br.com.controle.model.NotaFiscal;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ControletributosApplicationTests {

	@Test
	void deveAceitarNotaFiscalValida() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			NotaFiscal nota = new NotaFiscal(
					"123",
					LocalDate.of(2026, 11, 12),
					new BigDecimal("150.00"),
					"PENDENTE"
			);

			Set<ConstraintViolation<NotaFiscal>> erros = validator.validate(nota);

			assertTrue(erros.isEmpty());

			assertEquals("123", nota.getNumero());
			assertEquals(new BigDecimal("150.00"), nota.getValorTotal());
			assertEquals("PENDENTE", nota.getStatus());
		}
	}

	@Test
	void deveRejeitarNotaFiscalInvalida() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			NotaFiscal nota = new NotaFiscal(
					" ",
					null,
					new BigDecimal("-50.00"),
					""
			);

			Set<ConstraintViolation<NotaFiscal>> erros = validator.validate(nota);

			assertEquals(4, erros.size());
		}
	}
}


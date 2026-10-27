package br.edu.ifsuldeminas.clinicamedica.servico;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SegurancaTest {
    @Test void bcryptUsaSaltERejeitaSenhaErrada() {
        String primeiro = Seguranca.hashSenha("minha-senha-123");
        String segundo = Seguranca.hashSenha("minha-senha-123");
        assertEquals(60, primeiro.length());
        assertNotEquals(primeiro, segundo);
        assertTrue(Seguranca.verificarSenha("minha-senha-123", primeiro));
        assertFalse(Seguranca.verificarSenha("senha-errada", primeiro));
        assertFalse(Seguranca.verificarSenha("qualquer", "hash-invalido"));
        assertFalse(Seguranca.verificarSenha(null, primeiro));
    }

    @Test void validaConfirmacaoELimiteEmBytesParaNaoTruncarBCrypt() {
        assertThrows(IllegalArgumentException.class, () -> Seguranca.validarSenha("curta", "curta"));
        assertThrows(IllegalArgumentException.class, () -> Seguranca.validarSenha("senha1234", "outra1234"));
        String senha = "á".repeat(37);
        assertThrows(IllegalArgumentException.class, () -> Seguranca.validarSenha(senha, senha));
        assertDoesNotThrow(() -> Seguranca.validarSenha("á".repeat(36), "á".repeat(36)));
        String hash = Seguranca.hashSenha("a".repeat(72));
        assertFalse(Seguranca.verificarSenha("a".repeat(72) + "x", hash));
    }

    @Test void tokensCsrfSaoAleatoriosEUnicos() {
        Set<String> tokens = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String token = Seguranca.gerarToken();
            assertTrue(token.matches("[A-Za-z0-9_-]{43}"));
            assertTrue(tokens.add(token));
        }
    }

    @Test void normalizaEmailERejeitaMultiplosDestinatarios() {
        assertEquals("pessoa@example.com", Seguranca.normalizarEmail(" Pessoa@Example.com "));
        assertThrows(IllegalArgumentException.class, () -> Seguranca.normalizarEmail("a@b.com,c@d.com"));
        assertThrows(IllegalArgumentException.class, () -> Seguranca.normalizarEmail("a@b.com\r\nBcc:x@y.com"));
        assertThrows(IllegalArgumentException.class, () -> Seguranca.normalizarEmail(null));
    }
}

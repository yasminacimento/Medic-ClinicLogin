package br.edu.ifsuldeminas.clinicamedica.servico;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Locale;
import org.mindrot.jbcrypt.BCrypt;

public final class Seguranca {
    private static final SecureRandom RANDOM = new SecureRandom();

    private Seguranca() { }

    public static String gerarToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static void validarSenha(String senha, String confirmacao) {
        if (senha == null || senha.length() < 8
                || senha.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres e no máximo 72 bytes UTF-8.");
        }
        if (!senha.equals(confirmacao)) {
            throw new IllegalArgumentException("A confirmação da senha não confere.");
        }
    }

    public static String normalizarEmail(String email) {
        String valor = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        if (valor.length() > 255 || !valor.matches("[^\\s@<>;,]+@[^\\s@<>;,]+\\.[^\\s@<>;,]+")) {
            throw new IllegalArgumentException("Informe um endereço de e-mail válido.");
        }
        return valor;
    }

    public static String hashSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

    public static boolean verificarSenha(String senha, String hash) {
        if (senha == null || senha.getBytes(StandardCharsets.UTF_8).length > 72 || hash == null) {
            return false;
        }
        try { return BCrypt.checkpw(senha, hash); }
        catch (IllegalArgumentException ex) { return false; }
    }
}

package br.com.washii.infra.security;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtils {

    // Transforma senha pura em Hash
    public static String hashSenha(String senhaPura) {
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt(12)); 
        // O número 12 é o "cost factor" (nível de complexidade)
    }

    // Verifica se a senha digitada bate com o hash do banco
    public static boolean verificarSenha(String senhaPura, String senhaHasheada) {
        try {
            return BCrypt.checkpw(senhaPura, senhaHasheada);
        } catch (Exception e) {
            return false;
        }
    }
}
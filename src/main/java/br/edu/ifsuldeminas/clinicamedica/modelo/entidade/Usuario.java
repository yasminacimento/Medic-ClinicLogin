package br.edu.ifsuldeminas.clinicamedica.modelo.entidade;

public class Usuario {
    private final int id;
    private final String nomeUsuario;
    private final String senhaHash;
    private final String email;

    public Usuario(int id, String nomeUsuario, String senhaHash, String email) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.senhaHash = senhaHash;
        this.email = email;
    }

    public int getId() { return id; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getSenhaHash() { return senhaHash; }
    public String getEmail() { return email; }
}

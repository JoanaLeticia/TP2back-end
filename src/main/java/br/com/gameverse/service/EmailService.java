package br.com.gameverse.service;

public interface EmailService {
    void enviarEmail(String emailDestinatario, String assunto, String mensagem);
}

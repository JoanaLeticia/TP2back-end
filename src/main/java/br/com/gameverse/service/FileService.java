package br.com.gameverse.service;

import java.io.File;
import java.io.IOException;

public interface FileService {
    void salvar(Long id, String nomeImagem, byte[] imagem) throws IOException;

    File download(String nomeImagem);
}

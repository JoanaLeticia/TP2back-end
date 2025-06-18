package br.com.gameverse.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import br.com.gameverse.model.Produto;
import br.com.gameverse.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProdutoFileServiceImpl implements FileService {
    private final String PATH_UPLOAD = System.getProperty("user.home")
        + File.separator + "quarkus"
        + File.separator + "images"
        + File.separator + "produto" + File.separator;

    @Inject
    ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public void salvar(Long id, String nomeImagem, byte[] imagem) throws IOException {
        Produto produto = produtoRepository.findById(id);

        try {
            // Verifica se o diretório de upload existe
            File diretorio = new File(PATH_UPLOAD);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            // Se já existe uma imagem, deleta a antiga
            if (produto.getNomeImagem() != null && !produto.getNomeImagem().isEmpty()) {
                File arquivoAntigo = new File(PATH_UPLOAD + produto.getNomeImagem());
                if (arquivoAntigo.exists()) {
                    arquivoAntigo.delete();
                }
            }

            // Salva a nova imagem
            String novoNome = gerarNomeUnico(nomeImagem);
            Path destino = Paths.get(PATH_UPLOAD + novoNome);
            Files.write(destino, imagem);

            produto.setNomeImagem(novoNome);
            produtoRepository.persist(produto);

        } catch (IOException e) {
            throw new IOException("Erro ao salvar imagem do produto: " + e.getMessage(), e);
        }
    }

    private String gerarNomeUnico(String nomeOriginal) {
        String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        return UUID.randomUUID().toString() + extensao;
    }

    @Override
    public File download(String nomeArquivo) {
        File file = new File(PATH_UPLOAD + nomeArquivo);
        if (!file.exists()) {
            // Caso a imagem não exista no diretório de upload, tenta carregar do resources
            try (InputStream in = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("images/" + nomeArquivo)) {
                
                if (in != null) {
                    // Copia do resources para o diretório de upload
                    Files.copy(in, Paths.get(PATH_UPLOAD + nomeArquivo), StandardCopyOption.REPLACE_EXISTING);
                    return new File(PATH_UPLOAD + nomeArquivo);
                }
            } catch (IOException e) {
                // Se não encontrar em nenhum lugar, retorna null ou uma imagem padrão
                return null;
            }
        }
        return file;
    }
}
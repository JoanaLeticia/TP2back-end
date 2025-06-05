package br.com.gameverse.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
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
    private final String PATH_USER = System.getProperty("user.home")
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
            // Exclui a imagem antiga (se existir)
            String imagemAntiga = produto.getNomeImagem();
            if (imagemAntiga != null && !imagemAntiga.isEmpty()) {
                File arquivoAntigo = new File(PATH_USER + imagemAntiga);
                if (arquivoAntigo.exists()) {
                    boolean deleted = arquivoAntigo.delete();
                    if (!deleted) {
                        throw new IOException("Erro ao excluir a imagem antiga.");
                    }
                }
            }

            String novoNomeImagem = salvarImagem(imagem, nomeImagem);
            produto.setNomeImagem(novoNomeImagem);

        } catch (IOException e) {
            throw e;
        }
    }

    private String salvarImagem(byte[] imagem, String nomeImagem) throws IOException {

        String mimeType = Files.probeContentType(new File(nomeImagem).toPath());
        List<String> listMimeType = Arrays.asList("image/jpg", "image/jpeg", "image/png", "image/gif");
        if (!listMimeType.contains(mimeType)) {
            throw new IOException("Tipo de imagem não suportada.");
        }

        if (imagem.length > (1024 * 1024 * 10))
            throw new IOException("Arquivo muito grande.");

        File diretorio = new File(PATH_USER);
        if (!diretorio.exists())
            diretorio.mkdirs();

        String nomeArquivo = UUID.randomUUID()
                + "." + mimeType.substring(mimeType.lastIndexOf("/") + 1);

        String path = PATH_USER + nomeArquivo;

        File file = new File(path);
        // alunos (melhorar :)
        if (file.exists())
            throw new IOException("O nome gerado da imagem está repedido.");

        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(imagem);
        fos.flush();
        fos.close();

        return nomeArquivo;

    }

    @Override
    public File download(String nomeArquivo) {
        File file = new File(PATH_USER + nomeArquivo);
        return file;
    }

    public static void main(String[] args) {
        ProdutoFileServiceImpl f = new ProdutoFileServiceImpl();
        System.out.println(f.download("thumbnail_jogo.jpg").getName());
    }
}

package br.com.gameverse.service;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import br.com.gameverse.model.Produto;
import br.com.gameverse.repository.ProdutoRepository;

@ApplicationScoped
public class InitialDataService {

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    FileService fileService;

    // Caminho relativo dentro de resources
    private static final String IMAGES_DIR = "images/";
    
    // Caminho absoluto para salvar as imagens processadas
    private final String PATH_UPLOAD = System.getProperty("user.home")
        + File.separator + "quarkus"
        + File.separator + "images"
        + File.separator + "produto" + File.separator;

    void onStart(@Observes StartupEvent ev) {
        try {
            assignImagesToProducts();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar imagens iniciais", e);
        }
    }

    @Transactional
    public void assignImagesToProducts() throws IOException {
        List<Produto> produtos = produtoRepository.listAll();
        
        // Criar diretório de upload se não existir
        Files.createDirectories(Paths.get(PATH_UPLOAD));
        
        for (Produto produto : produtos) {
            if (produto.getNomeImagem() == null || produto.getNomeImagem().isEmpty()) {
                String imageName = getImageNameForProduct(produto.getNome());
                
                // Carregar a imagem do resources
                try (InputStream in = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(IMAGES_DIR + imageName)) {
                    
                    if (in != null) {
                        // Salvar no diretório de upload
                        Path targetPath = Paths.get(PATH_UPLOAD + imageName);
                        Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        
                        // Associar ao produto no banco
                        produto.setNomeImagem(imageName);
                        produtoRepository.persist(produto);
                    }
                }
            }
        }
    }

    private String getImageNameForProduct(String productName) {
        // Implemente sua lógica de mapeamento de nomes
        return productName.toLowerCase()
            .replace(" ", "-")
            .replace(":", "")
            .replace("'", "") + ".jpg";
    }
}
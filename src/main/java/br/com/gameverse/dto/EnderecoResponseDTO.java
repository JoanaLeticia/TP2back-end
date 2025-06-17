package br.com.gameverse.dto;

import br.com.gameverse.model.Endereco;

public record EnderecoResponseDTO(
        Long id,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cep,
        MunicipioResponseDTO municipio,
        Long clienteId) {
    public static EnderecoResponseDTO valueOf(Endereco endereco) {
        return new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCep(),
                MunicipioResponseDTO.valueOf(endereco.getMunicipio()),
                endereco.getCliente() != null ? endereco.getCliente().getId() : null);
    }
}

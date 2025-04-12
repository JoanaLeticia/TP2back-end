package br.unitins.tp2.dto;

import br.unitins.tp2.model.Endereco;

public record EnderecoResponseDTO(
        Long id,
        String logradouro,
        String numero,
        String complemento,
        String cep,
        MunicipioResponseDTO municipio) {
    public static EnderecoResponseDTO valueOf(Endereco endereco) {
        return new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getCep(),
                MunicipioResponseDTO.valueOf(endereco.getMunicipio()));
    }
}

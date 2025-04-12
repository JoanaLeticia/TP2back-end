package br.unitins.tp2.dto;

import java.util.Date;
import java.util.List;

import br.unitins.tp2.model.Cliente;

public record ClienteResponseDTO(
    Long id,
    String nome,
    String cpf,
    String login,
    Date dataNascimento,
    List<TelefoneResponseDTO> listaTelefone,
    List<EnderecoResponseDTO> listaEndereco
) { 
    public static ClienteResponseDTO valueOf(Cliente cliente){
        if (cliente == null) {
            return new ClienteResponseDTO(null, null, null, null, null, null, null);
        }

        List<TelefoneResponseDTO> listaTelefones = cliente.getTelefones()
                                                    .stream()
                                                    .map(TelefoneResponseDTO::valueOf)
                                                    .toList();

        List<EnderecoResponseDTO> listaEndereco = cliente.getEndereco()
                                                    .stream()
                                                    .map(EnderecoResponseDTO::valueOf)
                                                    .toList();
        return new ClienteResponseDTO(
            cliente.getId(), 
            cliente.getNome(),
            cliente.getCpf(),
            cliente.getEmail(),
            cliente.getDataNascimento(),
            listaTelefones,
            listaEndereco
        );
    }
}

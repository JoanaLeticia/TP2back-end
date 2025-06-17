package br.com.gameverse.model.converterjpa;

import br.com.gameverse.model.MetodoPagamento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MetodoPagamentoConverter implements AttributeConverter<MetodoPagamento, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MetodoPagamento metodoPagamento) {
        return metodoPagamento == null ? null : metodoPagamento.getId();

    }

    @Override
    public MetodoPagamento convertToEntityAttribute(Integer id) {
        return MetodoPagamento.valueOf(id);
    }
}

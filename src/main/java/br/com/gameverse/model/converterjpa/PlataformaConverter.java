package br.com.gameverse.model.converterjpa;

import br.com.gameverse.model.Plataforma;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PlataformaConverter implements AttributeConverter<Plataforma, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Plataforma plataforma) {
        return plataforma == null ? null : plataforma.getId();

    }

    @Override
    public Plataforma convertToEntityAttribute(Integer id) {
        return Plataforma.valueOf(id);
    }
}

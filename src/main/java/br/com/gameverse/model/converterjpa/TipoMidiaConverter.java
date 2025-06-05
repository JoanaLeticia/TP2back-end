package br.com.gameverse.model.converterjpa;

import br.com.gameverse.model.TipoMidia;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoMidiaConverter implements AttributeConverter<TipoMidia, Integer>  {
    @Override
    public Integer convertToDatabaseColumn(TipoMidia tipoMidia) {
        return tipoMidia == null ? null : tipoMidia.getId();

    }

    @Override
    public TipoMidia convertToEntityAttribute(Integer id) {
        return TipoMidia.valueOf(id);
    }
}

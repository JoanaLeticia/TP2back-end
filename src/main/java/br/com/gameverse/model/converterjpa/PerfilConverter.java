package br.com.gameverse.model.converterjpa;

import br.com.gameverse.model.Perfil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PerfilConverter implements AttributeConverter<Perfil, Integer> {
    
    @Override
    public Integer convertToDatabaseColumn(Perfil perfil) {
        return perfil == null ? null : perfil.getId();

    }

    @Override
    public Perfil convertToEntityAttribute(Integer id) {
        return Perfil.valueOf(id);
    }
}

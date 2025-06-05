package br.com.gameverse.model.converterjpa;

import br.com.gameverse.model.ClassificacaoIndicativa;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ClassificacaoIndicativaConverter implements AttributeConverter<ClassificacaoIndicativa, Integer>  {
    @Override
    public Integer convertToDatabaseColumn(ClassificacaoIndicativa classificacao) {
        return classificacao == null ? null : classificacao.getId();

    }

    @Override
    public ClassificacaoIndicativa convertToEntityAttribute(Integer id) {
        return ClassificacaoIndicativa.valueOf(id);
    }
}

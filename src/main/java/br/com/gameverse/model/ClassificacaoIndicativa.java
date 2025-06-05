package br.com.gameverse.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ClassificacaoIndicativa {
    LIVRE(1, "L"), 
    DEZ_ANOS(2, "10"),
    DOZE_ANOS(3, "12"),
    QUATORZE_ANOS(4, "14"),
    DEZESSEIS_ANOS(5, "16"),
    DEZOITO_ANOS(6, "18");

    private final int ID;
    private final String NOME;

    ClassificacaoIndicativa(int id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public int getId() {
        return ID;
    }

    public String getNome() {
        return NOME;
    }

     public static ClassificacaoIndicativa valueOf(int id) {
        for (ClassificacaoIndicativa c : ClassificacaoIndicativa.values()) {
            if (c.getId() == id)
                return c;
        }
        return null;
     }
}

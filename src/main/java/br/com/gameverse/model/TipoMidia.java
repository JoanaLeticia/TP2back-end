package br.com.gameverse.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoMidia {
    FISICA(1, "Física"), 
    DIGITAL(2, "Digital");

    private final int ID;
    private final String NOME;

    TipoMidia(int id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public int getId() {
        return ID;
    }

    public String getNome() {
        return NOME;
    }

     public static TipoMidia valueOf(int id) {
        for (TipoMidia t : TipoMidia.values()) {
            if (t.getId() == id)
                return t;
        }
        return null;
     }
}

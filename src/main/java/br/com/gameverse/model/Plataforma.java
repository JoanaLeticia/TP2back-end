package br.com.gameverse.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Plataforma {
    PS4(1, "PS4"), 
    PS5(2, "PS5"),
    XBOXONE(3, "Xbox One"),
    XBOXSERIES(4, "Xbox Series"),
    NINTENDO(5, "Nintendo");

    private final int ID;
    private final String NOME;

    Plataforma(int id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public int getId() {
        return ID;
    }

    public String getNome() {
        return NOME;
    }

     public static Plataforma valueOf(int id) {
        for (Plataforma p : Plataforma.values()) {
            if (p.getId() == id)
                return p;
        }
        return null;
     }
}

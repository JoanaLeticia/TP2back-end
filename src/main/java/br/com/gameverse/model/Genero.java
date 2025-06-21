package br.com.gameverse.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Genero {
    ACAO(1, "Ação"), 
    AVENTURA(2, "Aventura"),
    RPG(3, "RPG"),
    TIRO(4, "Tiro"),
    ESPORTES(5, "Esportes"),
    CORRIDA(6, "Corrida"),
    LUTA(7, "Luta"),
    TERROR(8, "Terror");


    private final int ID;
    private final String NOME;

    Genero(int id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public int getId() {
        return ID;
    }

    public String getNome() {
        return NOME;
    }

     public static Genero valueOf(int id) {
        for (Genero g : Genero.values()) {
            if (g.getId() == id)
                return g;
        }
        return null;
     }

     public static Genero fromNome(String nome) {
        for (Genero genero : Genero.values()) {
            if (genero.getNome().equalsIgnoreCase(nome)) {
                return genero;
            }
        }
        return null;
    }
}

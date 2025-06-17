package br.com.gameverse.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusPagamento {
    PENDENTE(1, "Pendente"), 
    APROVADO(2, "Aprovado"),
    RECUSADO(3, "Recusado"),
    ESTORNADO(4, "Estornado");

    private final int ID;
    private final String NOME;

    StatusPagamento(int id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public int getId() {
        return ID;
    }

    public String getNome() {
        return NOME;
    }

     public static StatusPagamento valueOf(int id) {
        for (StatusPagamento s : StatusPagamento.values()) {
            if (s.getId() == id)
                return s;
        }
        return null;
     }
}

package br.com.gameverse.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusPedido {
    AGUARDANDO(1, "Aguardando"), 
    EM_TRANSITO(2, "Em trânsito"),
    ENTREGUE(3, "Entregue"),
    CANCELADO(4, "Cancelado");

    private final int ID;
    private final String NOME;

    StatusPedido(int id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public int getId() {
        return ID;
    }

    public String getNome() {
        return NOME;
    }

     public static StatusPedido valueOf(int id) {
        for (StatusPedido s : StatusPedido.values()) {
            if (s.getId() == id)
                return s;
        }
        return null;
     }
}

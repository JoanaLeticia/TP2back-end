package br.com.gameverse.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MetodoPagamento {
    CARTAO_CREDITO(1, "Cartão de Crédito"), 
    CARTAO_DEBITO(2, "Cartão de Débito"),
    PIX(3, "Pix"),
    BOLETO(4, "Boleto");

    private final int ID;
    private final String NOME;

    MetodoPagamento(int id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public int getId() {
        return ID;
    }

    public String getNome() {
        return NOME;
    }

     public static MetodoPagamento valueOf(int id) {
        for (MetodoPagamento m : MetodoPagamento.values()) {
            if (m.getId() == id)
                return m;
        }
        return null;
     }
}

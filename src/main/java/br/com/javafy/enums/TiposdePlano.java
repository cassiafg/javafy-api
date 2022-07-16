package br.com.javafy.enums;

import java.util.Arrays;

public enum TiposdePlano {
    PREMIUM(1),
    FREE(0);

    private Integer tipo;
    TiposdePlano(Integer i) {
    }

    public Integer getTipo() {
        return this.tipo;
    }

    public static TiposdePlano ofTipo(Integer tipo){
        return Arrays.stream(TiposdePlano.values())
                .filter(tp -> tp.getTipo().equals(tipo))
                .findFirst()
                .get();
    }
}

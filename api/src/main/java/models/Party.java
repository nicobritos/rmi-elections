package models;

import com.sun.istack.internal.NotNull;

public enum Party {
    // NO CAMBIAR EL ORDEN (ORDEN ALFABETICO)

    BUFFALO,
    JACKALOPE,
    LEOPARD,
    LYNX,
    OWL,
    TIGER,
    TURTLE;

    public static Party from(@NotNull String name){
        return valueOf(name.toUpperCase());
    }

}

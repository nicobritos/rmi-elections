package models;

import com.sun.istack.internal.NotNull;

public enum Party {
    TIGER,
    LEOPARD,
    LYNX,
    TURTLE,
    OWL,
    JACKALOPE,
    BUFFALO;

    public static Party from(@NotNull String name){
        return valueOf(name.toUpperCase());
    }
}

package models;

public enum Party {
    // NO CAMBIAR EL ORDEN (ORDEN ALFABETICO)

    BUFFALO,
    JACKALOPE,
    LEOPARD,
    LYNX,
    OWL,
    TIGER,
    TURTLE;

    public static Party from(String name){
        return valueOf(name.toUpperCase());
    }

}

package models;

import com.sun.istack.internal.NotNull;

public enum Province {
    JUNGLE,
    SAVANNAH,
    TUNDRA;

    public static Province from(@NotNull String name){
         return valueOf(name.toUpperCase());
    }
}

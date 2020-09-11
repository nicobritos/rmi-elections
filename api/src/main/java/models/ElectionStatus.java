package models;

import java.io.Serializable;

public enum ElectionStatus implements Serializable {
    UNINITIALIZED, OPEN, FINISHED;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        switch (this) {
            case UNINITIALIZED: return "The elections have not started";
            case OPEN: return "The elections are open";
            default: return "The elections are closed";
        }
    }
}

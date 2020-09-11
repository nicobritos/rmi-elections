package models;

import java.io.Serializable;

public class Table implements Serializable {
    private int number;

    public Table(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

package com.example.lifeconnect.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Data implements Serializable {

    private Date date;
    private ArrayList<Espace> espaces;

    public Data(Date date, ArrayList<Espace> espaces) {
        this.date = date;
        this.espaces = espaces;
    }

    public Data() {}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Espace> getEspaces() {
        return espaces;
    }

    public void setEspaces(ArrayList<Espace> espaces) {
        this.espaces = espaces;
    }

}

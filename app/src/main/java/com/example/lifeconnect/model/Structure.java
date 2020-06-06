package com.example.lifeconnect.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Structure implements Serializable {

    private ArrayList<Espace> espaces;
    private int user;

    public Structure(int user) {
        this.user = user;
    }

    public ArrayList<Espace> getEspaces() {
        return espaces;
    }

    public void setEspaces(ArrayList<Espace> espaces) {
        this.espaces = espaces;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Structure{" +
                "espaces=" + espaces +
                ", user=" + user +
                '}';
    }
}

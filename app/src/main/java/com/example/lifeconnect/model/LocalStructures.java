package com.example.lifeconnect.model;

import java.util.ArrayList;

public class LocalStructures {

    private ArrayList<Structure> structures;

    public LocalStructures(ArrayList<Structure> structures) {
        this.structures = structures;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public void setStructures(ArrayList<Structure> structures) {
        this.structures = structures;
    }
}

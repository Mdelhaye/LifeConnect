package com.example.lifeconnect.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Espace implements Serializable {

    private int id;
    private String name;
    private ArrayList<Indicateur> listIndicateur;
    private ArrayList<Integer> listDayOn;

    public Espace() {
    }

    public Espace(int id, String name, ArrayList<Indicateur> listIndicateur, ArrayList<Integer> listDayOn) {
        this.id = id;
        this.name = name;
        this.listIndicateur = listIndicateur;
        this.listDayOn = listDayOn;
    }

    public Espace(String toString) {
        ArrayList<String> arrayList = new ArrayList<>();
        while (toString.contains("=")) {
            int indexStart = toString.indexOf("='") + 2;
            int indexEnd;
            if (toString.contains("',")) {
                indexEnd = toString.indexOf("',");
            }
            else {
                indexEnd = toString.indexOf("'}");
            }
            String word = toString.substring(indexStart, indexEnd);
            arrayList.add(word);
            toString = toString.substring(indexEnd + 1);
        }
        this.id = Integer.parseInt(arrayList.get(0).replaceAll("[\\D]", ""));

        this.name = arrayList.get(1);

        this.listIndicateur = new ArrayList<Indicateur>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Indicateur> getListIndicateur() {
        return listIndicateur;
    }

    public void setListIndicateur(ArrayList<Indicateur> listIndicateur) {
        this.listIndicateur = listIndicateur;
    }

    public ArrayList<Integer> getListDayOn() {
        return listDayOn;
    }

    public void setListDayOn(ArrayList<Integer> listDayOn) {
        this.listDayOn = listDayOn;
    }

    @Override
    public String toString() {
        return "Espace{" +
                "id='" + id +
                "', name='" + name + "'}";
    }
}

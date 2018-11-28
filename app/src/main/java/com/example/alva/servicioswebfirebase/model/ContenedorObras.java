package com.example.alva.servicioswebfirebase.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContenedorObras {
    @SerializedName("paints")
    private List<Obra> obraList;

    public List<Obra> getObraList() {
        return obraList;
    }

    @Override
    public String toString() {
        return "ContenedorObras{" +
                "obraList=" + obraList +
                '}';
    }
}

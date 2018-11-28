package com.example.alva.servicioswebfirebase.model;

import com.google.gson.annotations.SerializedName;

public class Obra {
    @SerializedName("image")
    private String imagen;
    @SerializedName("name")
    private String nombre;
    @SerializedName("artistId")
    private Integer id;


    public String getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getId() {
        return id;
    }

}

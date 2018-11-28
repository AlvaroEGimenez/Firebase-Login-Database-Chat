package com.example.alva.servicioswebfirebase.dao;

import com.example.alva.servicioswebfirebase.model.ContenedorObras;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceObra {
@GET("bins/x858r")
    Call<ContenedorObras> getObras();
}
